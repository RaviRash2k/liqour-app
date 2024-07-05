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

import android.util.Log;
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
    String type, user, key;
    CheckBox food, liquor;
    DatabaseReference DB;
    StorageReference storageReference;
    Uri uri;
    int PICK_IMAGE_REQUEST;


    String name;
    String price;
    String desc;
    String qty;


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
        if (user == null || user.isEmpty()) {

            Toast.makeText(getContext(), "User email is missing", Toast.LENGTH_SHORT).show();
        }

        storageReference = FirebaseStorage.getInstance().getReference();

        DB = FirebaseDatabase.getInstance().getReference().child("Items");
        key = DB.child("Items").push().getKey();

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

                name = itemName.getText().toString();
                price = itemPrice.getText().toString();
                desc = itemDesc.getText().toString();
                qty = itemQty.getText().toString();

                Integer.parseInt(qty);

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

                if(name.isEmpty() || price.isEmpty() || desc.isEmpty() || qty.isEmpty() || type.equals("invalid")){

                    Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }else{

                    if (uri != null) {
                        StorageReference file = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String imageUrl = uri.toString();
                                        DB.child(key).child("itemImage").setValue(imageUrl);
                                        DB.child(key).child("itemName").setValue(name);
                                        DB.child(key).child("itemPrice").setValue(price);
                                        DB.child(key).child("itemDescription").setValue(desc);
                                        DB.child(key).child("itemQuantity").setValue(qty);
                                        DB.child(key).child("itemType").setValue(type);
                                        DB.child(key).child("User").setValue(user);

                                        Intent i = new Intent(getActivity(), AdminHome.class);
                                        startActivity(i);

                                        Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG, "Error uploading image", e);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return rootView;
    }

    private String getFileExtension(Uri uri) {

        if (uri == null) {
            return null;
        }
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            if (uri != null) {
                img.setImageURI(uri);

            } else {
                Toast.makeText(getContext(), "Error: Image not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}