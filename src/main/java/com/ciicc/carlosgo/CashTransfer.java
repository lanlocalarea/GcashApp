package com.ciicc.carlosgo;

import java.sql.*;
import java.time.LocalDateTime;

public class CashTransfer {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

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

    public void cashTransfer(int id, float amount, long toNumber)
    {
        if (!UserAuthentication.isValidNumber(toNumber)){
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
        } else if (amount <= 0) {
            System.out.println("Amount should not be less than or equal to 0");
        } else {
            String message;

            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                String query = "SELECT * FROM users WHERE id = " + id;
                ResultSet rs = statement.executeQuery(query);
                if (!rs.next()) {
                    message = "Recipient not found";
                    return;
                }

                String fromName = rs.getString("Name");

                String query1 = "SELECT * FROM users WHERE Number = " + toNumber;
                ResultSet rs1 = statement.executeQuery(query1);
                if (!rs1.next()) {
                    message = "User not found";
                    return;
                }

                int toID = rs1.getInt("ID");

                String query2 = "SELECT * FROM balance WHERE user_ID = " + id;
                ResultSet rs2 = statement.executeQuery(query2);
                if (!rs2.next()) {
                    message = "User not found";
                    return;
                }

                float oldFromBalance = rs2.getFloat("amount");
                if (oldFromBalance <= amount) {
                    message = "Insufficient Balance";
                    return;
                }

                rs2.updateFloat("amount", oldFromBalance - amount);
                rs2.updateRow();

                String query3 = "SELECT * FROM balance WHERE user_ID = " + toID;
                ResultSet rs3 = statement.executeQuery(query3);
                if (!rs3.next()) {
                    message = "Recipient not found";
                    return;
                }

                float oldToBalance = rs3.getFloat("amount");
                rs3.updateFloat("amount", oldToBalance + amount);
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

                message = "Transfer successful. Amount: " + amount;

            } catch (SQLException e) {
                message = e.getLocalizedMessage();
            }

            if (message != null) {
                System.out.println(message);
            }


        }
    }
}
