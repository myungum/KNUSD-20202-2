package com.example.sd2020;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String password;
    private String name;
    private String phone;
    private String email;
    private String id;
    private String family;
    private String level;
    public User(){
        password="";
        name="";
        phone="";
        email="";
        id="";
        family="NULL";
        level="NULL";
    }
    public User(String mypassword, String myname, String myphone, String myemail, String myid){
        this.password=mypassword;
        this.phone=myphone;
        this.name=myname;
        this.email=myemail;
        this.id=myid;
        this.family="NULL";
        this.level="NULL";
    }
    public String getId() {
        return id;
    }
    public String getLevel(){return level;}
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phone;
    }
    public String getFamily(){return family;}
    public void setLevel(String level){this.level=level;}
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setFamily(String family){this.family=family;}

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("password", password);
        result.put("name", name);
        result.put("phone", phone);
        result.put("email", email);
        result.put("id", id);
        result.put("family",family);
        result.put("level",level);
        return result;
    }
}
