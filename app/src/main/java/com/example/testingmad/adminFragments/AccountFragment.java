package com.example.testingmad.adminFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.example.testingmad.adminHome.OthersOfHome.MainAdapter;
import com.example.testingmad.adminHome.OthersOfHome.MainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    MainAdapter myAdapter;
    ArrayList<MainModel> list;
    EditText ett;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        recyclerView = rootView.findViewById(R.id.rview2);
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testingmad-82201-default-rtdb.firebaseio.com/").child("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        list = new ArrayList<>();
        myAdapter = new MainAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        logout = rootView.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.clear();
                editor.apply();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();


                String x = sharedPreferences.getString("userEmail", "");
                System.out.println(x);

                for(DataSnapshot itemSnapshot : snapshot.getChildren()) {

                    String type = itemSnapshot.child("User").getValue(String.class);

                    if (type.equals(x)) {

                        String on1 = itemSnapshot.child("itemName").getValue(String.class);
                        String on2 = itemSnapshot.child("itemPrice").getValue(String.class);
                        String on3 = itemSnapshot.child("itemImage").getValue(String.class);

                        MainModel mainModel = new MainModel();

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

                System.out.println("error");
            }
        });

        return rootView;
    }
}