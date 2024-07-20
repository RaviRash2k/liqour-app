package com.example.testingmad.superAdminHome.superAdminFragments;

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

import com.example.testingmad.R;
import com.example.testingmad.superAdminHome.OthersOfSuperAdminHome.SuperMainModel;
import com.example.testingmad.superAdminHome.SuperAdminProductManage.PendingProductAdapter;
import com.example.testingmad.superAdminHome.SuperAdminProductManage.PendingProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperAdminNewProductFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    PendingProductAdapter myAdapter;
    ArrayList<PendingProductModel> list;
    SharedPreferences sharedPreferences;
    String itemCode;
    TextView noItemsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_super_admin_new_product, container, false);

        recyclerView = rootView.findViewById(R.id.pendingProducts);
        noItemsTextView = rootView.findViewById(R.id.noItemsTextView);
        database = FirebaseDatabase.getInstance().getReference().child("Pending Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new PendingProductAdapter(getContext(), list);
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
                    String on1 = itemSnapshot.child("itemName").getValue(String.class);
                    String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                    String on3 = itemSnapshot.child("itemImage").getValue(String.class);
                    String on4 = itemSnapshot.child("itemQuantity").getValue(String.class);
                    String on5 = itemSnapshot.child("itemDescription").getValue(String.class);
                    String on6 = itemSnapshot.child("User").getValue(String.class);
                    String on7 = itemSnapshot.child("itemType").getValue(String.class);
                    String itemCode = itemSnapshot.getKey();

                    PendingProductModel mainModel = new PendingProductModel();
                    mainModel.setName(on1 != null ? on1 : "Unknown Name");
                    mainModel.setPrice(on2 != null ? on2 : "Unknown Price");
                    mainModel.setImage(on3 != null ? on3 : "Unknown Image");
                    mainModel.setQty(on4 != null ? on4 : "Unknown Quantity");
                    mainModel.setDescription(on5 != null ? on5 : "Unknown Description");
                    mainModel.setSeller(on6 != null ? on6 : "Unknown Seller");
                    mainModel.setType(on7 != null ? on7 : "Unknown Type");
                    mainModel.setItemId(itemCode);

                    list.add(mainModel);
                }

                myAdapter.notifyDataSetChanged();

                if (list.isEmpty()) {
                    noItemsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noItemsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        });
    }
}
