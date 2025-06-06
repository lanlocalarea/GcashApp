package com.ciicc.carlosgo;

import java.sql.*;
import java.util.Scanner;

public class Gcash {
    private static String dbUrl = "jdbc:mysql://localhost/dbgcash";
    private static String dbUsername = "root";
    private static String dbPassword = "";
    private static String dbSql = "SELECT * FROM userInfo WHERE firstName = 'Carlos' AND lastName = 'Go'";
    private static User user;
    private static String email, password;
    private static Scanner input = new Scanner(System.in);

    public static Connection con(){
        Connection connection = null;
        try {
             connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

    public static void getLogin(String email, String password){
        try {
            String loginQuery = "SELECT * FROM userInfo WHERE email = '" + email + "' AND password = '" + password + "'";
            Statement statement = con().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(loginQuery);
            queryResult.next();
            user = new User(
                    queryResult.getString(1),
                    queryResult.getString(2),
                    queryResult.getFloat(3),
                    queryResult.getString(4),
                    queryResult.getString(5));
            System.out.println("Login Success!");
            con().close();

        }catch (SQLException e){
            System.out.println("Wrong username or password.");
        }
    }

    public static void main(String[] args) {
//        Gcash.con();
        System.out.println("Login: ");
        System.out.print("Enter your Email: ");
        email = input.next();
        System.out.print("Enter your Password: ");
        password = input.next();
        getLogin(email,password);
        try {
            System.out.println("Welcome " + user.getFirstName() + "!");
        } catch (NullPointerException e) {
            System.exit(0);
        }

    }

}
