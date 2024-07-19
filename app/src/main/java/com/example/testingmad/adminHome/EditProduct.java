package com.example.testingmad.adminHome;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.testingmad.Forgot.Forgot;
import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        editname = findViewById(R.id.editname);
        editprice = findViewById(R.id.editprice);
        editqty = findViewById(R.id.editqty);
        editdesc = findViewById(R.id.editdesc);

        delpro = findViewById(R.id.delpro);

        editcansel = findViewById(R.id.editcansel);
        editupdate = findViewById(R.id.editupdate);

        //delete click
        //delete button
        delpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Alert for confirm delete
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure delete this product ?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Get itemCode from intent
                        Intent intent = getIntent();
                        String itemCode = intent.getStringExtra("ItemCode");

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Items").child(itemCode);
                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(v.getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        });

        //Update button click
        editupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editname.getText().toString();
                String price = editprice.getText().toString();
                String qty = editqty.getText().toString();
                String desc = editdesc.getText().toString();

                if(name.isEmpty() && price.isEmpty() && qty.isEmpty() && desc.isEmpty()){

                    Toast.makeText(EditProduct.this,"Fill need fields",Toast.LENGTH_SHORT).show();

                }else{
                    // Get itemCode from intent
                    Intent intent = getIntent();
                    String itemCode = intent.getStringExtra("ItemCode");

                    if (itemCode == null || itemCode.isEmpty()) {
                        Toast.makeText(EditProduct.this, "Item code is missing", Toast.LENGTH_SHORT).show();
                        finish();

                    }else{
                        DB.child(itemCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    //name
                                    if(!name.isEmpty()){
                                        DB.child(itemCode).child("itemName").setValue(name);
                                    }

                                    //price
                                    if(!price.isEmpty()){
                                        DB.child(itemCode).child("itemPrice").setValue(price);
                                    }

                                    //quantity
                                    if(!qty.isEmpty()){
                                        DB.child(itemCode).child("itemQuantity").setValue(qty);
                                    }

                                    //description
                                    if(!desc.isEmpty()){
                                        DB.child(itemCode).child("itemDescription").setValue(desc);
                                    }

                                    Toast.makeText(EditProduct.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditProduct.this, AdminHome.class));
                                    finish();

                                } else {
                                    Toast.makeText(EditProduct.this, "Item not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(EditProduct.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        //click update cansel button
        editcansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProduct.this, AdminHome.class));
            }
        });

    }
}