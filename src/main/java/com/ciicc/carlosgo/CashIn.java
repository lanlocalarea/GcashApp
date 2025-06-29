package com.ciicc.carlosgo;

import java.sql.*;
import java.time.LocalDateTime;

public class CashIn {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    public void cashIn(int id, float amount, long toNumber)
    {
        if (!UserAuthentication.isValidNumber(toNumber)){
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
        } else if (amount <= 0) {
            System.out.println("Amount should not be less than or equal to 0");
        } else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                String query = "SELECT * FROM users WHERE Number = " + toNumber;
                ResultSet rs = statement.executeQuery(query);

                if (rs.next()) {
                    int toID = rs.getInt("ID");
                    String name = rs.getString("Name");
                    String query1 = "SELECT * FROM balance WHERE user_ID = " + toID;
                    ResultSet rs1 = statement.executeQuery(query1);

                    if (rs1.next()) {
                        float oldAmount = rs1.getFloat("amount");
                        rs1.updateFloat("amount", oldAmount + amount);
                        rs1.updateRow();
                        String query2 = "SELECT * FROM transaction";
                        ResultSet rs2 = statement.executeQuery(query2);
                        rs2.moveToInsertRow();
                        rs2.updateFloat("amount", amount);
                        rs2.updateString("name", name);
                        rs2.updateInt("account_ID", id);
                        rs2.updateString("date", String.valueOf(LocalDateTime.now()));
                        rs2.updateInt("transferToID", toID);
                        rs2.updateInt("transferFromID", id);
                        rs2.insertRow();
                        rs2.moveToCurrentRow();
                    } else {
                        System.out.println("Recipient not found");
                    }
                } else {
                    System.out.println("Recipient not found");
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
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
