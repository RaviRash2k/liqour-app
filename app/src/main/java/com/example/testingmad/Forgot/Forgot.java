package com.example.testingmad.Forgot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Forgot extends AppCompatActivity {

    EditText email, pass, repass;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot);

        email = findViewById(R.id.femaill);
        pass = findViewById(R.id.fpass);
        repass = findViewById(R.id.frepass);

        btn = findViewById(R.id.submit);

        DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Users");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString();
                String getPass = pass.getText().toString();
                String getRePass = repass.getText().toString();

                if(getEmail.isEmpty() || getPass.isEmpty() || getRePass.isEmpty()){

                    Toast.makeText(Forgot.this,"Fill all fields",Toast.LENGTH_SHORT).show();

                }else{

                    if(!getPass.equals(getRePass)){

                        Toast.makeText(Forgot.this,"Passwords not matching",Toast.LENGTH_SHORT).show();

                    }else{
                        DB.orderByChild("userEmail").equalTo(getEmail).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        String userId = userSnapshot.getKey();

                                        DB.child(userId).child("password").setValue(getPass);

                                        Toast.makeText(Forgot.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Forgot.this, MainActivity.class));
                                        finish();
                                        return;
                                    }

                                }else{
                                    Toast.makeText(Forgot.this,"Email doesn't exit",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }
}