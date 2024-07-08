package com.example.testingmad.OrderManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.testingmad.R;
import com.example.testingmad.RegisterActivity;
import com.example.testingmad.cusFragments.Cus_HomeFragment;
import com.example.testingmad.cusHome.CustomerHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderForm extends AppCompatActivity {

    EditText ordEmail, ordMobile, ordAddress;
    TextView ordDec, ordQty, ordInc, unitPrice, qtyOfOrd, finalPrice, ordItemName;
    Button ordCansel, ordBtn;
    int orderCount = 1;
    int lastPrice;
    String itemCode, availableQty, itemName, itemPrice, seller, customer, itmimage;
    DatabaseReference DB, DBOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_form);

        //Edit texts
        ordEmail = findViewById(R.id.ordEmail);
        ordMobile = findViewById(R.id.ordMobile);
        ordAddress = findViewById(R.id.ordAddress);

        //Text views
        ordDec = findViewById(R.id.ordDec);
        ordQty = findViewById(R.id.ordQty);
        ordInc = findViewById(R.id.ordInc);
        unitPrice = findViewById(R.id.unitPrice);
        qtyOfOrd = findViewById(R.id.qtyOfOrd);
        finalPrice = findViewById(R.id.finalPrice);
        ordItemName = findViewById(R.id.oName);

        //Buttons
        ordCansel = findViewById(R.id.ordCansel);
        ordBtn = findViewById(R.id.ordBtn);

        //Get user code from sharedpref
        SharedPreferences sharedPreference = OrderForm.this.getSharedPreferences("CurrentUser", MODE_PRIVATE);
        customer = sharedPreference.getString("userEmail","");

        //get itemCode from intent
        Intent intent = getIntent();
        itemCode = intent.getStringExtra("ItemCode");

        //Take Item's data from firebase
        DB = FirebaseDatabase.getInstance().getReference().child("Items").child(itemCode);
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    itemName = dataSnapshot.child("itemName").getValue(String.class);
                    availableQty = dataSnapshot.child("itemQuantity").getValue(String.class);
                    itemPrice = dataSnapshot.child("itemPrice").getValue(String.class);
                    seller = dataSnapshot.child("User").getValue(String.class);
                    itmimage = dataSnapshot.child("itemImage").getValue(String.class);

                    //Set item name
                    ordItemName.setText(itemName);
                    unitPrice.setText(itemPrice);
                    finalPrice.setText(itemPrice);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Order quantity dec
        ordDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderCount > 1){
                    orderCount-- ;
                    ordQty.setText(String.valueOf(orderCount));
                    qtyOfOrd.setText(String.valueOf(orderCount));

                    //final price
                    lastPrice = (Integer.parseInt(itemPrice)) * orderCount;
                    finalPrice.setText(String.valueOf(lastPrice));
                }
            }
        });

        //Order quantity inc
        ordInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DB = FirebaseDatabase.getInstance().getReference().child("Items").child(itemCode);

                int availableQuantity = Integer.parseInt(availableQty);

                if (orderCount < availableQuantity) {
                    orderCount++;
                    ordQty.setText(String.valueOf(orderCount));
                    qtyOfOrd.setText(String.valueOf(orderCount));

                    //final price
                    lastPrice = (Integer.parseInt(itemPrice)) * orderCount;
                    finalPrice.setText(String.valueOf(lastPrice));
                }
            }
        });

        //Order Button click
        ordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        //Cansel button
        ordCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OrderForm.this, CustomerHome.class);
                startActivity(i);
            }
        });

    }

    //Alert Message for order button click
    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm order");
        builder.setMessage("Are you sure order now ?");

        builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = ordEmail.getText().toString();
                String mobile =ordMobile.getText().toString();
                String address =ordAddress.getText().toString();

                if(email.isEmpty() || mobile.isEmpty() || address.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }else{

                    //Order data save on database
                    DBOrders = FirebaseDatabase.getInstance().getReference().child("Orders");

                    String key = DBOrders.push().getKey();
                    DBOrders.child(key).child("OrderItemName").setValue(itemName);
                    DBOrders.child(key).child("image").setValue(itmimage);
                    DBOrders.child(key).child("OrderEmail").setValue(email);
                    DBOrders.child(key).child("OrderMobile").setValue(mobile);
                    DBOrders.child(key).child("OrderAddress").setValue(address);
                    DBOrders.child(key).child("OrderQty").setValue(String.valueOf(orderCount));
                    DBOrders.child(key).child("OrderItemId").setValue(itemCode);
                    DBOrders.child(key).child("OrderCusId").setValue(customer);
                    DBOrders.child(key).child("OrderSellerId").setValue(seller);
                    DBOrders.child(key).child("OrderLastPrice").setValue(String.valueOf(lastPrice));
                    DBOrders.child(key).child("Status").setValue("pending");
                }
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
}