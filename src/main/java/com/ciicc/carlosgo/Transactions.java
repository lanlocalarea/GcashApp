package com.ciicc.carlosgo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transactions {

    private static final String DB_URL = "jdbc:mysql://localhost/gcashdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private static final String NO_TRANSACTIONS = "No Transaction History Found.";
    private static final String TRANSACTION_HEADER = "========== Transaction History ==========";
    private static final String TRANSACTION_FOOTER = "==========================================";
    private static final String TRANSACTION_DIVIDER = "------------------------------------------";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");

    private static Connection con() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return connection;
    }

    public void viewAll() {
        String query = "SELECT * FROM transaction";
        displayTransactions(query, "No transaction records found.");
    }

    public void viewUserAll(int userID) {
        String query = "SELECT * FROM transaction WHERE account_ID = " + userID;
        displayTransactions(query, NO_TRANSACTIONS);
    }

    public void viewTransaction(int transactionID) {
        String query = "SELECT * FROM transaction WHERE ID = " + transactionID;

        try (Connection con = con();
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                System.out.println("Transaction not found.");
                return;
            }

            System.out.println(TRANSACTION_HEADER);
            printTransaction(rs);
            System.out.println(TRANSACTION_FOOTER);

        } catch (SQLException e) {
            System.out.println("Error fetching transaction: " + e.getMessage());
        }
    }

    private void displayTransactions(String query, String emptyMessage) {
        try (Connection con = con();
             Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                System.out.println(emptyMessage);
                return;
            }

            System.out.println(TRANSACTION_HEADER);
            do {
                printTransaction(rs);
            } while (rs.next());
            System.out.println(TRANSACTION_FOOTER);

        } catch (SQLException e) {
            System.out.println("Error displaying transactions: " + e.getMessage());
        }
    }

    private void printTransaction(ResultSet rs) throws SQLException {
        System.out.println(TRANSACTION_DIVIDER);
        System.out.println("Transaction ID   : " + rs.getInt("ID"));
        System.out.printf("Amount           : ₱%,.2f%n", rs.getFloat("amount"));
        System.out.println("Name             : " + rs.getString("name"));
        System.out.println("Account ID       : " + rs.getInt("account_ID"));

        String rawDate = rs.getString("date");
        LocalDateTime dateTime = LocalDateTime.parse(rawDate);
        System.out.println("Date             : " + dateTime.format(DATE_FORMATTER));

        System.out.println("Transfer To ID   : " + rs.getInt("transferToID"));
        System.out.println("Transfer From ID : " + rs.getInt("transferFromID"));
    }
}
