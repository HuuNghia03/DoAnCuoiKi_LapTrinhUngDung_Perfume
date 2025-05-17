package com.example.perfume.entity;

import java.io.Serializable;

public class AddressEntity implements Serializable {
    private String nameAddress;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String district;
    private String ward;
    private String detailAddress;
    private boolean isDefault;

    public AddressEntity(String nameAddress, String firstName, String lastName, String phone,
                   String city, String district, String ward, String detailAddress, boolean isDefault) {
        this.nameAddress = nameAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.detailAddress = detailAddress;
        this.isDefault=isDefault;
    }
    public String getNameAddress(){
        return nameAddress;
    }

    public String getCity() {
        return city;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getDistrict() {
        return district;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getWard() {
        return ward;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
