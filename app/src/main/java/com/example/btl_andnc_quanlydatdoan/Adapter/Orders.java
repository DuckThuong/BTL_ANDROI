package com.example.btl_andnc_quanlydatdoan.Adapter;

import com.example.btl_andnc_quanlydatdoan.Domain.Foods;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Orders {
    private String id;
    private Date date;
    private double totalPrice;
    ArrayList<Foods> listFood;

    public Orders() {
    }

    public Orders(String id, Date date, double totalPrice, ArrayList<Foods> listFood) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.listFood = listFood;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Foods> getListFood() {
        return listFood;
    }

    public void setListFood(ArrayList<Foods> listFood) {
        this.listFood = listFood;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
