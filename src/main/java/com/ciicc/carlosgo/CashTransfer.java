package com.ciicc.carlosgo;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class CashTransfer {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    private static Connection con() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return connection;
    }

    public void cashTransfer(int id, float amount, long toNumber) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        if (!UserAuthentication.isValidNumber(toNumber)) {
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount should not be less than or equal to 0.");
            return;
        }

        try (Connection con = con();
             Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            String query = "SELECT * FROM users WHERE id = " + id;
            ResultSet rs = statement.executeQuery(query);
            if (!rs.next()) {
                System.out.println("Sender account not found.");
                return;
            }

            String fromName = rs.getString("Name");

            String query1 = "SELECT * FROM users WHERE Number = " + toNumber;
            ResultSet rs1 = statement.executeQuery(query1);
            if (!rs1.next()) {
                System.out.println("Recipient account not found.");
                return;
            }

            int toID = rs1.getInt("ID");
            String toName = rs1.getString("Name");

            String query2 = "SELECT * FROM balance WHERE user_ID = " + id;
            ResultSet rs2 = statement.executeQuery(query2);
            if (!rs2.next()) {
                System.out.println("Sender balance record not found.");
                return;
            }

            float senderBalance = rs2.getFloat("amount");
            if (senderBalance < amount) {
                System.out.println("Insufficient balance.");
                return;
            }

            rs2.updateFloat("amount", senderBalance - amount);
            rs2.updateRow();

            String query3 = "SELECT * FROM balance WHERE user_ID = " + toID;
            ResultSet rs3 = statement.executeQuery(query3);
            if (!rs3.next()) {
                System.out.println("Recipient balance record not found.");
                return;
            }

            float recipientBalance = rs3.getFloat("amount");
            rs3.updateFloat("amount", recipientBalance + amount);
            rs3.updateRow();

            String query4 = "SELECT * FROM transaction";
            ResultSet rs4 = statement.executeQuery(query4);
            rs4.moveToInsertRow();
            rs4.updateFloat("amount", amount);
            rs4.updateString("name", fromName);
            rs4.updateInt("account_ID", id);
            rs4.updateString("date", String.valueOf(LocalDateTime.now()));
            rs4.updateInt("transferToID", toID);
            rs4.updateInt("transferFromID", id);
            rs4.insertRow();
            rs4.moveToCurrentRow();

            System.out.println("\n========== Transfer Successful ==========");
            System.out.println("Sender         : " + fromName);
            System.out.println("Recipient      : " + toName);
            System.out.println("Amount         : ₱" + df.format(amount));
            System.out.println("=========================================");

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
