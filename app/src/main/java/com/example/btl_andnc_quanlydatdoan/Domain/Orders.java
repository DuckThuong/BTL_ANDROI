package com.example.btl_andnc_quanlydatdoan.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Orders implements Serializable {
    private String id;
    private double totalPrice;
    ArrayList<Foods> listFood;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Orders() {
    }

    public Orders(String id, int quantity, double totalPrice, ArrayList<Foods> listFood) {
        this.id = id;
        this.quantity = quantity;
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

}
