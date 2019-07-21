package com.example.shoaib.booklistingapp;

public class book {

    private String mAuthor;
    private String mTitle;
    private String mDescribtion;
    private String mImage;

    public book(String mTitle, String mAuthor, String mDescribtion,String mImage) {
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
        this.mDescribtion = mDescribtion;
        this.mImage = mImage;
    }


    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmDescribtion() {
        return mDescribtion;
    }

    public String getmImage() {
        return mImage;
    }
}


