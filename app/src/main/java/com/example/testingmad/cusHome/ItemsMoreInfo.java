package com.example.testingmad.cusHome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.CartFiles.MainAdapterOrd;
import com.example.testingmad.CartFiles.MainModel3;
import com.example.testingmad.R;
import com.example.testingmad.feedbacks.FeedbackAdapter;
import com.example.testingmad.feedbacks.FeedbackModel;
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

public class ItemsMoreInfo extends AppCompatActivity {

    ImageView proImage;
    TextView proName, proCode, proPrice, proQty, proDesc;
    LinearLayout loutfbck;
    String itemCode, uName;
    ImageButton fbacksend;
    EditText fback;
    DatabaseReference DB;
    RecyclerView recyclerView;
    FeedbackAdapter myAdapter;
    ArrayList<FeedbackModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_more_info);

        // Get itemCode from intent
        Intent intent = getIntent();
        itemCode = intent.getStringExtra("ItemCode");

        recyclerView = findViewById(R.id.rview5);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ItemsMoreInfo.this));

        list = new ArrayList<>();
        myAdapter = new FeedbackAdapter(ItemsMoreInfo.this, list);
        recyclerView.setAdapter(myAdapter);

        proName = findViewById(R.id.proname);
        proCode = findViewById(R.id.procode);
        proPrice = findViewById(R.id.proprice);
        proQty = findViewById(R.id.proqty);
        proDesc = findViewById(R.id.prodesc);
        proImage = findViewById(R.id.proimg);

        loutfbck = findViewById(R.id.loutfbck);

        fbacksend = findViewById(R.id.fbacksend);
        fback = findViewById(R.id.fbackadd);

        fetchDataFromDatabase();

        SharedPreferences prf2 = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String tpe = prf2.getString("type", "");

        if(tpe.equals("customer")){
            loutfbck.setVisibility(View.VISIBLE);
        }else{
            loutfbck.setVisibility(View.GONE);
        }

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

        //Sending feedbacks
        fbacksend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user entered feedback
                String feedback = fback.getText().toString();

                if(feedback.isEmpty()){

                    Toast.makeText(ItemsMoreInfo.this, "Enter your feedback", Toast.LENGTH_SHORT).show();

                }else{
                    //get cus from sharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                    String customer = sharedPreferences.getString("userEmail", "");

                    //add data to db
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(itemCode);

                    database.child(customer).setValue(feedback);

                    fback.setText("");
                    Toast.makeText(ItemsMoreInfo.this, "Feedback added", Toast.LENGTH_SHORT).show();
                }
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

    private void fetchDataFromDatabase() {

        DatabaseReference databaseFb = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(itemCode);
        databaseFb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String user = dataSnapshot.getKey();
                    String feedback = dataSnapshot.getValue(String.class); // Fetch feedback directly

                    DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("userName");
                    userNameRef.addListenerForSingleValueEvent(new ValueEventListener() { // Use single event listener
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String userName = dataSnapshot.getValue(String.class); // Fetch userName
                                FeedbackModel mainModel = new FeedbackModel();
                                mainModel.setFeedback(feedback);
                                mainModel.setUserName(userName);
                                list.add(mainModel);
                                myAdapter.notifyDataSetChanged(); // Notify adapter inside this listener
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
