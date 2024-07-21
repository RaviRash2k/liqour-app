package com.example.testingmad.adminHome;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testingmad.R;
import com.example.testingmad.adminFragments.AccountFragment;
import com.example.testingmad.adminFragments.AdOrdersFragment;
import com.example.testingmad.adminFragments.AddFragment;
import com.example.testingmad.adminFragments.HomeFragment;
import com.example.testingmad.adminFragments.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHome extends AppCompatActivity {

    private BottomNavigationView bNavView;
    private FrameLayout frm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        bNavView = findViewById(R.id.bottonNav);
        frm = findViewById(R.id.frame_layout);

        loadFragment(new HomeFragment(), false);

        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.nhome){
                    loadFragment(new HomeFragment(), false);

                }else if(itemId == R.id.norders){
                    loadFragment(new AdOrdersFragment(), false);

                }else if(itemId == R.id.nadd){
                    loadFragment(new AddFragment(), false);

                }
//                else if(itemId == R.id.nalarts){
//                    loadFragment(new NotificationFragment(), false);
//
//                }
                else if(itemId == R.id.nprofile){
                    loadFragment(new AccountFragment(), false);

                }else{
                    loadFragment(new HomeFragment(), false);
                }

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInsialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInsialized){
            fragmentTransaction.add(R.id.frame_layout, fragment);

        }else{

            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }

        fragmentTransaction.commit();
    }
}