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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmad.R;
import com.example.testingmad.RegisterActivity;
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
    TextView foodSelect, liqorSelect, x, y;
    String liqOfood, k;

    Button addCart, order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cus__home, container, false);


        recyclerView = rootView.findViewById(R.id.rview1);
        database = FirebaseDatabase.getInstance().getReference().child("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new MainAdapterCus(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        foodSelect = rootView.findViewById(R.id.foodSelect);
        liqorSelect = rootView.findViewById(R.id.liqorSelect);
        x = rootView.findViewById(R.id.x);
        y = rootView.findViewById(R.id.y);

        addCart = rootView.findViewById(R.id.addCart);
        order = rootView.findViewById(R.id.order);

        liqOfood = "food";

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference DB = FirebaseDatabase.getInstance().getReference();

//                DB.child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
//                        String customer = sharedPreferences.getString("userEmail", "");
//
//
//                            DB.child("Cart").child(k).child("customer").setValue(customer);
//                            DB.child("Cart").child(k).child("mobile").setValue(mobile);
//                            DB.child("Cart").child(k).child("password").setValue(pass);
//                            DB.child("Cart").child(k).child("type").setValue(type);
//
//                            if(type == "admin"){
//
//                                DB.child("Users").child(email).child("proPic").setValue("");
//                            }
//
//                            Toast.makeText(RegisterActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
//                            finish();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });

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

                                MainModel2 mainModel2 = new MainModel2();

                                mainModel2.setItemName(on1);
                                mainModel2.setItemPrice(on2);
                                mainModel2.setItmImage(on3);
                                mainModel2.setItemQty(on4);

                                list.add(mainModel2);

                            }else if(liqOfood.equals("liquor") && itemType.equals("liquor")){

                                System.out.println("Drinks");

                                String on1 = itemSnapshot.child("itemName").getValue(String.class);
                                String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                                String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                                String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);

                                MainModel2 mainModel2 = new MainModel2();

                                mainModel2.setItemName(on1);
                                mainModel2.setItemPrice(on2);
                                mainModel2.setItmImage(on3);
                                mainModel2.setItemQty(on4);

                                list.add(mainModel2);
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

                                MainModel2 mainModel = new MainModel2();

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

                                MainModel2 mainModel = new MainModel2();

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

                        MainModel2 mainModel = new MainModel2();

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

                        MainModel2 mainModel = new MainModel2();

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