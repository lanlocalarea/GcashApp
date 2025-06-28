package com.ciicc.carlosgo;

import java.sql.*;

public class CheckBalance {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    public float checkBalance(int id) {

        float balance = 0;
        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE))
        {
            String query = "SELECT * FROM balance WHERE user_ID = " + id;
            ResultSet rs = statement.executeQuery(query);
            if (rs.next())
                balance = rs.getFloat("amount");
            else
                System.out.println("User not found");
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return balance;
    }

    private static Connection con(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return connection;
    }
}
