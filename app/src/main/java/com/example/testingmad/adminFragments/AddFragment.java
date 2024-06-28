package com.example.testingmad.adminFragments;

import android.app.Activity;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testingmad.R;
import com.example.testingmad.adminHome.AdminHome;
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


public class AddFragment extends Fragment {

    EditText itemName, itemPrice, itemDesc, itemQty;

    Button btn;
    ImageView img;
    String type, user, k;
    CheckBox food, liquor;
    DatabaseReference DB;
    StorageReference storageReference;
    Uri uri;
    int PICK_IMAGE_REQUEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        itemName = rootView.findViewById(R.id.edititemName);
        itemPrice = rootView.findViewById(R.id.edititemPrice);
        itemDesc = rootView.findViewById(R.id.edititemDesc);
        itemQty = rootView.findViewById(R.id.editqty);
        img = rootView.findViewById(R.id.img);

        SharedPreferences sharedPreference = requireActivity().getSharedPreferences("CurrentUser", getContext().MODE_PRIVATE);
        user = sharedPreference.getString("userEmail","");

        storageReference = FirebaseStorage.getInstance().getReference();

        DB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://testingmad-82201-default-rtdb.firebaseio.com/").child("Items");
        String key = DB.child("Items").push().getKey();

        k = key;

        PICK_IMAGE_REQUEST = 2;

        btn = rootView.findViewById(R.id.itemBtn);

        //image open
        img.setOnClickListener(new View.OnClickListener() {
            @Nullable
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "img clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

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

                if(name.isEmpty() || price.isEmpty() || desc.isEmpty() || qty.isEmpty() || uri == null || type == "invalid"){

                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }else{

                    if(uri != null){
                        uploadImageToFireBase(uri);
                        Toast.makeText(getContext(), "img uploaded",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getContext(), "no image here",Toast.LENGTH_SHORT).show();
                    }

                    DB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(key)){
                                Toast.makeText(getContext(), "Already have an item", Toast.LENGTH_SHORT).show();
                            }else {

                                DB.child(key).child("itemName").setValue(name);
                                DB.child(key).child("itemPrice").setValue(price);
                                DB.child(key).child("itemDescription").setValue(desc);
                                DB.child(key).child("itemQuantity").setValue(qty);
                                DB.child(key).child("itemType").setValue(type);
                                DB.child(key).child("User").setValue(user);

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

    private void uploadImageToFireBase(Uri uri) {

        StorageReference file = storageReference.child(System.currentTimeMillis()+ "." +getFileExtension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DB.child(k).child("itemImage").setValue(uri.toString());

                        Toast.makeText(getContext(), "Successfully added",Toast.LENGTH_SHORT).show();

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
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==Activity.RESULT_OK && data !=null){

            uri = data.getData();
            img.setImageURI(uri);
        }
    }
}