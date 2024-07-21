package com.example.testingmad.cusFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmad.R;
import com.example.testingmad.RegisterActivity;
import com.example.testingmad.Search_Bar.SearchActivity;
import com.example.testingmad.adminHome.OthersOfHome.MainModel;
import com.example.testingmad.cusHome.OthersOfHomeCus.MainAdapterCus;
import com.example.testingmad.cusHome.OthersOfHomeCus.MainModel2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cus_HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    MainAdapterCus myAdapter;
    ArrayList<MainModel2> list;
    ImageButton searchImg;
    EditText search;
    ImageView foodSelect, liqorSelect, x, y;
    String liqOfood = "food";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cus__home, container, false);

        recyclerView = rootView.findViewById(R.id.rview1);
        database = FirebaseDatabase.getInstance().getReference().child("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MainAdapterCus(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        //search image
        searchImg = rootView.findViewById(R.id.searchImg);
        search = rootView.findViewById(R.id.search);

        foodSelect = rootView.findViewById(R.id.foodSelect);
        liqorSelect = rootView.findViewById(R.id.liqorSelect);
        x = rootView.findViewById(R.id.x);
        y = rootView.findViewById(R.id.y);

        foodSelect.setVisibility(View.VISIBLE);
        liqorSelect.setVisibility(View.GONE);

        //click search button
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchResult = search.getText().toString();

                if(!searchResult.isEmpty()){

                    Intent i = new Intent(getContext(), SearchActivity.class);
                    i.putExtra("Search", searchResult);
                    getContext().startActivity(i);

                    search.setText("");

                }else{ }
            }
        });

        fetchDataFromDatabase();

        //click food button
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liqOfood = "food";
                foodSelect.setVisibility(View.VISIBLE);
                liqorSelect.setVisibility(View.GONE);
                fetchDataFromDatabase();
            }
        });

        //click liquor button
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liqOfood = "liquor";
                foodSelect.setVisibility(View.GONE);
                liqorSelect.setVisibility(View.VISIBLE);
                fetchDataFromDatabase();
            }
        });

        return rootView;
    }

    private void fetchDataFromDatabase() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String itemType = itemSnapshot.child("itemType").getValue(String.class);

                    if ((liqOfood.equals("food") && itemType.equals("food")) ||
                            (liqOfood.equals("liquor") && itemType.equals("liquor"))) {

                        String on1 = itemSnapshot.child("itemName").getValue(String.class);
                        String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                        String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                        String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);
                        String seller = itemSnapshot.child("User").getValue(String.class);
                        String itemCode = itemSnapshot.getKey();


                        MainModel2 mainModel = new MainModel2();
                        mainModel.setItemName(on1);
                        mainModel.setItemPrice(on2);
                        mainModel.setItmImage(on3);
                        mainModel.setItemQty(on4);
                        mainModel.setSeller(seller);
                        mainModel.setItemCode(itemCode);

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
