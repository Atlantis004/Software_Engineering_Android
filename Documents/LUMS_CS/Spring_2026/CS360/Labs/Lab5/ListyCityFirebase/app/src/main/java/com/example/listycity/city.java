package com.example.listycity;

public class city {
    private String name;
    private String province;

    // Required empty constructor for Firestore
    public city() {
    }

    public city(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    // setters for editing
    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}