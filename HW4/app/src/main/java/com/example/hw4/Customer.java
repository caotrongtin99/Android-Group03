package com.example.hw4;

public class Customer {
    private String fullName;
    private String phoneNumber;
    private String imageName;

    public  Customer(String fullName,String phoneNumber,String imageName){
        this.fullName=fullName;
        this.phoneNumber=phoneNumber;
        this.imageName = imageName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
