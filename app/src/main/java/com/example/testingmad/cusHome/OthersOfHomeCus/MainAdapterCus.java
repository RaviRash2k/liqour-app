package com.example.testingmad.cusHome.OthersOfHomeCus;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.MainActivity;
import com.example.testingmad.OrderManager.OrderForm;
import com.example.testingmad.R;
import com.example.testingmad.RegisterActivity;
import com.example.testingmad.cusHome.ItemsMoreInfo;
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

        //Order button
        holder.orderCusHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, OrderForm.class);
                i.putExtra("ItemCode", model.getItemCode());

                context.startActivity(i);
            }
        });

        //Add cart button
        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Cart");

                SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
                String customer = sharedPreferences.getString("userEmail", "");


                DB.orderByChild("customer").equalTo(customer).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        boolean itemExists = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String itemCode = snapshot.child("ItemCode").getValue(String.class);
                            if (itemCode != null && itemCode.equals(model.getItemCode())) {
                                itemExists = true;
                                break;
                            }
                        }

                        if ((itemExists)) {

                            Toast.makeText(context, "Item Already added", Toast.LENGTH_SHORT).show();

                        } else {
                            String k = DB.push().getKey();
                            if (k != null) {
                                DB.child(k).child("customer").setValue(customer);
                                DB.child(k).child("itemName").setValue(model.getName());
                                DB.child(k).child("itemPrice").setValue(model.getPrice());
                                DB.child(k).child("itemImage").setValue(model.getImage());
                                DB.child(k).child("itemQuantity").setValue(model.getItemQty());
                                DB.child(k).child("seller").setValue(model.getSeller());
                                DB.child(k).child("ItemCode").setValue(model.getItemCode());
                            }

                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });

        //More info
        holder.moreinfo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemCodex = model.getItemCode();
                System.out.println("ItemCode from model: " + itemCodex);

                Intent i = new Intent(context, ItemsMoreInfo.class);
                i.putExtra("ItemCode", itemCodex);
                context.startActivity(i);
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

        TextView itemName, itemPrice, itemQty, moreinfo1;
        ImageView itemImage;
        Button addCart, orderCusHome;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            moreinfo1 = itemView.findViewById(R.id.moreinfo1);
            itemName = itemView.findViewById(R.id.forName);
            itemPrice = itemView.findViewById(R.id.forPrice);
            itemImage = itemView.findViewById(R.id.forimg);
            itemQty = itemView.findViewById(R.id.forQty);
            addCart = itemView.findViewById(R.id.addCart);
            orderCusHome = itemView.findViewById(R.id.orderCusHome);
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