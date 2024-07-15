package com.example.testingmad.CartFiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.testingmad.OrderManager.OrderForm;
import com.example.testingmad.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainAdapterOrd extends RecyclerView.Adapter<MainAdapterOrd.MainViewHolder> {

    Context context;
    ArrayList<MainModel3> list;

    public MainAdapterOrd(Context context, ArrayList<MainModel3> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainModel3 model = list.get(position);

        // Set item name
        holder.itemName.setText(model.getItemName());

        // Set item price
        holder.itemPrice.setText(model.getItemPrice());

        // Set available quantity
        holder.itemQty.setText(model.getItemQty());

        // Load image
        if (model.getItmImage() != null && !model.getItmImage().isEmpty()) {
            new LoadImageTask(holder.itemImage).execute(model.getItmImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Delete cart item
        holder.cartDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Cart").child(model.getCartCode());

                database.removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(context, "Cart item removed", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> System.err.println("Error removing data: " + e.getMessage()));
            }
        });

        // Click order button
        holder.ordInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OrderForm.class);
                i.putExtra("ItemCode", model.getItemCode());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty, cartDel;
        ImageView itemImage;
        Button ordInCart;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.forNameCart);
            itemPrice = itemView.findViewById(R.id.forPriceCart);
            itemImage = itemView.findViewById(R.id.forimgCart);
            itemQty = itemView.findViewById(R.id.forQtyCart);
            ordInCart = itemView.findViewById(R.id.orderOfCart);
            cartDel = itemView.findViewById(R.id.cartDel);
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
