package com.example.testingmad.superAdminHome;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testingmad.R;
import com.example.testingmad.cusFragments.Cus_CartFragment;
import com.example.testingmad.cusFragments.Cus_Fragment;
import com.example.testingmad.cusFragments.Cus_HomeFragment;
import com.example.testingmad.cusFragments.Cus_OrderFragment;
import com.example.testingmad.superAdminHome.superAdminFragments.SuperAdminHomeFragment;
import com.example.testingmad.superAdminHome.superAdminFragments.SuperAdminNewProductFragment;
import com.example.testingmad.superAdminHome.superAdminFragments.SuperAdminNotificationFragment;
import com.example.testingmad.superAdminHome.superAdminFragments.SuperAdminOrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SuperAdminHomeActivity extends AppCompatActivity {

    private BottomNavigationView bNavView;
    private FrameLayout frm;
    MenuItem sasout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_super_admin_home);

        bNavView = findViewById(R.id.bottonNavSA);
        frm = findViewById(R.id.frame_layout_sa);

        loadFragment(new SuperAdminHomeFragment(), false);

        bNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.sachome){
                    loadFragment(new SuperAdminHomeFragment(), false);

                }else if(itemId == R.id.saprod){
                    loadFragment(new SuperAdminNewProductFragment(), false);

                }else if(itemId == R.id.saorders){
                    loadFragment(new SuperAdminNotificationFragment(), false);

                }else if(itemId == R.id.sanoti){
                    loadFragment(new SuperAdminOrdersFragment(), false);

                }else{
                    loadFragment(new SuperAdminHomeFragment(), false);
                }

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInsialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInsialized){
            fragmentTransaction.add(R.id.frame_layout_sa, fragment);

        }else{

            fragmentTransaction.replace(R.id.frame_layout_sa, fragment);
        }

        fragmentTransaction.commit();
    }
}