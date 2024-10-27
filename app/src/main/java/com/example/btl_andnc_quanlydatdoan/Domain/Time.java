package com.example.btl_andnc_quanlydatdoan.Domain;

public class Time {
    private int Id;
    private String Value;

    public int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return Value;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public Time() {
    }
}
