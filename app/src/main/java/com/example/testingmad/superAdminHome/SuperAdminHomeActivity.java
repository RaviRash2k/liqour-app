package com.example.testingmad.superAdminHome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.testingmad.MainActivity;
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
                    loadFragment(new SuperAdminOrdersFragment(), false);

                }
//                else if(itemId == R.id.sanoti){
//                    loadFragment(new SuperAdminNotificationFragment(), false);
//
//                }
                else if(itemId == R.id.sasout){
                    signOut();

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

    public void signOut(){

        //Alert of click cansel when status pending

        AlertDialog.Builder builder = new AlertDialog.Builder(SuperAdminHomeActivity.this);
        builder.setMessage("Do you want to logout ?");

        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences sharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent i = new Intent(SuperAdminHomeActivity.this, MainActivity.class);
                startActivity(i);
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
}