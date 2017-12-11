package com.fineapp.jsoncontacts;

public class Form {

    public String id;
    public String name;
    public String email;
    public String address;
    public String gender;
    public String phone;

    public Form(){}

    public Form(String id, String name, String email, String address, String gender, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.phone = phone;
    }
}
