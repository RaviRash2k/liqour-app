package com.example.testingmad.cusHome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testingmad.MainActivity;
import com.example.testingmad.R;

public class CustomerHome extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cus_home);

        logout = findViewById(R.id.cuslogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreference = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(CustomerHome.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}