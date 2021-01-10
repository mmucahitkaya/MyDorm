package com.example.mydorm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateAd extends AppCompatActivity {


    //define all attributes
    public Bitmap selectedImageBitmap, selectedImageBitmap2, selectedImageBitmap3;
    public Uri imageData, imageData2, imageData3;
    public ImageView adsPhoto, adsPhoto2, adsPhoto3;
    public EditText adsHead, description, address, price;
    public Button publish;
    public CheckBox friend, furniture;
    String category;


    //define firebase
    public FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    public StorageReference storageReference = firebaseStorage.getReference();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public FirebaseAuth firebaseAuth  = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        getSupportActionBar().hide();
        //define layouts attributes
        adsPhoto = findViewById(R.id.adsPhoto);
        adsHead = findViewById(R.id.adsHead);
        description = findViewById(R.id.description);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);
        adsPhoto2 = findViewById(R.id.selectImage2);
        friend = findViewById(R.id.checkroommate);
        furniture = findViewById(R.id.checkfurniture);
        publish = findViewById(R.id.publish);
        furniture.setEnabled(true);
        friend.setEnabled(true);





        //select category room or furniture
        friend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (friend.isChecked()){
                    category = "Room";
                    furniture.setEnabled(false);
                } else {
                    furniture.setEnabled(true);
                }
            }
        });

        //select category room or furniture
        furniture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (furniture.isChecked()){
                    category = "Furniture";
                    friend.setEnabled(false);
                } else {
                    friend.setEnabled(true);
                }
            }
        });
    }

    //upload image
    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }
    //upload second image
    public void selectImage2(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 3);

        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 4);
        }
    }
    //upload third image ##OPTIONAL##
   /* public void selectImage3(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 5);

        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 6);
        }
    }*/

    @Override   //Access gallery permission request.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK);
                startActivityForResult(intentToGallery, 2);
            }
        }
        if (requestCode == 3){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK);
                startActivityForResult(intentToGallery, 4);
            }
        }

        if (requestCode == 5){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK);
                startActivityForResult(intentToGallery, 6);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImageBitmap = ImageDecoder.decodeBitmap(source);
                    adsPhoto.setImageBitmap(selectedImageBitmap);
                }else{
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    adsPhoto.setImageBitmap(selectedImageBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 4 && resultCode == RESULT_OK && data != null) {

            imageData2 = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source2 = ImageDecoder.createSource(this.getContentResolver(), imageData2);
                    selectedImageBitmap2 = ImageDecoder.decodeBitmap(source2);
                    adsPhoto2.setImageBitmap(selectedImageBitmap2);

                } else {
                    selectedImageBitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData2);
                    adsPhoto2.setImageBitmap(selectedImageBitmap2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == 6 && resultCode == RESULT_OK && data != null) {

            imageData3 = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source3 = ImageDecoder.createSource(this.getContentResolver(), imageData3);
                    selectedImageBitmap3 = ImageDecoder.decodeBitmap(source3);
                    adsPhoto3.setImageBitmap(selectedImageBitmap3);
                } else {
                    selectedImageBitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData3);
                    adsPhoto3.setImageBitmap(selectedImageBitmap3);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //publish ad method and send firebase
    public void publishAd(View view) {
        if (imageData != null){
            UUID uuid = UUID.randomUUID();
            final String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference urlReference = FirebaseStorage.getInstance().getReference(imageName);
                    urlReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();

                            if (imageData2 != null){
                                UUID uuid2 = UUID.randomUUID();
                                final String imageName2 = "images/" + uuid2 + ".jpg";
                                storageReference.child(imageName2).putFile(imageData2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        StorageReference url = FirebaseStorage.getInstance().getReference(imageName2);
                                        url.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String downloadUrl2 = uri.toString();


                                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                String email = firebaseUser.getEmail();

                                                String descriptionTxt = description.getText().toString();
                                                String headerTxt = adsHead.getText().toString();
                                                String addressTxt = address.getText().toString();
                                                String priceTxt = price.getText().toString();


                                                HashMap<String, Object> advertData = new HashMap<>();
                                                advertData.put("price", Integer.parseInt(priceTxt));
                                                advertData.put("advertOwner", email);
                                                advertData.put("header", headerTxt);
                                                advertData.put("address", addressTxt);
                                                advertData.put("description", descriptionTxt);
                                                advertData.put("downloadURL", downloadUrl);
                                                advertData.put("category", category);
                                                advertData.put("downloadURL2", downloadUrl2);
                                                advertData.put("date", FieldValue.serverTimestamp());

                                                firebaseFirestore.collection("Adverts").add(advertData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        startActivity(new Intent(CreateAd.this, Dashboard.class));
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CreateAd.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }else{
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String email = firebaseUser.getEmail();

                                String descriptionTxt = description.getText().toString();
                                String headerTxt = adsHead.getText().toString();
                                String addressTxt = address.getText().toString();
                                String priceTxt = price.getText().toString();


                                HashMap<String, Object> advertData = new HashMap<>();
                                advertData.put("price", Integer.parseInt(priceTxt));
                                advertData.put("advertOwner", email);
                                advertData.put("header", headerTxt);
                                advertData.put("address", addressTxt);
                                advertData.put("description", descriptionTxt);
                                advertData.put("downloadURL", downloadUrl);
                                advertData.put("category", category);

                                advertData.put("date", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Adverts").add(advertData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        startActivity(new Intent(CreateAd.this, Dashboard.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateAd.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateAd.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

}