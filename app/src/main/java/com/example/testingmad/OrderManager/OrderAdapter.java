package com.example.testingmad.OrderManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
import com.example.testingmad.OrderManager.OrderAdapter;
import com.example.testingmad.OrderManager.OrderModel;
import com.example.testingmad.adminHome.AdminHome;
import com.example.testingmad.adminHome.EditProduct;
import com.example.testingmad.cusHome.CustomerHome;
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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MainViewHolder>{

    Context context;
    ArrayList<OrderModel> list;


    public OrderAdapter(Context context, ArrayList<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_item_card, parent, false);
        return new OrderAdapter.MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MainViewHolder holder, int position) {
        OrderModel model = list.get(position);

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new OrderAdapter.LoadImageTask(holder.itemImage).execute(model.getImage());

        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        //Set order id
        holder.id.setText(model.getOrderId());

        //Set name
        holder.itemName.setText(model.getItemName());

        // Set price
        holder.itemPrice.setText(model.getPrice());

        //Set qty
        holder.itemQty.setText(model.getQuantity());

        //Set status
        holder.status.setText(model.getStatus());

        //cansel button and add feedback visibility
        String s = model.getStatus();

        if(s.equals("pending")){
            holder.ordDelete.setVisibility(View.VISIBLE);
            holder.fback.setVisibility(View.GONE);

            //delete button
            holder.ordDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Alert of click cansel when status pending

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
                                        Toast.makeText(v.getContext(), "Order deleted successfully", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(v.getContext(), "Failed to delete order", Toast.LENGTH_SHORT).show();
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

        }else if(s.equals("packing")){
            holder.ordDelete.setVisibility(View.VISIBLE);
            holder.fback.setVisibility(View.GONE);

            holder.ordDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Orders").child(model.getOrderId());

                    //Alert of click cansel when status pending
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure delete order ?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        database.child("Status").setValue("cancelled");

                                        Toast.makeText(v.getContext(), "Order deleted successfully", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(v.getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(v.getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
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
        else if(s.equals("shipping")){
            holder.ordDelete.setVisibility(View.GONE);
            holder.fback.setVisibility(View.GONE);

        }
        else if (s.equals("delivered")) {
            holder.fback.setVisibility(View.VISIBLE);
            holder.ordDelete.setVisibility(View.GONE);

        } else{
            holder.ordDelete.setVisibility(View.GONE);
            holder.fback.setVisibility(View.GONE);
        }

        //Send feedbacks
        holder.fbacksendinord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user entered feedback
                String feedback = holder.fbackinord.getText().toString();

                if(feedback.isEmpty()){

                    Toast.makeText(v.getContext(), "Enter your feedback", Toast.LENGTH_SHORT).show();

                }else{
                    //get cus from sharedPreferences
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
                    String customer = sharedPreferences.getString("userEmail", "");

                    //add data to db
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(model.getItemID());

                    database.child(customer).setValue(feedback);

                    holder.fbackinord.setText("");
                    Toast.makeText(v.getContext(), "Feedback added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty, id, status, ordDelete;
        ImageView itemImage;
        LinearLayout fback;
        EditText fbackinord;
        ImageButton fbacksendinord;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.ordn);
            itemPrice = itemView.findViewById(R.id.ordp);
            itemImage = itemView.findViewById(R.id.ordImg);
            itemQty = itemView.findViewById(R.id.ordq);
            id = itemView.findViewById(R.id.ordi);
            status = itemView.findViewById(R.id.ords);
            ordDelete = itemView.findViewById(R.id.ordd);
            fback = itemView.findViewById(R.id.fback);

            fbackinord = itemView.findViewById(R.id.fbackinord);
            fbacksendinord = itemView.findViewById(R.id.fbacksendinord);
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
