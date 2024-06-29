package com.example.testingmad.cusHome.OthersOfHomeCus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
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

public class MainAdapterCus extends RecyclerView.Adapter<MainAdapterCus.MainViewHolder> {

    Context context;
    ArrayList<MainModel2> list;

    public MainAdapterCus(Context context, ArrayList<MainModel2> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_item, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainModel2 model = list.get(position);

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new LoadImageTask(holder.itemImage).execute(model.getImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (model.itemName != null && on2 != null && on3 != null && on4 != null) {
                    DatabaseReference DB = FirebaseDatabase.getInstance().getReference();

                    DB.child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
                            String customer = sharedPreferences.getString("userEmail", "");

                            String k = DB.child("Cart").push().getKey();
                            DB.child("Cart").child(k).child("customer").setValue(customer);
                            DB.child("Cart").child(k).child("itemName").setValue(model.getName());
                            DB.child("Cart").child(k).child("itemPrice").setValue(model.getPrice());
                            DB.child("Cart").child(k).child("itemImage").setValue(model.getImage());
                            DB.child("Cart").child(k).child("itemQuantity").setValue(model.getItemQty());
                            DB.child("Cart").child(k).child("seller").setValue(model.getSeller());
                            DB.child("Cart").child(k).child("ItemCode").setValue(model.getItemCode());

                            System.out.println(model.getPrice());
                            System.out.println(model.getSeller());
                            System.out.println(model.getItemCode());

                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
//                } else {
//                    Toast.makeText(getContext(), "Please select an item to add to cart", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Set item name
        holder.itemName.setText(model.getName());

        // Set item price
        holder.itemPrice.setText(model.getPrice());

        //Set Available qty
        holder.itemQty.setText(model.getItemQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty;
        ImageView itemImage;
        Button addCart;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.forName);
            itemPrice = itemView.findViewById(R.id.forPrice);
            itemImage = itemView.findViewById(R.id.forimg);
            itemQty = itemView.findViewById(R.id.forQty);
            addCart = itemView.findViewById(R.id.addCart);
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