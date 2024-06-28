//package com.example.testingmad.cusHome.OthersOfHome;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.testingmad.R;
//import com.example.testingmad.adminHome.OthersOfHome.MainModel;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class MainAdapter extends RecyclerView.Adapter<Ma.MainViewHolder> {
//
//    Context context;
//    ArrayList<com.example.testingmad.adminHome.OthersOfHome.MainModel> list;
//
//    public MainAdapter(Context context, ArrayList<com.example.testingmad.adminHome.OthersOfHome.MainModel> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public com.example.testingmad.adminHome.OthersOfHome.MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.one_card, parent, false);
//        return new com.example.testingmad.adminHome.OthersOfHome.MainAdapter.MainViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull com.example.testingmad.adminHome.OthersOfHome.MainAdapter.MainViewHolder holder, int position) {
//        MainModel model = list.get(position);
//
//        // Load image
//        if (model.getImage() != null && !model.getImage().isEmpty()) {
//            new com.example.testingmad.adminHome.OthersOfHome.MainAdapter.LoadImageTask(holder.itemImage).execute(model.getImage());
//        } else {
//            holder.itemImage.setImageResource(R.drawable.ic_launcher_background);
//        }
//
//        // Set item name
//        holder.itemName.setText(model.getName());
//
//        // Set item price
//        holder.itemPrice.setText(model.getPrice());
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class MainViewHolder extends RecyclerView.ViewHolder {
//
//        TextView itemName, itemPrice;
//        ImageView itemImage;
//
//        public MainViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            itemName = itemView.findViewById(R.id.forName);
//            itemPrice = itemView.findViewById(R.id.forPrice);
//            itemImage = itemView.findViewById(R.id.forimg);
//        }
//    }
//
//    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//
//        private ImageView imageView;
//
//        public LoadImageTask(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String imageUrl = strings[0];
//            Bitmap bitmap = null;
//            try {
//                URL url = new URL(imageUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                InputStream inputStream = connection.getInputStream();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }
//}