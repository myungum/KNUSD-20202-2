package com.example.sd2020;

public class Parent {
    String name;
    String id;
    String pn;
    public Parent()
    {
        name="NULL";
        id="NULL";
        pn="NULL";
    }

    public Parent(String name, String id, String pn)
    {
        this.name=name;
        this.id=id;
        this.pn=pn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPn() {
        return pn;
    }

    public String getName() {
        return name;
    }
}
