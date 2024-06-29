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

        DB = FirebaseDatabase.getInstance().getReference();


        //Register
        btn = findViewById(R.id.reg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = rname.getText().toString();
                String email = remail.getText().toString();
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

                    DB.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(email)){
                                Toast.makeText(RegisterActivity.this, "Already have an account", Toast.LENGTH_SHORT).show();
                            }else{

                                DB.child("Users").child(email).child("userName").setValue(name);
                                DB.child("Users").child(email).child("mobile").setValue(mobile);
                                DB.child("Users").child(email).child("password").setValue(pass);
                                DB.child("Users").child(email).child("type").setValue(type);

                                if(type == "admin"){

                                    DB.child("Users").child(email).child("proPic").setValue("");
                                }

                                Toast.makeText(RegisterActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

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