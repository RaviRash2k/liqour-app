package com.example.testingmad.adminFragments.AdminOrderManagment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
import com.example.testingmad.OrderManager.OrderModel;
import com.example.testingmad.cusHome.ItemsMoreInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MainViewHolder> {

    Context context;
    ArrayList<AdminOrderModel> list;

    public AdminOrderAdapter(Context context, ArrayList<AdminOrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_order_card, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        AdminOrderModel model = list.get(position);

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new LoadImageTask(holder.itemImage).execute(model.getImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set order details
        holder.id.setText(model.getOrderId());
        holder.itemName.setText(model.getItemName());
        holder.itemPrice.setText(model.getPrice());
        holder.itemQty.setText(model.getQuantity());
        holder.status.setText(model.getStatus());

        //click more info of order
        holder.ordMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderCodex = model.getOrderId();

                Intent i = new Intent(context, OrderMoreInfoActivity.class);
                i.putExtra("orderCode", orderCodex);
                context.startActivity(i);
            }
        });

        String status = model.getStatus();

        // Handle different statuses
        if (status.equals("pending")) {
            holder.acceptOrder.setVisibility(View.VISIBLE);
            holder.addToShipping.setVisibility(View.GONE);
            holder.finishOrder.setVisibility(View.GONE);
            holder.adCancelOrder.setVisibility(View.GONE);

            holder.acceptOrder.setOnClickListener(v -> showConfirmationDialog(v, model.getOrderId(), "packing", "Order added to packing"));

        } else if (status.equals("packing")) {
            holder.addToShipping.setVisibility(View.VISIBLE);
            holder.acceptOrder.setVisibility(View.GONE);
            holder.finishOrder.setVisibility(View.GONE);
            holder.adCancelOrder.setVisibility(View.GONE);

            holder.addToShipping.setOnClickListener(v -> showConfirmationDialog(v, model.getOrderId(), "shipping", "Order added to shipping"));

        } else if (status.equals("shipping")) {
            holder.finishOrder.setVisibility(View.VISIBLE);
            holder.acceptOrder.setVisibility(View.GONE);
            holder.addToShipping.setVisibility(View.GONE);
            holder.adCancelOrder.setVisibility(View.GONE);

            holder.finishOrder.setOnClickListener(v -> showConfirmationDialog(v, model.getOrderId(), "delivered", "Order finished"));

        }else if (status.equals("delivered")) {
            holder.acceptOrder.setVisibility(View.GONE);
            holder.addToShipping.setVisibility(View.GONE);
            holder.adCancelOrder.setVisibility(View.GONE);
            holder.finishOrder.setVisibility(View.GONE);

        } else if (status.equals("cancelled")) {
            holder.adCancelOrder.setVisibility(View.VISIBLE);
            holder.finishOrder.setVisibility(View.GONE);
            holder.acceptOrder.setVisibility(View.GONE);
            holder.addToShipping.setVisibility(View.GONE);

            holder.adCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure delete order ?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Orders").child(model.getOrderId());
                            database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Order deleted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to delete order", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //Order's quantity add again for available qty
                            int canselQty = Integer.parseInt(model.getQuantity());

                            DatabaseReference dbForItems = FirebaseDatabase.getInstance().getReference().child("Items").child(model.getItemID()).child("itemQuantity");
                            dbForItems.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        int itemQuantity = Integer.parseInt(snapshot.getValue(String.class)) + canselQty;
                                        String strItemQuantity = String.valueOf(itemQuantity);
                                        dbForItems.setValue(strItemQuantity);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    holder.itemQty.setText("Error");
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showConfirmationDialog(View v, String orderId, String newStatus, String successMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", (dialog, which) -> updateOrderStatus(v, orderId, newStatus, successMessage));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void updateOrderStatus(View v, String orderId, String newStatus, String successMessage) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    database.child("Status").setValue(newStatus);
                    Toast.makeText(v.getContext(), successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty, id, status;
        ImageView itemImage;
        Button acceptOrder, addToShipping, finishOrder, adCancelOrder;
        LinearLayout ordMoreInfo;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.ordnad);
            itemPrice = itemView.findViewById(R.id.ordpad);
            itemImage = itemView.findViewById(R.id.ordImgad);
            itemQty = itemView.findViewById(R.id.ordqad);
            id = itemView.findViewById(R.id.ordiad);
            status = itemView.findViewById(R.id.ordsad);
            acceptOrder = itemView.findViewById(R.id.acceptOrder);
            addToShipping = itemView.findViewById(R.id.addToShipping);
            finishOrder = itemView.findViewById(R.id.finishOrder);
            adCancelOrder = itemView.findViewById(R.id.adCanselOrder);
            ordMoreInfo = itemView.findViewById(R.id.ordMoreInfo);
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}