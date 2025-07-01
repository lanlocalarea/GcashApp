package com.ciicc.carlosgo;

import java.sql.*;

public class UserAuthentication {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    private String name;
    private String email;
    private long number;
    private short pin;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getNumber() {
        return number;
    }

    public short getPin() {
        return pin;
    }

    public void Registration(String name, String email, long number, short pin) {
        if (!isValidName(name) || !isValidEmail(email)) {
            System.out.println("All fields are required. Please complete the form.");
        } else if (!isEmailFormatValid(email)) {
            System.out.println("Invalid email format. Example: janedoe123@gmail.com");
        } else if (!isValidNumber(number)) {
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
        } else if (!isValidPin(pin)) {
            System.out.println("PIN must be exactly 4 digits with no decimal or letters.");
        } else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                String query = "SELECT * FROM users WHERE Number = " + number;
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    System.out.println("This mobile number is already registered.");
                } else {
                    String query1 = "SELECT * FROM users";
                    ResultSet rs1 = statement.executeQuery(query1);
                    rs1.moveToInsertRow();
                    rs1.updateString("Name", name);
                    rs1.updateString("Email", email);
                    rs1.updateLong("Number", number);
                    rs1.updateShort("PIN", pin);
                    rs1.insertRow();
                    rs1.moveToCurrentRow();

                    String query2 = "SELECT ID FROM users WHERE Number = " + number;
                    ResultSet rs2 = statement.executeQuery(query2);
                    if (rs2.next()) {
                        int newUserId = rs2.getInt("ID");

                        String query3 = "SELECT * FROM balance";
                        ResultSet rs3 = statement.executeQuery(query3);
                        rs3.moveToInsertRow();
                        rs3.updateFloat("amount", 0f);
                        rs3.updateInt("user_ID", newUserId);
                        rs3.insertRow();
                        rs3.moveToCurrentRow();

                        System.out.println("Registration successful.");
                    } else {
                        System.out.println("Error retrieving user ID after registration.");
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }


    public int Login(long number, short pin) {
        if (!isValidNumber(number)) {
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
            return 0;
        } else if (!isValidPin(pin)) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");
            return 0;
        } else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                String query = "SELECT * FROM users WHERE Number = " + number + " AND PIN = " + pin;
                ResultSet rs = statement.executeQuery(query);
                if (!rs.next()) {
//                    System.out.println("Login failed. Please check your number and PIN.");
                    return 0;
                } else {
                    this.name = rs.getString("Name");
                    this.email = rs.getString("Email");
                    this.number = rs.getLong("Number");
                    this.pin = rs.getShort("PIN");
                    return rs.getInt("ID");
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
                return 0;
            }
        }
    }

    public void changePin(int userId, short oldPin, short newPin) {
        if (!isValidPin(oldPin)) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");
        } else if (!isValidPin(newPin)) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");
        } else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                String query = "SELECT * FROM users WHERE ID = " + userId;
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    if (rs.getShort("PIN") != oldPin) {
                        System.out.println("The current PIN you entered is incorrect.");
                    } else {
                        rs.updateShort("PIN", newPin);
                        rs.updateRow();
                        this.pin = newPin;
                        System.out.println("Your PIN has been successfully changed.");
                    }
                } else {
                    System.out.println("User account not found. Please register first.");
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public void logout() {
        System.out.println("User " + name + " has successfully logged out.");
        this.name = null;
        this.email = null;
        this.number = 0;
        this.pin = 0;
    }

    static boolean isValidName(String name) {
        return name != null && !name.isEmpty();
    }

    static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }

    static boolean isEmailFormatValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    static boolean isValidNumber(long number) {
        return number >= 9_000_000_000L && number < 10_000_000_000L && !String.valueOf(number).matches(".*[a-zA-Z].*");
    }

    static boolean isValidPin(short pin) {
        String pinStr = String.valueOf(pin);
        return pinStr.matches("\\d{4}");
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
