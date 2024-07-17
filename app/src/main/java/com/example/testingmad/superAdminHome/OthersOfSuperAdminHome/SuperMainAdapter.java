package com.example.testingmad.superAdminHome.OthersOfSuperAdminHome;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
import com.example.testingmad.adminHome.EditProduct;
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
import java.util.Objects;

public class SuperMainAdapter extends RecyclerView.Adapter<SuperMainAdapter.MainViewHolder> {

    Context context;
    ArrayList<SuperMainModel> list;

    public SuperMainAdapter(Context context, ArrayList<SuperMainModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SuperMainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.one_card_super_admin, parent, false);
        return new SuperMainAdapter.MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuperMainAdapter.MainViewHolder holder, int position) {
        SuperMainModel model = list.get(position);

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new SuperMainAdapter.LoadImageTask(holder.itemImage).execute(model.getImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set item name
        holder.itemName.setText(model.getName());

        // Set item price
        holder.itemPrice.setText(model.getPrice());

        // Set available quantity
        holder.itemQty.setText(model.getQty());

        //set seller id
//        holder.forseller.setText(model.getSeller());

        // More info
        holder.moreinfoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCodex = model.getItemCode();
                Intent i = new Intent(context, ItemsMoreInfo.class);
                i.putExtra("ItemCode", itemCodex);
                context.startActivity(i);
            }
        });

        //delete button
        holder.deleteproductsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Alert for confirm delete
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure delete this product ?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Items").child(model.getItemCode());
                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(v.getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                                }
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty, forseller;
        ImageButton deleteproductsa;
        ImageView itemImage;
        LinearLayout moreinfoad;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.forNamesa);
            itemPrice = itemView.findViewById(R.id.forPricesa);
            itemImage = itemView.findViewById(R.id.forimgsa);
            itemQty = itemView.findViewById(R.id.forQtysa);
//            forseller = itemView.findViewById(R.id.forseller);
            deleteproductsa = itemView.findViewById(R.id.deleteproductsa);
            moreinfoad = itemView.findViewById(R.id.moreinfoad);
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
