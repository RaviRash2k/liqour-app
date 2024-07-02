package com.example.testingmad.adminFragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingmad.MainActivity;
import com.example.testingmad.R;
import com.example.testingmad.adminHome.AdminHome;
import com.example.testingmad.adminHome.OthersOfHome.MainAdapter;
import com.example.testingmad.adminHome.OthersOfHome.MainModel;
import com.example.testingmad.cusHome.CustomerHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    RecyclerView recyclerView;
    TextView name, editPro, ok, cansel, ook, ccansel;
    DatabaseReference database, databaseTwo;
    StorageReference storageReference;
    MainAdapter myAdapter;
    ArrayList<MainModel> list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int PICK_IMAGE_REQUEST;
    Uri uri;
    ImageView proPic, edit;
    EditText proNameEdit;

    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        name = rootView.findViewById(R.id.proName);

        proNameEdit = rootView.findViewById(R.id.proNameEdit);

        editPro = rootView.findViewById(R.id.editPro);
        edit = rootView.findViewById(R.id.edit);

        recyclerView = rootView.findViewById(R.id.rview2);
        database = FirebaseDatabase.getInstance().getReference().child("Items");
        databaseTwo = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        storageReference = FirebaseStorage.getInstance().getReference();

        list = new ArrayList<>();
        myAdapter = new MainAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        logout = rootView.findViewById(R.id.logout);
        proPic = rootView.findViewById(R.id.proPic);

        ok = rootView.findViewById(R.id.ok);
        cansel = rootView.findViewById(R.id.cansel);

        ook = rootView.findViewById(R.id.ook);
        ccansel = rootView.findViewById(R.id.ccansel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name.setVisibility(View.GONE);
                proNameEdit.setVisibility(View.VISIBLE);
                ook.setVisibility(View.VISIBLE);
                ccansel.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
            }
        });

        ook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseTwo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String newName = proNameEdit.getText().toString();

                        if(newName.isEmpty()){

                            name.setVisibility(View.VISIBLE);
                            proNameEdit.setVisibility(View.GONE);
                            ook.setVisibility(View.GONE);
                            ccansel.setVisibility(View.GONE);
                            edit.setVisibility(View.VISIBLE);

                        }else{
                            databaseTwo.child(sharedPreferences.getString("userEmail", "")).child("userName").setValue(newName);
                            name.setVisibility(View.VISIBLE);
                            proNameEdit.setVisibility(View.GONE);
                            ook.setVisibility(View.GONE);
                            ccansel.setVisibility(View.GONE);
                            edit.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ccansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                proNameEdit.setVisibility(View.GONE);
                ook.setVisibility(View.GONE);
                ccansel.setVisibility(View.GONE);
            }
        });

        editPro.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "img clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                ok.setVisibility(View.VISIBLE);
                cansel.setVisibility(View.VISIBLE);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImageToFireBase(uri);

                ok.setVisibility(View.GONE);
                cansel.setVisibility(View.GONE);
            }
        });

        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.setVisibility(View.GONE);
                cansel.setVisibility(View.GONE);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.clear();
                editor.apply();

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        databaseTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(sharedPreferences.getString("userEmail", ""))) {

                    String getName = snapshot.child(sharedPreferences.getString("userEmail", "")).child("userName").getValue(String.class);
                    name.setText(getName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


    private void uploadImageToFireBase(Uri uri) {

        StorageReference file = storageReference.child(System.currentTimeMillis()+ "." +getFileExtension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        databaseTwo.child(sharedPreferences.getString("userEmail", "")).child("proPic").setValue(uri.toString());

                        Toast.makeText(getContext(), "Profile picture added successfully",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "Failled",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String getFileExtension(Uri uri) {

        if (uri == null) {
            return "";
        }

        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data !=null){

            uri = data.getData();

            if(uri != null){

                proPic.setImageURI(uri);
            }else{

            }
        }
    }
}