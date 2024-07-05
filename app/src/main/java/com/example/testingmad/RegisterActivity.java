package com.example.testingmad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    CheckBox checkbox;
    Button btn;
    TextView log;
    String type;
    EditText rname, remail, rmobile, rpass, reepass;
    DatabaseReference DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        rname = findViewById(R.id.rname);
        remail = findViewById(R.id.remail);
        rmobile = findViewById(R.id.rmobile);
        rpass = findViewById(R.id.rpass);
        reepass = findViewById(R.id.repass);

        DB = FirebaseDatabase.getInstance().getReference().child("Users");

        String key = DB.push().getKey();

        //Register
        btn = findViewById(R.id.reg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = rname.getText().toString();
                String email = remail.getText().toString().trim();
                String mobile = rmobile.getText().toString();
                String pass = rpass.getText().toString();
                String repass = reepass.getText().toString();

                //check admin or cus
                checkbox = findViewById(R.id.checkbox);

                if (checkbox.isChecked()) {
                    type = "admin";
                } else {
                    type = "customer";
                }

                if(name.isEmpty() || email.isEmpty() || mobile.isEmpty() || pass.isEmpty()){

                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();

                }else if(!pass.equals(repass)){
                    Toast.makeText(RegisterActivity.this, "Password not matching", Toast.LENGTH_SHORT).show();
                }

                else{

                    DB.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();

                            } else {
                                DB.child(key).child("userEmail").setValue(email);
                                DB.child(key).child("userName").setValue(name);
                                DB.child(key).child("mobile").setValue(mobile);
                                DB.child(key).child("password").setValue(pass);
                                DB.child(key).child("type").setValue(type);

                                Toast.makeText(RegisterActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        });


        //Already have account
        log = findViewById(R.id.tolog);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}