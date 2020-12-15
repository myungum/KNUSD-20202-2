package com.example.sd2020.Diary;

public class Article {

    private String id;
    private String date;
    private String title;
    private String write;
    private String userEmail;
    private String uri_image;

    Article() {
        id = "";
        date = "";
        title = "";
        write = "";
        userEmail = "";
        uri_image="";
    }

    Article(String id, String date, String title, String write, String userEmail, String uri_image) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.write = write;
        this.userEmail = userEmail;
        this.uri_image = uri_image;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getwrite() {
        return write;
    }

    public void setwrite(String write) {
        this.write = write;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public void setuserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String geturi_image() {
        return uri_image;
    }

    public void seturi_image(String uri_image) {
        this.uri_image = uri_image;
    }

}
