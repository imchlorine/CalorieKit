package com.maclee.calorie.model;

import java.util.Date;

public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String dob;
    private double height;
    private double weight;
    private Character gender;
    private String address;
    private String postcode;
    private String loa;
    private int spm;
    public User() {
    }

    public User(String firstname, String lastname, String email, String dob, double height, double weight, Character gender, String address, String postcode, String loa, int spm) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.loa = loa;
        this.spm = spm;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLoa() {
        return loa;
    }

    public void setLoa(String loa) {
        this.loa = loa;
    }

    public int getSpm() {
        return spm;
    }

    public void setSpm(int spm) {
        this.spm = spm;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", loa='" + loa + '\'' +
                ", spm=" + spm +
                '}';
    }
}
