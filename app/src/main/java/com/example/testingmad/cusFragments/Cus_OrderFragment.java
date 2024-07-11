package com.example.testingmad.cusFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testingmad.OrderManager.OrderAdapter;
import com.example.testingmad.OrderManager.OrderModel;
import com.example.testingmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cus_OrderFragment extends Fragment {

    DatabaseReference database;
    TextView pending, shipped, deliver, packing;
    RecyclerView recyclerView;
    String status = "pending";
    ArrayList<OrderModel> list;
    OrderAdapter adapter;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cus__order, container, false);

        recyclerView = rootView.findViewById(R.id.rview4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pending = rootView.findViewById(R.id.pending);
        shipped = rootView.findViewById(R.id.shiped);
        deliver = rootView.findViewById(R.id.deliver);
        packing = rootView.findViewById(R.id.packing);

        database = FirebaseDatabase.getInstance().getReference().child("Orders");
        sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);

        list = new ArrayList<>();
        adapter = new OrderAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        fetchDataFromDatabase();

        //pending button
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "pending";
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                fetchDataFromDatabase();
            }
        });

        //packing
        packing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "packing";
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                fetchDataFromDatabase();
            }
        });

        //shipped button
        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "shipped";
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                fetchDataFromDatabase();
            }
        });

        //deliver button
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "delivered";
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
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
                    String customer = itemSnapshot.child("OrderCusId").getValue(String.class);

                    if (customer != null && customer.equals(sharedPreferences.getString("userEmail", ""))) {

                        String DBstatus = itemSnapshot.child("Status").getValue(String.class);

                        if ((DBstatus != null) &&
                                ((status.equals("pending") && DBstatus.equals("pending")) ||
                                        (status.equals("shipped") && DBstatus.equals("shipped")) ||
                                        (status.equals("delivered") && DBstatus.equals("delivered")) ||
                                        (status.equals("packing") && DBstatus.equals("packing")))) {

                            String itemId = itemSnapshot.child("OrderItemId").getValue(String.class);

                            String on1 = itemSnapshot.child("OrderItemName").getValue(String.class);
                            String on2 = itemSnapshot.child("image").getValue(String.class);
                            String on3 = itemSnapshot.child("OrderLastPrice").getValue(String.class);
                            String on4 = itemSnapshot.child("OrderQty").getValue(String.class);
                            String on5 = itemSnapshot.child("Status").getValue(String.class);
                            String on6 = itemSnapshot.getKey();

                            OrderModel mainModel = new OrderModel();
                            mainModel.setItemName(on1);
                            mainModel.setPrice(on3);
                            mainModel.setImage(on2);
                            mainModel.setQuantity(on4);
                            mainModel.setStatus(on5);
                            mainModel.setOrderId(on6);
                            mainModel.setItemID(itemId);

                            list.add(mainModel);
                        }else{ }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
