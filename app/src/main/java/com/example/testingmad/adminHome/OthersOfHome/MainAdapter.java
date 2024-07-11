package com.example.testingmad.adminHome.OthersOfHome;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
import com.example.testingmad.adminHome.EditProduct;
import com.example.testingmad.cusHome.ItemsMoreInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Context context;
    ArrayList<MainModel> list;

    public MainAdapter(Context context, ArrayList<MainModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.one_card, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainModel model = list.get(position);

        // Load image
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            new LoadImageTask(holder.itemImage).execute(model.getImage());
        } else {
            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set item name
        holder.itemName.setText(model.getName());

        // Set item price
        holder.itemPrice.setText(model.getPrice());

        //Set Available qty
        holder.itemQty.setText(model.getItemQty());

        //edit product icon visibility
        SharedPreferences sharedPreferences = context.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("userEmail", "");
        if(model.getSeller().equals(currentUser)){
            holder.editproduct.setVisibility(View.VISIBLE);
        }else{
            holder.editproduct.setVisibility(View.GONE);
        }

        //Edit product
        holder.editproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemCodex = model.getItemCode();

                Intent i = new Intent(context, EditProduct.class);
                i.putExtra("ItemCode", itemCodex);
                context.startActivity(i);
            }
        });


        //More info
        holder.moreinfoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemCodex = model.getItemCode();

                Intent i = new Intent(context, ItemsMoreInfo.class);
                i.putExtra("ItemCode", itemCodex);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQty, moreinfoad, editproduct;
        ImageView itemImage;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.forName);
            itemPrice = itemView.findViewById(R.id.forPrice);
            itemImage = itemView.findViewById(R.id.forimg);
            itemQty = itemView.findViewById(R.id.forQty);
            moreinfoad = itemView.findViewById(R.id.moreinfoad);
            editproduct = itemView.findViewById(R.id.editproduct);
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