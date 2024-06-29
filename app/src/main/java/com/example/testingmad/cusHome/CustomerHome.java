package com.example.testingmad.cusHome;

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
import com.example.testingmad.adminFragments.AddFragment;
import com.example.testingmad.adminFragments.HomeFragment;
import com.example.testingmad.adminFragments.NotificationFragment;
import com.example.testingmad.cusFragments.Cus_CartFragment;
import com.example.testingmad.cusFragments.Cus_Fragment;
import com.example.testingmad.cusFragments.Cus_HomeFragment;
import com.example.testingmad.cusFragments.Cus_OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerHome extends AppCompatActivity {

    private BottomNavigationView bNavView;
    private FrameLayout frm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cus_home);


        bNavView = findViewById(R.id.bottonNav);
        frm = findViewById(R.id.frame_layout);

        loadFragment(new Cus_HomeFragment(), false);

        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.nchome){
                    loadFragment(new Cus_HomeFragment(), false);

                }else if(itemId == R.id.ncadd){
                    loadFragment(new Cus_Fragment(), false);

                }else if(itemId == R.id.ncart){
                    loadFragment(new Cus_CartFragment(), false);

                }else if(itemId == R.id.norders){
                    loadFragment(new Cus_OrderFragment(), false);

                }else{
                    loadFragment(new Cus_HomeFragment(), false);
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