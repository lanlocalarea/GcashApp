package com.ciicc.carlosgo;

public class User {
    private final String firstName, lastName;
    private final float balance;
    private final String email, password;

    public User(String firstName, String lastName, float balance, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.email = email;
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public float getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}