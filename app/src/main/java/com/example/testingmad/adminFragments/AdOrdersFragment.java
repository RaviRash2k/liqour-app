package com.example.testingmad.adminFragments;

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

import com.example.testingmad.R;
import com.example.testingmad.adminFragments.AdminOrderManagment.AdminOrderAdapter;
import com.example.testingmad.adminFragments.AdminOrderManagment.AdminOrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdOrdersFragment extends Fragment {

    DatabaseReference database;
    TextView pending, shipped, deliver, packing, canceled;
    RecyclerView recyclerView;
    String status = "pending";
    ArrayList<AdminOrderModel> list;
    AdminOrderAdapter adapter;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ad_orders, container, false);

        recyclerView = rootView.findViewById(R.id.rview7);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pending = rootView.findViewById(R.id.pendingad);
        shipped = rootView.findViewById(R.id.shipedad);
        deliver = rootView.findViewById(R.id.deliverad);
        packing = rootView.findViewById(R.id.packingad);
        canceled = rootView.findViewById(R.id.canceled);

        database = FirebaseDatabase.getInstance().getReference().child("Orders");
        sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);

        list = new ArrayList<>();
        adapter = new AdminOrderAdapter(getContext(), list);
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
                canceled.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
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
                canceled.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                fetchDataFromDatabase();
            }
        });

        //shipped button
        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "shipping";
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                canceled.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
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
                canceled.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                fetchDataFromDatabase();
            }
        });

        //canceled button
        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "cancelled";
                packing.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                canceled.setTextColor(ContextCompat.getColor(requireContext(), R.color.my_green));
                shipped.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                pending.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                deliver.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
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
                    String seller = itemSnapshot.child("OrderSellerId").getValue(String.class);

                    if (seller != null && seller.equals(sharedPreferences.getString("userEmail", ""))) {

                        String DBstatus = itemSnapshot.child("Status").getValue(String.class);

                        if ((DBstatus != null) &&
                                ((status.equals("pending") && DBstatus.equals("pending")) ||
                                        (status.equals("shipping") && DBstatus.equals("shipping")) ||
                                        (status.equals("delivered") && DBstatus.equals("delivered")) ||
                                        (status.equals("packing") && DBstatus.equals("packing")) ||
                                        (status.equals("cancelled") && DBstatus.equals("cancelled")))) {

                            String itemId = itemSnapshot.child("OrderItemId").getValue(String.class);
                            String on1 = itemSnapshot.child("OrderItemName").getValue(String.class);
                            String on2 = itemSnapshot.child("image").getValue(String.class);
                            String on3 = itemSnapshot.child("OrderLastPrice").getValue(String.class);
                            String on4 = itemSnapshot.child("OrderQty").getValue(String.class);
                            String on5 = itemSnapshot.child("Status").getValue(String.class);
                            String on6 = itemSnapshot.getKey();

                            AdminOrderModel mainModel = new AdminOrderModel();
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