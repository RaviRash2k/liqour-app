package com.example.testingmad.superAdminHome.SuperAdminProductManage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.OrderManager.OrderForm;
import com.example.testingmad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PendingProductAdapter extends RecyclerView.Adapter<PendingProductAdapter.MainViewHolder>{

    Context context;
    ArrayList<PendingProductModel> list;

    public PendingProductAdapter(Context context, ArrayList<PendingProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PendingProductAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_product, parent, false);
        return new PendingProductAdapter.MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingProductAdapter.MainViewHolder holder, int position) {
        if (list == null) {
            Log.e("PendingProductAdapter", "List is null");
            return;
        }
        if (position < 0 || position >= list.size()) {
            Log.e("PendingProductAdapter", "Invalid position: " + position);
            return;
        }

        PendingProductModel model = list.get(position);

        // Set item name
        holder.itemName.setText(model.getName());

        // Set item price
        holder.itemPrice.setText(model.getPrice());

        // Set item description
        holder.itemDesc.setText(model.getDescription());

        // Set item seller id
        holder.seller.setText(model.getSeller());

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new PendingProductAdapter.LoadImageTask(holder.itemImage).execute(model.getImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Click reject button
        holder.pendingReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert for confirm reject
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure reject this product?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Pending Items").child(model.getItemId());
                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Product rejected successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(v.getContext(), "Failed to reject product", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // Click accept button
        holder.pendingAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alert for confirm accept
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure accept this product?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add to Item table when accepted
                        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child("Items");

                        String key = database2.push().getKey();

                        database2.child(key).child("User").setValue(model.getSeller());
                        database2.child(key).child("itemDescription").setValue(model.getDescription());
                        database2.child(key).child("itemImage").setValue(model.getImage());
                        database2.child(key).child("itemName").setValue(model.getName());
                        database2.child(key).child("itemPrice").setValue(model.getPrice());
                        database2.child(key).child("itemQuantity").setValue(model.getQty());
                        database2.child(key).child("itemType").setValue(model.getType());

                        // Remove from Pending Items table when accepted
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Pending Items").child(model.getItemId());
                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemDesc, seller;
        ImageView itemImage;
        Button pendingReject, pendingAccept;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.pendingName);
            itemPrice = itemView.findViewById(R.id.pendingPrice);
            itemImage = itemView.findViewById(R.id.pendingImg);
            itemDesc = itemView.findViewById(R.id.pendingDescription);
            seller = itemView.findViewById(R.id.pendingSellerId);

            pendingReject = itemView.findViewById(R.id.pendingReject);
            pendingAccept = itemView.findViewById(R.id.pendingAccept);
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
