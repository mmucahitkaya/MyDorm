package com.example.mydorm;

import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class User {
    private String Name = "";
    private String Lastname ="";
    private String Faculty = "";
    private String Birthday = "";
    private String Gender = "";
    private String Email = "";
    private String IDNumber = "";
    private String MatrNo = "";
    private String PersonelNo = "";
    private String Phonenumber = "";
    private String studentoracademician ="";
    private String pass;




//user class

    public User(String Name, String Lastname, String Gender, String Faculty, String Birthday, String Email, String pass, String IDNumber, String Phonenumber, String MatrNo, String PersonelNo , String studentoracademician){
        this.Name = Name;
        this.Lastname = Lastname;
        this.Faculty = Faculty;
        this.Birthday = Birthday;
        this.Email = Email;
        this.IDNumber = IDNumber;
        this.PersonelNo = PersonelNo;
        this.MatrNo = MatrNo;
        this.Phonenumber = Phonenumber;
        this.Gender = Gender;
        this.studentoracademician = studentoracademician;
        this.pass = pass;
    }


    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public String getFaculty() {
        return Faculty;
    }

    public void setFaculty(String Faculty) {
        this.Faculty = Faculty;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String Phonenumber) {
        this.Phonenumber = Phonenumber;
    }

    public String getMatrNo() {
        return MatrNo;
    }

    public void setMatrNo(String matrNo) {
        MatrNo = matrNo;
    }

    public String getPersonelNo() {
        return PersonelNo;
    }

    public void setPersonelNo(String personelNo) {
        PersonelNo = personelNo;
    }

    public String getPass() {
        return pass;
    }

    public String getStudentoracademician() {
        return studentoracademician;
    }

    public void setStudentoracademician(String studentoracademician) {
        this.studentoracademician = studentoracademician;
    }


    private void setPass(String pass) {
        this.pass = pass;
    }

    public boolean checkEmail(String mail){


        if (mail.contains("tau.edu.tr")==true){
            return true;
        }
        else{
            return false;
        }
    }
    //optional checkidnumber
    public static boolean checkIDNumber(String e) {

        for (int i = 0; i < e.length(); i++) {
            if (e.charAt(0) != '0' && e.length() == 11) {
                return true;
            }
        }
        return false;
    }

}
