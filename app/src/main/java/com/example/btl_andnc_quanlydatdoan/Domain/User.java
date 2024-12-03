package com.example.btl_andnc_quanlydatdoan.Domain;

public class User {
    public String id;
    public String birth;
    public String birthPlace;
    public String name;
    public String phone;
    public String cccd;
    public String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public User(){}
    public User(String id, String birth, String birthPlace, String name, String phone, String cccd, String imageUrl) {
        this.id = id;
        this.birth = birth;
        this.birthPlace = birthPlace;
        this.name = name;
        this.phone = phone;
        this.cccd = cccd;
        this.imageUrl = imageUrl;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCccd() {
        return cccd;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
