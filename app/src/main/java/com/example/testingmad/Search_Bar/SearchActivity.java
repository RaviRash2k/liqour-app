package com.example.testingmad.Search_Bar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    SearchAdapter myAdapter;
    ArrayList<SearchModel> list;
    TextView noItemsText;
    ImageButton searchImg;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.searchRV);
        noItemsText = findViewById(R.id.no_items_text);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        list = new ArrayList<>();
        myAdapter = new SearchAdapter(SearchActivity.this, list);
        recyclerView.setAdapter(myAdapter);

        // Get itemCode from intent
        Intent intent = getIntent();
        String search = intent.getStringExtra("Search");

        //search image
        searchImg = findViewById(R.id.searchImgg);
        searchText = findViewById(R.id.searchh);

        searchText.setText(search);

        //click search button
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchResult = searchText.getText().toString();
                databaseReference.orderByChild("itemName").startAt(searchResult).endAt(searchResult + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        list.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String itemName = snapshot.child("itemName").getValue(String.class);
                            String itemPrice = snapshot.child("itemPrice").getValue(String.class);
                            String itemImage = snapshot.child("itemImage").getValue(String.class);
                            String itemQty = snapshot.child("itemQuantity").getValue(String.class);
                            String itemCode = snapshot.getKey();

                            SearchModel mainModel = new SearchModel();

                            mainModel.setName(itemName);
                            mainModel.setPrice(itemPrice);
                            mainModel.setImage(itemImage);
                            mainModel.setQty(itemQty);
                            mainModel.setItemCode(itemCode);

                            list.add(mainModel);
                        }

                        myAdapter.notifyDataSetChanged();

                        ImageView noItemImage = findViewById(R.id.noItemImage);

                        if (list.isEmpty()) {
                            noItemsText.setVisibility(View.VISIBLE);
                            noItemImage.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                            noItemsText.setText("No results found for \"" + searchResult +"\"");

                        } else {
                            noItemsText.setVisibility(View.VISIBLE);
                            noItemImage.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            noItemsText.setText("Results for \"" + searchResult +"\"");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });

            }
        });

        //load search function
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");

        databaseReference.orderByChild("itemName").startAt(search).endAt(search + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String itemName = snapshot.child("itemName").getValue(String.class);
                    String itemPrice = snapshot.child("itemPrice").getValue(String.class);
                    String itemImage = snapshot.child("itemImage").getValue(String.class);
                    String itemQty = snapshot.child("itemQuantity").getValue(String.class);
                    String itemCode = snapshot.getKey();

                    SearchModel mainModel = new SearchModel();

                    mainModel.setName(itemName);
                    mainModel.setPrice(itemPrice);
                    mainModel.setImage(itemImage);
                    mainModel.setQty(itemQty);
                    mainModel.setItemCode(itemCode);

                    list.add(mainModel);
                }

                myAdapter.notifyDataSetChanged();

                ImageView noItemImage = findViewById(R.id.noItemImage);

                if (list.isEmpty()) {
                    noItemsText.setVisibility(View.VISIBLE);
                    noItemImage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    noItemsText.setText("No results found for \"" + search +"\"");

                } else {
                    noItemsText.setVisibility(View.VISIBLE);
                    noItemImage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    noItemsText.setText("Results for \"" + search +"\"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}