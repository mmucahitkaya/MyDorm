package com.example.mydorm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Clock;


public class SignUp extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button callLogin;
    Button callSignUp;
    CheckBox academician, student;
    String category;
    String tempperson;
    String tempstudent;
    String empty = "";
    String gender ="";
    FirebaseFirestore database;
    TextInputLayout regFName, regLName, regidnumber, regEmail, regPassword, regmatrno, regpersonno;
    RadioButton male, female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        database = FirebaseFirestore.getInstance();

        regFName = findViewById(R.id.input_fname);
        regLName = findViewById(R.id.lastname);
        regidnumber = findViewById(R.id.idnumber);
        regmatrno = findViewById(R.id.matrno);
        regpersonno = findViewById(R.id.personelnumber);
        regEmail = findViewById(R.id.input_mail);
        regPassword = findViewById(R.id.input_password);
        student = findViewById(R.id.checkstudent);
        academician = findViewById(R.id.checkacademician);
        female = findViewById(R.id.genderfemale);
        male = findViewById(R.id.gendermale);
        callLogin = findViewById(R.id.btn_loginPage);
        callSignUp = findViewById(R.id.btn_signup);

        fAuth = FirebaseAuth.getInstance();


        student.setEnabled(true);
        academician.setEnabled(true);
        regpersonno.setEnabled(false);
        regmatrno.setEnabled(false);

        //check student or academician
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (student.isChecked()){
                    category = "Student";
                    regmatrno.setEnabled(true);
                    regpersonno.setEnabled(false);
                    academician.setEnabled(false);
                } else if(student.isChecked() != true) {
                    regmatrno.setEnabled(false);
                    academician.setEnabled(true);
                    regpersonno.setEnabled(false);
                }
            }
        });
        academician.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (academician.isChecked()){
                    category = "Academician";
                    regpersonno.setEnabled(true);
                    regmatrno.setEnabled(false);
                    student.setEnabled(false);
                } else if(academician.isChecked() != true) {
                    regpersonno.setEnabled(false);
                    student.setEnabled(true);
                    regmatrno.setEnabled(false);
                }
            }
        });


        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        //SingUp buton and check infos
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = regFName.getEditText().getText().toString().trim();
                String lname = regLName.getEditText().getText().toString().trim();
                String id = regidnumber.getEditText().getText().toString().trim();
                String mail = regEmail.getEditText().getText().toString().trim();
                String pass = regPassword.getEditText().getText().toString().trim();
                if(academician.isChecked()){
                    String temppersonno = regpersonno.getEditText().getText().toString().trim();
                    tempperson = temppersonno ;
                }else{
                    tempperson = empty;
                }
                if(student.isChecked()){
                    String tempmatrno = regmatrno.getEditText().getText().toString().trim();
                    tempstudent = tempmatrno ;
                }else{
                    tempstudent = empty;
                }if(female.isChecked()){
                    gender = "Female";
                }else if(male.isChecked()){
                    gender = "Male";
                }


                final User user = new User(fname, lname, gender, empty, empty, mail, empty, id, empty,tempstudent,tempperson,category);
                //if user use tau.edu.tr mail, signup is okay
                if(user.checkEmail(mail)==true){
                    fAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String uid = fAuth.getUid();
                                database.collection("users")
                                        .document(uid)
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(SignUp.this, Dashboard.class));
                                        }else{
                                            Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Toast.makeText(SignUp.this,"User Created!",Toast.LENGTH_SHORT).show(); //kayıt başarılıysa user created mesajı
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));

                            }
                        }
                    });
                }else if(user.checkEmail(mail)==false){//invalid mail
                    Toast.makeText(SignUp.this,"Invalid Mail!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}