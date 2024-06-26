package com.example.testingmad.adminFragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testingmad.R;
import com.example.testingmad.adminHome.AdminHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddFragment extends Fragment {

    EditText itemName, itemPrice, itemDesc, itemQty;

    Button btn;
    String type, user;
    CheckBox food, liquor;
    DatabaseReference DB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        itemName = rootView.findViewById(R.id.edititemName);
        itemPrice = rootView.findViewById(R.id.edititemPrice);
        itemDesc = rootView.findViewById(R.id.edititemDesc);
        itemQty = rootView.findViewById(R.id.editqty);

        SharedPreferences sharedPreference = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
        user = sharedPreference.getString("userEmail","");

        DB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testingmad-82201-default-rtdb.firebaseio.com/");
        String key = DB.child("Items").push().getKey();

        btn = rootView.findViewById(R.id.itemBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String desc = itemDesc.getText().toString();
                String qty = itemQty.getText().toString();

                //check food or liquor
                food = rootView.findViewById(R.id.checkfood);
                liquor = rootView.findViewById(R.id.checkliquor);

                if (food.isChecked() && liquor.isChecked()) {
                    type = "invalid";
                } else if (food.isChecked()) {
                    type = "food";
                } else if (liquor.isChecked()) {
                    type = "liquor";
                }else{
                    type = "invalid";
                }

                if(name.isEmpty() || price.isEmpty() || desc.isEmpty() || qty.isEmpty() || type == "invalid"){

                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }else{

                    DB.child("Items").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(key)){
                                Toast.makeText(getContext(), "Already have an item", Toast.LENGTH_SHORT).show();
                            }else {

                                DB.child("Items").child(key).child("itemName").setValue(name);
                                DB.child("Items").child(key).child("itemPrice").setValue(price);
                                DB.child("Items").child(key).child("itemDescription").setValue(desc);
                                DB.child("Items").child(key).child("itemQuantity").setValue(qty);
                                DB.child("Items").child(key).child("itemType").setValue(type);
                                DB.child("Items").child(key).child("User").setValue(user);

                                Toast.makeText(getContext(), "Data Inserted", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(getActivity(), AdminHome.class);
                                startActivity(i);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        return rootView;
    }
}