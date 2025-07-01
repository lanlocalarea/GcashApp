package com.ciicc.carlosgo;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class CashIn {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    //Cash In deducts from the number inputted and add to itself.

    public void cashIn(int id, float amount, long fromNumber) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        if (amount <= 0) {
            System.out.println("Amount should not be less than or equal to 0.");
            return;
        }

        if (!UserAuthentication.isValidNumber(fromNumber)) {
            System.out.println("Invalid sender mobile number. Use 10-digit format like 9213456789.");
            return;
        }

        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            String query = "SELECT * FROM users WHERE id = " + id;
            ResultSet rs = statement.executeQuery(query);
            if (!rs.next()) {
                System.out.println("Recipient user not found.");
                return;
            }

            String toName = rs.getString("Name");

            String query1 = "SELECT * FROM users WHERE Number = " + fromNumber;
            ResultSet rs1 = statement.executeQuery(query1);
            if (!rs1.next()) {
                System.out.println("Sender user not found.");
                return;
            }

            int fromID = rs1.getInt("ID");

            String query2 = "SELECT * FROM balance WHERE user_ID = " + fromID;
            ResultSet rs2 = statement.executeQuery(query2);
            if (!rs2.next()) {
                System.out.println("Sender balance record not found.");
                return;
            }

            float fromBalance = rs2.getFloat("amount");
            if (fromBalance < amount) {
                System.out.println("Sender has insufficient balance.");
                return;
            }

            rs2.updateFloat("amount", fromBalance - amount);
            rs2.updateRow();

            String query3 = "SELECT * FROM balance WHERE user_ID = " + id;
            ResultSet rs3 = statement.executeQuery(query3);
            if (!rs3.next()) {
                System.out.println("Recipient balance record not found.");
                return;
            }

            float toBalance = rs3.getFloat("amount");
            rs3.updateFloat("amount", toBalance + amount);
            rs3.updateRow();

            String query4 = "SELECT * FROM transaction";
            ResultSet rs4 = statement.executeQuery(query4);
            rs4.moveToInsertRow();
            rs4.updateFloat("amount", amount);
            rs4.updateString("name", toName);
            rs4.updateInt("account_ID", id);
            rs4.updateString("date", String.valueOf(LocalDateTime.now()));
            rs4.updateInt("transferToID", id);
            rs4.updateInt("transferFromID", fromID);
            rs4.insertRow();
            rs4.moveToCurrentRow();

            System.out.println("\n========== Cash-In Successful ==========");
            System.out.println("From Number  : " + fromNumber);
            System.out.println("To           : " + toName);
            System.out.println("Amount       : ₱" + df.format(amount));
            System.out.println("========================================");

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static Connection con() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return connection;
    }
}
