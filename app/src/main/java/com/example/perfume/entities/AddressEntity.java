package com.example.perfume.entities;

import java.io.Serializable;

public class AddressEntity implements Serializable {
    public String nameAddress;
    public String firstName;
    public String lastName;
    public String phone;
    public String city;
    public String district;
    public String ward;
    public String detailAddress;

    public AddressEntity(String nameAddress, String firstName, String lastName, String phone,
                   String city, String district, String ward, String detailAddress) {
        this.nameAddress = nameAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.detailAddress = detailAddress;
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
}
