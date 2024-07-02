package com.example.testingmad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testingmad.Forgot.Forgot;
import com.example.testingmad.adminHome.AdminHome;
import com.example.testingmad.cusHome.CustomerHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email, pass;
    TextView forget;
    Button btn;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView reg = findViewById(R.id.toreg);
        email = findViewById(R.id.emaill);
        pass = findViewById(R.id.lassword);
        btn = findViewById(R.id.login);

        Intent i = new Intent(this, RegisterActivity.class);

        DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Users");

        pref = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        editor = pref.edit();

        if(check()){

            SharedPreferences prf2 = getSharedPreferences("CurrentUser", MODE_PRIVATE);
            String x = prf2.getString("type", "");
            System.out.println(x);

            switch (x) {
                case "admin":
                    startActivity(new Intent(MainActivity.this, AdminHome.class));
                    finish();
                    break;

                case "customer":
                    startActivity(new Intent(MainActivity.this, CustomerHome.class));
                    finish();
                    break;

                default:
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Click", "Click");

                String emailtxt = email.getText().toString().trim();
                String passtxt = pass.getText().toString().trim();

                if (emailtxt.isEmpty() || passtxt.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();

                } else {

                    DB.orderByChild("userEmail").equalTo(emailtxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                    String userId = userSnapshot.getKey();
                                    String getPass = userSnapshot.child("password").getValue(String.class);
                                    String gettype = userSnapshot.child("type").getValue(String.class);

                                    if (passtxt != null && passtxt.equals(getPass)) {

                                        editor.putString("userEmail", userId);
                                        editor.putString("type", gettype);
                                        editor.putBoolean("logged", true);
                                        editor.apply();

                                        if ("customer".equals(gettype)) {
                                            Toast.makeText(MainActivity.this, "Successfully logged into Customer account", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, CustomerHome.class));
                                            finish();

                                        } else if ("admin".equals(gettype)) {
                                            Toast.makeText(MainActivity.this, "Successfully logged into Admin account", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, AdminHome.class));
                                            finish();

                                        } else {
                                            Toast.makeText(MainActivity.this, "Unknown user type", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(i);
            }
        });

        forget = findViewById(R.id.fogetbtn);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Forgot.class));
            }
        });
    }

    //not loggin
    private boolean check(){
        return pref.getBoolean("logged", false);
    }
}