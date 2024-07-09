package com.example.testingmad.cusFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmad.CartFiles.MainModel3;
import com.example.testingmad.R;
import com.example.testingmad.RegisterActivity;
import com.example.testingmad.CartFiles.MainAdapterOrd;
import com.example.testingmad.CartFiles.MainModel3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cus_CartFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    MainAdapterOrd myAdapter;
    ArrayList<MainModel3> list;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cus__cart, container, false);


        recyclerView = rootView.findViewById(R.id.rview3);
        database = FirebaseDatabase.getInstance().getReference().child("Cart");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MainAdapterOrd(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);

        fetchDataFromDatabase();

        return rootView;
    }
    private void fetchDataFromDatabase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String customer = itemSnapshot.child("customer").getValue(String.class);

                    if (customer.equals(sharedPreferences.getString("userEmail", ""))){

                        String on1 = itemSnapshot.child("itemName").getValue(String.class);
                        String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                        String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                        String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);
                        String seller = itemSnapshot.child("User").getValue(String.class);
                        String cartCode = itemSnapshot.getKey();
                        String itemCode = itemSnapshot.child("ItemCode").getValue(String.class);


                        MainModel3 mainModel = new MainModel3();
                        mainModel.setItemName(on1);
                        mainModel.setItemPrice(on2);
                        mainModel.setItmImage(on3);

                        list.add(mainModel);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
