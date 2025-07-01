package com.ciicc.carlosgo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transactions {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    private static final String NO_TRANSACTIONS = "No Transaction History Found.";
    private static final String TRANSACTION_HEADER = "========== Transaction History ==========";
    private static final String TRANSACTION_FOOTER = "==========================================";
    private static final String TRANSACTION_DIVIDER = "------------------------------------------";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");


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

    public void viewAll() {
        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            String query = "SELECT * FROM transaction";
            ResultSet result = statement.executeQuery(query);

            System.out.println(TRANSACTION_HEADER);
            while (result.next()) {
                printTransaction(result);
            }
            System.out.println(TRANSACTION_FOOTER);

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void viewUserAll(int userID) {
        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            String query = "SELECT * FROM transaction WHERE account_ID = " + userID;
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                System.out.println(NO_TRANSACTIONS);
                return;
            }

            System.out.println(TRANSACTION_HEADER);
            do {
                printTransaction(result);
            } while (result.next());
            System.out.println(TRANSACTION_FOOTER);

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void viewTransaction(int transactionID) {
        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            String query = "SELECT * FROM transaction WHERE ID = " + transactionID;
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                System.out.println("Transaction not found.");
                return;
            }

            System.out.println(TRANSACTION_HEADER);
            printTransaction(result);
            System.out.println(TRANSACTION_FOOTER);

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private void printTransaction(ResultSet result) throws SQLException {
        System.out.println(TRANSACTION_DIVIDER);
        System.out.println("Transaction ID   : " + result.getInt("ID"));
        System.out.printf("Amount           : ₱%,.2f%n", result.getFloat("amount"));
        System.out.println("Name             : " + result.getString("name"));
        System.out.println("Account ID       : " + result.getInt("account_ID"));

        String rawDate = result.getString("date");
        LocalDateTime dateTime = LocalDateTime.parse(rawDate);
        System.out.println("Date             : " + dateTime.format(DATE_FORMATTER));

        System.out.println("Transfer To ID   : " + result.getInt("transferToID"));
        System.out.println("Transfer From ID : " + result.getInt("transferFromID"));
    }
}
