package com.example.mydorm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    //define firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = fAuth.getCurrentUser();

    Button updateProfile, submit;
    EditText emailTxt, facultyTxt, birthdayTxt, idTxt, genderTxt, lnameTxt, nameTxt, phoneNumTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //define layouts attributes
        submit = findViewById(R.id.submit);
        updateProfile = findViewById(R.id.update);
        updateProfile.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);

        emailTxt = findViewById(R.id.email);
        facultyTxt = findViewById(R.id.faculty);
        lnameTxt = findViewById(R.id.lname);
        phoneNumTxt = findViewById(R.id.phoneNum);
        birthdayTxt = findViewById(R.id.birthday);
        idTxt = findViewById(R.id.id);
        genderTxt = findViewById(R.id.gender);
        nameTxt = findViewById(R.id.name);

        //update profile buton
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTxt.setText("");
                birthdayTxt.setText("");
                lnameTxt.setText("");
                emailTxt.setText("");
                genderTxt.setText("");
                phoneNumTxt.setText("");
                idTxt.setText("");
                facultyTxt.setText("");

                submit.setVisibility(View.VISIBLE);
                updateProfile.setVisibility(View.INVISIBLE);
            }
        });
        //update profile
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
                updateProfile.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);

                getUserDataFB();
            }
        });

        getUserDataFB();
    }
    //get user data from firebase method
    public void getUserDataFB(){
        database.collection("users").document(currentUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(Profile.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    Map<String, Object> data = value.getData();

                    String lastname = (String) data.get("lastname");
                    String name = (String) data.get("name");
                    String birtday = (String) data.get("birtday");
                    String email = (String) data.get("email");
                    String faculty = (String) data.get("faculty");
                    String gender = (String) data.get("gender");
                    String phoneNum = (String) data.get("phonenumber");
                    String idNumber = (String) data.get("idnumber");

                    nameTxt.setText(name);
                    birthdayTxt.setText(birtday);
                    lnameTxt.setText(lastname);
                    emailTxt.setText(email);
                    genderTxt.setText(gender);
                    phoneNumTxt.setText(phoneNum);
                    idTxt.setText(idNumber);
                    facultyTxt.setText(faculty);


                }
            }
        });
    }
    //update user data method
    public void updateUserData(){

        String newName = nameTxt.getText().toString();
        String newLname = lnameTxt.getText().toString();
        String newBirthday = birthdayTxt.getText().toString();
        String newGender = genderTxt.getText().toString();
        String newPhoneNum = phoneNumTxt.getText().toString();
        String newid = idTxt.getText().toString();
        String newfaculty = facultyTxt.getText().toString();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", newName);
        userData.put("birthday", newBirthday);
        userData.put("phonenumber", newPhoneNum);
        userData.put("idnumber", newid);
        userData.put("lastname", newLname);
        userData.put("gender", newGender);
        userData.put("faculty", newfaculty);


        database.collection("users").document(currentUser.getUid()).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Profile.this, "Profiliniz başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}