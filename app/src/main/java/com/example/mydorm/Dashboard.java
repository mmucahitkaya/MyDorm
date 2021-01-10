package com.example.mydorm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Flow;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    ArrayList<String> downloadUrl2FB;
    ArrayList<String> advertHeadFromFB;
    ArrayList<Long> advertPrice;
    ArrayList<String> downloadURLFromFB;
    ArrayList<String> addressListFB;
    ArrayList<String> ownerListFB;
    ArrayList<String> categoryarray;
    ArrayList<String> descListFB;
    ArrayList<String> docIDFB;

    FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //advertslist
        advertHeadFromFB = new ArrayList<>();
        advertPrice = new ArrayList<>();
        downloadURLFromFB = new ArrayList<>();
        addressListFB = new ArrayList<>();
        downloadUrl2FB = new ArrayList<>();
        descListFB = new ArrayList<>();
        ownerListFB = new ArrayList<>();
        docIDFB = new ArrayList<>();
        categoryarray = new ArrayList<>();

        //Show ads saved to firebase in dashboard
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedAdapter = new FeedAdapter(this, advertHeadFromFB, downloadURLFromFB, advertPrice,
                addressListFB, downloadUrl2FB,
                descListFB, ownerListFB, docIDFB);
        recyclerView.setAdapter(feedAdapter);

        getDataFromFirebaseDes("date");
        System.out.println(downloadUrl2FB.size());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Menu buton actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Menu Activities
        // When clicked on My Adverts
        if (item.getItemId() == R.id.myAdverts){
            clearArray();
            getMyAdverts();
        }
        // When clicked on Create Ad
        if (item.getItemId() == R.id.toCreateAd){
            Intent i = new Intent(Dashboard.this, CreateAd.class);
            startActivity(i);
        }

        //Clear Filters
        if (item.getItemId() == R.id.clearFilter){
            clearArray();
            getDataFromFirebaseDes("date");
        }

        // Logout
        if (item.getItemId() == R.id.logout){
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, Login.class));
        }

        // When clicked on Profile
        if (item.getItemId() == R.id.profile){
            startActivity(new Intent(Dashboard.this, Profile.class));
        }

        // sort by increasing price
        if (item.getItemId() == R.id.increasePrice){
            clearArray();
            getDataFromFirebaseAsc("price");
        } else if (item.getItemId() == R.id.filterfurniture){ // Filter Furniture
            clearArray();
            getAdvertsCategory("Furniture");
        }else if(item.getItemId() == R.id.filterroom) {  // Filter Room
            clearArray();
            getAdvertsCategory("Room");
        }else if (item.getItemId() == R.id.decreasePrice){ // sort by decreasing price
            clearArray();
            getDataFromFirebaseDes("price");
        }
        return super.onOptionsItemSelected(item);
    }


    // sort by decreasing price
    public void getDataFromFirebaseDes(String orderBy){
        firebaseFirestore.collection("Adverts").orderBy(orderBy, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(Dashboard.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();
                        String docId = snapshot.getId();

                        String head = (String) data.get("header");
                        String downloadUrl = (String) data.get("downloadURL");
                        Long priceTxt = (Long) data.get("price");
                        String address = (String) data.get("address");
                        String url2  = (String) data.get("downloadURL2");
                        String desc = (String) data.get("description");
                        String owner = (String) data.get("advertOwner");



                        docIDFB.add(docId);
                        ownerListFB.add(owner);
                        descListFB.add(desc);
                        advertHeadFromFB.add(head);
                        downloadURLFromFB.add(downloadUrl);
                        advertPrice.add(priceTxt);
                        addressListFB.add(address);
                        downloadUrl2FB.add(url2);

                        feedAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }


    // // sort by increasing price
    public void getDataFromFirebaseAsc(String orderBy){
        firebaseFirestore.collection("Adverts").orderBy(orderBy, Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(Dashboard.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();
                        String docId = snapshot.getId();

                        String head = (String) data.get("header");
                        String downloadUrl = (String) data.get("downloadURL");
                        Long priceTxt = (Long) data.get("price");
                        String address = (String) data.get("address");
                        String url2  = (String) data.get("downloadURL2");
                        String desc = (String) data.get("description");
                        String owner = (String) data.get("advertOwner");

                        docIDFB.add(docId);
                        ownerListFB.add(owner);
                        descListFB.add(desc);
                        advertHeadFromFB.add(head);
                        downloadURLFromFB.add(downloadUrl);
                        advertPrice.add(priceTxt);
                        addressListFB.add(address);
                        downloadUrl2FB.add(url2);

                        feedAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    // Sorting by Category
    public void getAdvertsCategory(String value){

        firebaseFirestore.collection("Adverts").whereEqualTo("category",value).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(Dashboard.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();
                        String docId = snapshot.getId();

                        String head = (String) data.get("header");
                        String downloadUrl = (String) data.get("downloadURL");
                        Long priceTxt = (Long) data.get("price");
                        String address = (String) data.get("address");
                        String url2  = (String) data.get("downloadURL2");
                        String desc = (String) data.get("description");
                        String owner = (String) data.get("advertOwner");
                        String category = (String) data.get("category");

                        docIDFB.add(docId);
                        ownerListFB.add(owner);
                        descListFB.add(desc);
                        advertHeadFromFB.add(head);
                        downloadURLFromFB.add(downloadUrl);
                        advertPrice.add(priceTxt);
                        addressListFB.add(address);
                        downloadUrl2FB.add(url2);
                        categoryarray.add(category);


                        feedAdapter.notifyDataSetChanged();
                        System.out.println("head");
                    }

                }
            }
        });
    }


    // Show My Ad
    public void getMyAdverts(){

        firebaseFirestore.collection("Adverts").whereEqualTo("advertOwner",firebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(Dashboard.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        Map<String, Object> data = snapshot.getData();
                        String docId = snapshot.getId();

                        String head = (String) data.get("header");
                        String downloadUrl = (String) data.get("downloadURL");
                        Long priceTxt = (Long) data.get("price");
                        String address = (String) data.get("address");
                        String url2  = (String) data.get("downloadURL2");
                        String desc = (String) data.get("description");
                        String owner = (String) data.get("advertOwner");

                        docIDFB.add(docId);
                        ownerListFB.add(owner);
                        descListFB.add(desc);
                        advertHeadFromFB.add(head);
                        downloadURLFromFB.add(downloadUrl);
                        advertPrice.add(priceTxt);
                        addressListFB.add(address);
                        downloadUrl2FB.add(url2);


                        feedAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    // clear all arrays
    public void clearArray(){
        advertPrice.clear();
        downloadURLFromFB.clear();
        advertHeadFromFB.clear();
        descListFB.clear();
        downloadUrl2FB.clear();
        addressListFB.clear();
        advertPrice.clear();
        ownerListFB.clear();
        docIDFB.clear();
        categoryarray.clear();
    }

}