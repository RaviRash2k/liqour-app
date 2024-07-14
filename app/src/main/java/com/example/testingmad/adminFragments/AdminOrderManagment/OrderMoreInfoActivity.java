package com.example.testingmad.adminFragments.AdminOrderManagment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testingmad.R;
import com.example.testingmad.cusHome.ItemsMoreInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderMoreInfoActivity extends AppCompatActivity {

    TextView ordIdMI, ordnameMI, ordcusMI, ordemailMI, ordmobiMI, ordadMI, ordqtyMI;
    String orderCode;
    DatabaseReference DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_more_info);

        //Assign id to text views
        ordIdMI = findViewById(R.id.ordIdMI);
        ordnameMI = findViewById(R.id.ordnameMI);
        ordcusMI = findViewById(R.id.ordcusMI);
        ordemailMI = findViewById(R.id.ordemailMI);
        ordmobiMI = findViewById(R.id.ordmobiMI);
        ordadMI = findViewById(R.id.ordadMI);
        ordqtyMI = findViewById(R.id.ordqtyMI);

        // Get orderCode from intent
        Intent intent = getIntent();
        orderCode = intent.getStringExtra("orderCode");

        DB = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderCode);

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("OrderItemName").getValue(String.class);
                    String cus = dataSnapshot.child("OrderCusId").getValue(String.class);
                    String email = dataSnapshot.child("OrderEmail").getValue(String.class);
                    String mobile = dataSnapshot.child("OrderMobile").getValue(String.class);
                    String address = dataSnapshot.child("OrderAddress").getValue(String.class);
                    String qty = dataSnapshot.child("OrderQty").getValue(String.class);

                    // Set items
                    ordIdMI.setText(orderCode);
                    ordnameMI.setText(name);
                    ordcusMI.setText(cus);
                    ordemailMI.setText(email);
                    ordmobiMI.setText(mobile);
                    ordadMI.setText(address);
                    ordqtyMI.setText(qty);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}