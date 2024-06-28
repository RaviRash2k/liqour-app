package com.example.testingmad.adminFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testingmad.R;
import com.example.testingmad.adminHome.OthersOfHome.MainAdapter;
import com.example.testingmad.adminHome.OthersOfHome.MainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    MainAdapter myAdapter;
    ArrayList<MainModel> list;
    TextView foodSelect, liqorSelect, x, y;
    String liqOfood;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.rview1);
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testingmad-82201-default-rtdb.firebaseio.com/").child("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MainAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        foodSelect = rootView.findViewById(R.id.foodSelect);
        liqorSelect = rootView.findViewById(R.id.liqorSelect);
        x = rootView.findViewById(R.id.x);
        y = rootView.findViewById(R.id.y);

        liqOfood = "food";

        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liqOfood = "liquor";
                foodSelect.setVisibility(View.GONE);
                liqorSelect.setVisibility(View.VISIBLE);
                System.out.println(liqOfood);

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear();

                        for(DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            String itemType = itemSnapshot.child("itemType").getValue(String.class);

                            if(liqOfood.equals("food") && itemType.equals("food")){
                                System.out.println("hello");

                                String on1 = itemSnapshot.child("itemName").getValue(String.class);
                                String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                                String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                                String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                                MainModel mainModel = new MainModel();

                                mainModel.setItemName(on1);
                                mainModel.setItemPrice(on2);
                                mainModel.setItmImage(on3);
                                mainModel.setItemQty(on4);

                                list.add(mainModel);

                            }else if(liqOfood.equals("liquor") && itemType.equals("liquor")){

                                System.out.println("Drinks");

                                String on1 = itemSnapshot.child("itemName").getValue(String.class);
                                String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                                String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                                String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                                MainModel mainModel = new MainModel();

                                mainModel.setItemName(on1);
                                mainModel.setItemPrice(on2);
                                mainModel.setItmImage(on3);
                                mainModel.setItemQty(on4);

                                list.add(mainModel);
                            }

                            System.out.println("Test 1");
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        System.out.println("error");
                    }
                });
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liqOfood = "food";
                foodSelect.setVisibility(View.VISIBLE);
                liqorSelect.setVisibility(View.GONE);
                System.out.println(liqOfood);

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear();

                        for(DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            String itemType = itemSnapshot.child("itemType").getValue(String.class);

                            if(liqOfood.equals("food") && itemType.equals("food")){
                                System.out.println("hello");

                                String on1 = itemSnapshot.child("itemName").getValue(String.class);
                                String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                                String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                                String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                                MainModel mainModel = new MainModel();

                                mainModel.setItemName(on1);
                                mainModel.setItemPrice(on2);
                                mainModel.setItmImage(on3);
                                mainModel.setItmImage(on4);
                                mainModel.setItemQty(on4);

                                list.add(mainModel);

                            }else if(liqOfood.equals("liquor") && itemType.equals("liquor")){

                                System.out.println("Drinks");

                                String on1 = itemSnapshot.child("itemName").getValue(String.class);
                                String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                                String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                                String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                                MainModel mainModel = new MainModel();

                                mainModel.setItemName(on1);
                                mainModel.setItemPrice(on2);
                                mainModel.setItmImage(on3);
                                mainModel.setItemQty(on4);

                                list.add(mainModel);
                            }

                            System.out.println("Test 1");
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        System.out.println("error");
                    }
                });
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for(DataSnapshot itemSnapshot : snapshot.getChildren()) {

                    String itemType = itemSnapshot.child("itemType").getValue(String.class);

                    if(liqOfood.equals("food") && itemType.equals("food")){
                        System.out.println("hello");

                        String on1 = itemSnapshot.child("itemName").getValue(String.class);
                        String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                        String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                        String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                        MainModel mainModel = new MainModel();

                        mainModel.setItemName(on1);
                        mainModel.setItemPrice(on2);
                        mainModel.setItmImage(on3);
                        mainModel.setItemQty(on4);

                        list.add(mainModel);

                    }else if(liqOfood.equals("liquor") && itemType.equals("liquor")){

                        System.out.println("Drinks");

                            String on1 = itemSnapshot.child("itemName").getValue(String.class);
                            String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                            String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                            String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                            MainModel mainModel = new MainModel();

                            mainModel.setItemName(on1);
                            mainModel.setItemPrice(on2);
                            mainModel.setItmImage(on3);
                            mainModel.setItemQty(on4);

                            list.add(mainModel);
                        }

                    System.out.println("Test 1");
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                System.out.println("error");
            }
        });

        return rootView;
    }
}