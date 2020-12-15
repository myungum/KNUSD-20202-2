package com.example.sd2020.Diary;

public class Article {

    private String id;
    private String familyId;
    private String date;
    private String tag;
    private String uriImage;

    Article() {
        id = "";
        date = "";
        tag = "";
        uriImage="";
    }

    Article(String id, String familyId, String date, String tag, String uriImage) {
        this.id = id;
        this.familyId = familyId;
        this.date = date;
        this.tag = tag;
        this.uriImage = uriImage;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTag() { return tag; }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFamilyId() {
        return familyId;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

}
