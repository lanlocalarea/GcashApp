package com.ciicc.carlosgo;
import java.sql.*;

public class UserAuthentication {
    private static final String dbUrl = "jdbc:mysql://localhost/gcashdb";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    private int id;
    private String name;
    private String email;
    private long number;
    private short pin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public short getPin() {
        return pin;
    }

    public void setPin(short pin) {
        this.pin = pin;
    }

    public void Registration(String name, String email, long number, short pin) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            System.out.println("All fields are required. Please complete the form.");
        }else if(!email.contains("@") || !email.contains(".")){
            System.out.println("Invalid email format. Example: janedoe123@gmail.com");
        } else if (number < 9_000_000_000L || number > 10_000_000_000L || String.valueOf(number).matches(".*[a-zA-Z].*")) {
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");
        } else if (String.valueOf(pin).matches(".*[a-zA-Z].*")) {
            System.out.println("PIN must contain only numbers.");
        } else if (!String.valueOf(pin).matches("\\d{4}")) {
            System.out.println("PIN must be exactly 4 digits with no decimal or letters.");
        } else {
            try (Connection connection = con();
                 Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE))
            {
                String query = "select * from users where Number = " + number;
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()){
                    System.out.println("This mobile number is already registered.");
                } else {
                    String query1 = "select * from users";
                    ResultSet rs1 = statement.executeQuery(query1);
                    rs1.moveToInsertRow();
                    rs1.updateString("Name", name);
                    rs1.updateString("Email", email);
                    rs1.updateLong("Number", number);
                    rs1.updateShort("PIN", pin);
                    rs1.insertRow();
                    rs1.moveToCurrentRow();
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public int Login(long number, short pin) {
        int id = 0;

        if (number < 9_000_000_000L || number > 10_000_000_000L || String.valueOf(number).matches(".*[a-zA-Z].*")) {
            System.out.println("Invalid mobile number. Use 10-digit format like 9213456789.");}
        else if (String.valueOf(pin).matches(".*[a-zA-Z].*") || !String.valueOf(pin).matches("\\d{4}") || pin < 0) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");}
        else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE))
            {
                String query = "SELECT * FROM users WHERE Number = " + number + " AND PIN = " + pin;
                ResultSet rs = statement.executeQuery(query);
                if (!rs.next())
                    System.out.println("Login failed. Please check your number and PIN.");
                else {
                    setId(id = rs.getInt("ID"));
                    setName(rs.getString("Name"));
                    setEmail(rs.getString("Email"));
                    setNumber(rs.getLong("Number"));
                    setPin(rs.getShort("PIN"));
                }

            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return id;
    }

    public void changePin(short oldPin, short newPin){
        if (String.valueOf(oldPin).matches(".*[a-zA-Z].*") || !String.valueOf(oldPin).matches("\\d{4}") || oldPin < 0) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");
        }
        else if (String.valueOf(newPin).matches(".*[a-zA-Z].*") || !String.valueOf(newPin).matches("\\d{4}") || newPin < 0) {
            System.out.println("Invalid PIN. It must be exactly 4 numeric digits.");
        }
        else {
            try (Connection con = con();
                 Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                String query = "SELECT * FROM users WHERE ID = " + getId();
                ResultSet rs = statement.executeQuery(query);
                if (rs.next()) {
                    if (rs.getShort("PIN") != oldPin){
                        System.out.println("The current PIN you entered is incorrect.");
                    } else {
                        rs.updateShort("PIN", newPin);
                        rs.updateRow();
                        setPin(newPin);
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

    public void logout(){
        System.out.println("User " + getName() + " has successfully logged out.");
        setId(0);
        setName(null);
        setEmail(null);
        setNumber(0);
        setPin((short)0);
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

