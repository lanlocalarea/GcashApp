package com.ciicc.carlosgo;

public class User {
    private String firstName, lastName;
    private float balance;
    private String email, password;

    public User(String firstName, String lastName, float balance, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {

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