package com.example.testingmad.adminHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testingmad.Forgot.Forgot;
import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProduct extends AppCompatActivity {

    EditText editname, editprice, editqty, editdesc;
    TextView delpro;
    Button editcansel, editupdate;

    DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Items");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_product);

//        editname = findViewById(R.id.editname);
//        editprice = findViewById(R.id.editprice);
//        editqty = findViewById(R.id.editqty);
//        editdesc = findViewById(R.id.editdesc);
//
//        delpro = findViewById(R.id.delpro);
//
//        editcansel = findViewById(R.id.editcansel);
//        editupdate = findViewById(R.id.editupdate);
//
//        //Update button click
//        editupdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String name = editname.getText().toString();
//                String price = editprice.getText().toString();
//                String qty = editqty.getText().toString();
//                String desc = editdesc.getText().toString();
//
//                if(name.isEmpty() || price.isEmpty() || qty.isEmpty() || desc.isEmpty()){
//
//                    Toast.makeText(EditProduct.this,"Fill all fields",Toast.LENGTH_SHORT).show();
//
//                }else{
//
//                    DB.orderByKey().equalTo(getEmail).addListenerForSingleValueEvent(new ValueEventListener() {
//
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            if (dataSnapshot.exists()) {
//
//                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                                    String userId = userSnapshot.getKey();
//
//                                    DB.child(userId).child("password").setValue(getPass);
//
//                                    Toast.makeText(EditProduct.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(EditProduct.this, MainActivity.class));
//                                    finish();
//                                    return;
//                                }
//
//                            }else{
//                                Toast.makeText(EditProduct.this,"Email doesn't exit",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//        });

    }
}