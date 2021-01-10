package com.example.mydorm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    //define layouts attributes
    public ImageView adsPhoto, adsPhoto2;
    public EditText adsHeadTxt, descriptionTxt, addressTxt, priceTxt,adownermailtxt,adownernametxt;
    public Button updateAdvert, deleteAdvert;

    //define strings and firebase
    String url1, url2, address, head, price, desc,  docID, adownermail,adownername,adownerlastname, adownerallname;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //define layouts attributes
        adsPhoto = findViewById(R.id.adsPhoto);
        adsHeadTxt = findViewById(R.id.adsHead);
        descriptionTxt = findViewById(R.id.description);
        addressTxt = findViewById(R.id.address);
        priceTxt = findViewById(R.id.price);
        adsPhoto2 = findViewById(R.id.selectImage2);
        updateAdvert = findViewById(R.id.updateAdvert);
        updateAdvert.setVisibility(View.VISIBLE);
        deleteAdvert = findViewById(R.id.deleteAdvert);
        deleteAdvert.setVisibility(View.VISIBLE);
        adownermailtxt = findViewById(R.id.ownermail);
        adownernametxt = findViewById(R.id.edittxtadownername);

        //call and define all ads on firebase
        url1 = getIntent().getStringExtra("photo1");
        url2 = getIntent().getStringExtra("photo2");
        address = getIntent().getStringExtra("address");
        head = getIntent().getStringExtra("head");
        price = getIntent().getStringExtra("price");
        desc = getIntent().getStringExtra("desc");
        docID = getIntent().getStringExtra("docId");

        //call photos
        Picasso.get().load(url1).into(adsPhoto);
        Picasso.get().load(url2).into(adsPhoto2);

        //set ad attributes on edittext
        adsHeadTxt.setText(head);
        descriptionTxt.setText(desc);
        priceTxt.setText(price);
        addressTxt.setText(address);

        //check ad owner and call infos
        checkAdvertOwner();
        System.out.println("docid " + docID );

        //update ad
        updateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    database.collection("Adverts").document(docID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            HashMap<String, Object> newAdvertData = new HashMap<>();

                            newAdvertData.put("description", descriptionTxt.getText().toString());
                            newAdvertData.put("header", adsHeadTxt.getText().toString());
                            newAdvertData.put("price", Integer.parseInt(priceTxt.getText().toString()));
                            newAdvertData.put("address", addressTxt.getText().toString());

                            database.collection("Adverts").document(docID).update(newAdvertData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DetailActivity.this, "update Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DetailActivity.this, Dashboard.class));
                                }
                            });
                        }
                    });

            }
        });

        //delete ad buton
        deleteAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, Dashboard.class));
                finish();
                deleteAd();

            }
        });

    }
    //delete ad method
    public void deleteAd(){
        database = FirebaseFirestore.getInstance();
        database.collection("Adverts").document(docID).delete();
    }

    //check ad owner method
    public void checkAdvertOwner(){
        database.collection("Adverts").document(docID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String, Object> data = value.getData();
                String owner = (String) data.get("advertOwner");
                if (owner != null) {
                    if (mAuth.getCurrentUser().getEmail().equals(owner)){
                        updateAdvert.setVisibility(View.VISIBLE);
                        deleteAdvert.setVisibility(View.VISIBLE);
                        adownermailtxt.setEnabled(false);
                        adownernametxt.setEnabled(false);
                    }else{
                        updateAdvert.setVisibility(View.INVISIBLE);
                        deleteAdvert.setVisibility(View.INVISIBLE);
                        adsHeadTxt.setEnabled(false);
                        descriptionTxt.setEnabled(false);
                        addressTxt.setEnabled(false);
                        priceTxt.setEnabled(false);
                        adownermailtxt.setEnabled(false);
                        adownernametxt.setEnabled(false);
                    }
                }else{
                    updateAdvert.setVisibility(View.INVISIBLE);
                    deleteAdvert.setVisibility(View.INVISIBLE);

                }database.collection("users").whereEqualTo("email", owner).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {

                                Map<String, Object> data = snapshot.getData();
                                adownername = (String) data.get("name");
                                adownerlastname = (String) data.get("lastname");
                                adownermail =(String) data.get("email");
                                adownerallname = adownername + " " + adownerlastname;
                                adownermailtxt.setText(adownermail);
                                adownernametxt.setText(adownerallname);
                                System.out.println(adownerallname);
                            }
                        }
                    }
                });
            }
        });
    }
}