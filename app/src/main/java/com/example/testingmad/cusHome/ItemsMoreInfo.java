package com.example.testingmad.cusHome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ItemsMoreInfo extends AppCompatActivity {

    ImageView proImage;
    TextView proName, proCode, proPrice, proQty, proDesc;
    String itemCode;
    DatabaseReference DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_more_info);

        // Get itemCode from intent
        Intent intent = getIntent();
        itemCode = intent.getStringExtra("ItemCode");

        proName = findViewById(R.id.proname);
        proCode = findViewById(R.id.procode);
        proPrice = findViewById(R.id.proprice);
        proQty = findViewById(R.id.proqty);
        proDesc = findViewById(R.id.prodesc);
        proImage = findViewById(R.id.proimg);

        DB = FirebaseDatabase.getInstance().getReference().child("Items").child(itemCode);

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("itemName").getValue(String.class);
                    String itemPrice = dataSnapshot.child("itemPrice").getValue(String.class);
                    String availableQty = dataSnapshot.child("itemQuantity").getValue(String.class);
                    String desc = dataSnapshot.child("itemDescription").getValue(String.class);
                    String itmimage = dataSnapshot.child("itemImage").getValue(String.class);

                    // Set items
                    proName.setText(name);
                    proCode.setText(itemCode);
                    proPrice.setText(itemPrice);
                    proQty.setText(availableQty);
                    proDesc.setText(desc);

                    if (itmimage != null && !itmimage.isEmpty()) {
                        new LoadImageTask(proImage).execute(itmimage);
                    } else {
                        proImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
