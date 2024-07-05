package com.example.testingmad.OrderManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.testingmad.R;
import com.example.testingmad.cusFragments.Cus_HomeFragment;
import com.example.testingmad.cusHome.CustomerHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderForm extends AppCompatActivity {

    EditText ordEmail, ordMobile, ordAddress;
    TextView ordDec, ordQty, ordInc, unitPrice, qtyOfOrd, finalPrice;
    Button ordCansel, ordBtn;
    int orderCount = 1;
    String itemCode;
    DatabaseReference DB;

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




        //ItemCode
        Intent intent = getIntent();
        itemCode = intent.getStringExtra("ItemCode");

        //Order quantity inc and dec
        ordDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderCount > 1){
                    orderCount --;
                    ordQty.setText(String.valueOf(orderCount));
                }else{}
            }
        });

        ordInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                orderCount++;
                ordQty.setText(String.valueOf(orderCount));
            }
        });



        //Cansel button
//        ordCansel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(OrderForm.this, CustomerHome.class);
//                startActivity(i);
//            }
//        });

    }
}