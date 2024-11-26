package com.example.btl_andnc_quanlydatdoan.Domain;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Foods implements Serializable {
    private String Description;
    private int Id;
    private double Price;
    private String ImagePath;
    private double Star;
    private int TimeValue;
    private String Title;
    private int numberInCart;

    @NonNull
    @Override
    public String toString() {
        return Title;
    }

    public Foods() {
    }

    public String getDescription() {
        return Description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getPrice() {
        return Price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public double getStar() {
        return Star;
    }

    public int getTimeValue() {
        return TimeValue;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
