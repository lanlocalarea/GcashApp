package com.ciicc.carlosgo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Gcash {
    private static final String dbUrl = "jdbc:mysql://localhost/dbgcash";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";
    private static User user;
    private static String email, password;
    private static final Scanner input = new Scanner(System.in);

    public static Connection con(){
        Connection connection = null;
        try {
             connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return connection;
    }
    //Login
    public static void userAuthentication(String email, String password){
        try (Connection connection = con())
        {
            String loginQuery = "SELECT * FROM userInfo WHERE email = '" + email + "' AND password = '" + password + "'";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(loginQuery);
            queryResult.next();
            user = new User(
                    queryResult.getString(1),
                    queryResult.getString(2),
                    queryResult.getFloat(3),
                    queryResult.getString(4),
                    queryResult.getString(5));
            System.out.println("Login Success!");

        }catch (SQLException e){
            System.out.println("Wrong username or password.");
        }
    }
    //Cash in
    public static void cashIn(float amount){
        try (Connection connection = con())
        {
            String getInfoQuery = "SELECT * FROM userInfo WHERE email = '" + user.getEmail() + "' AND password = '" + user.getPassword() + "'";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(getInfoQuery);
            queryResult.absolute(1);
            queryResult.updateFloat(3,(user.getBalance() + amount));
            queryResult.updateRow();
            connection.close();
            addTransaction(user.getEmail(), "Cash In", user.getEmail(), amount, LocalDateTime.now());
            System.out.println("----------------------------------------");
            System.out.println("Php " + String.format("%,.2f", amount) + " has been added to your balance");
            refreshInfo();
            System.out.println("Your new balance is: Php " + String.format("%,.2f", user.getBalance()));
            System.out.println("----------------------------------------");

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    //Cash Transfer
    public static void cashTransfer(String receiverEmail, float amount){
            Connection thisConnection = con();
        try {
            thisConnection.setAutoCommit(false);
            // Sender Side
            String getSenderQuery = "SELECT * FROM userInfo WHERE email = '" + user.getEmail() + "' AND password = '" + user.getPassword() + "'";
            Statement statement = thisConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(getSenderQuery);
            queryResult.absolute(1);
            if (user.getBalance() < amount) {
                System.out.println("----------------------------------------");
                System.out.println("Insufficient balance.");
                System.out.println("----------------------------------------");
                thisConnection.rollback();
                return;
            }

            queryResult.updateFloat(3, (user.getBalance() - amount));
            queryResult.updateRow();

            // Receiver Side
            String getReceiverQuery = "SELECT * FROM userInfo WHERE email = '" + receiverEmail + "'";
            Statement statementReceiver = thisConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResultReceiver = statementReceiver.executeQuery(getReceiverQuery);
            if (!queryResultReceiver.first()) {
                System.out.println("----------------------------------------");
                System.out.println("Receiver not found.");
                System.out.println("----------------------------------------");
                thisConnection.rollback();
                return;
            }
            float receiverBalance = queryResultReceiver.getFloat(3);
            queryResultReceiver.updateFloat(3, (receiverBalance + amount));
            queryResultReceiver.updateRow();
            addTransaction(user.getEmail(), "Sent", receiverEmail, amount, LocalDateTime.now());
            addTransaction(receiverEmail, "Received",  user.getEmail(), amount, LocalDateTime.now());
            thisConnection.commit();
            System.out.println("----------------------------------------");
            System.out.println("Php " + String.format("%,.2f", amount) + " has been transferred to " + receiverEmail);
            refreshInfo();
            System.out.println("Your new balance is: Php " + String.format("%,.2f", user.getBalance()));
            System.out.println("----------------------------------------");
            returnMenu();


        } catch (SQLException e) {
            try {
                if (thisConnection != null) thisConnection.rollback();
                System.out.println("----------------------------------------");
                System.out.println("Transfer failed.");
                System.out.println("----------------------------------------");
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback error: " + rollbackEx.getMessage());
            }
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (thisConnection != null) thisConnection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }

    }

    // Create Transaction
    public static void addTransaction(String email, String transactionType, String recipient, float amount, LocalDateTime date){
        Connection connection;
        try {
            connection = con();
            String TransactionQuery = "SELECT * FROM transactions WHERE email = '" + user.getEmail() + "'";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(TransactionQuery);
            queryResult.moveToInsertRow();
            queryResult.updateString(2,email);
            queryResult.updateString(3,transactionType);
            queryResult.updateString(4, recipient);
            queryResult.updateFloat(5,amount);
            queryResult.updateString(6, String.valueOf(date));
            queryResult.insertRow();
            queryResult.moveToCurrentRow();
            connection.close();
        }catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            }
    }

    //Create List of Transactions object
    public static List<Transaction> getTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>();
        String email = user.getEmail();
        String query = "SELECT * FROM transactions WHERE email = ? ORDER BY date DESC LIMIT " + count;


        try (Connection connection = con()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String transactionType = rs.getString("transactionType");
                String recipient = rs.getString("recipient");
                float amount = rs.getFloat("amount");
                LocalDateTime date = LocalDateTime.parse(rs.getString("date"));
                transactions.add(new Transaction(email, transactionType, recipient, amount, date));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }

        return transactions;
    }

    //Display Transactions
    public static void showTransactions(int count) {
        List<Transaction> transactions = getTransactions(count);

        System.out.printf("%-12s %-20s %-25s %-15s %-20s\n", "Type", "To/From", "Email", "Amount", "Date");
        for (Transaction transaction : transactions) {
            String contact = transaction.getTransactionType().equalsIgnoreCase("Received") ? "From" : "To";
            System.out.printf("%-12s %-20s %-25s %,-15.2f %-20s\n",
                    transaction.getTransactionType(),
                    contact,
                    transaction.getRecipient(),
                    transaction.getAmount(),
                    transaction.getDate().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy | hh:mm a"))
            );
        }


        System.out.println("---------------------------------------------------");
    }




    public static void main(String[] args) {
//        Gcash.con();
        System.out.println("Login: ");
        System.out.print("Enter your Email: ");
        email = input.next();
        System.out.print("Enter your Password: ");
        password = input.next();
        userAuthentication(email,password);
        try{
            showMenu();
        } catch (NullPointerException e) {
            System.out.println("Do you want to exit?");
            System.out.println("1: Return to login");
            System.out.println("2: Exit");
            System.out.print("Input: ");
            int option = input.nextInt();
            switch (option) {
                case 1: main(new String[]{});
                    break;
                case 2: {
                    input.close();
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("Invalid Option");
                    System.out.println("Exiting...");
                    input.close();
                    System.exit(0);
                }
            }
        }
    }

    //Show Menu
    public static void showMenu(){
        System.out.println("\n=================================================");
        System.out.println(" Welcome " + user.getFirstName() + "!");
        System.out.println("=================================================");
        System.out.println("What would you want to do today?");
        System.out.println("1: Check Balance");
        System.out.println("2: Cash in");
        System.out.println("3: Cash Transfer");
        System.out.println("4: Show Transactions History");
        System.out.println("5: Exit");
        System.out.print("Input: ");
        int menuChoice = 0;
        try {
            if (input.hasNextInt()) {
                menuChoice = input.nextInt();
                input.nextLine();
            } else {
                input.next();
                System.out.println("Invalid input. Please enter a number.");
                returnMenu();

            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            input.next();
            returnMenu();

        }

        switch (menuChoice) {
            case 1: {
                System.out.println("----------------------------------------");
                System.out.println("Your Balance is: Php " + String.format("%,.2f", user.getBalance()));
                System.out.println("----------------------------------------");
                returnMenu();
                break;
            }
            case 2: {
                System.out.print("Cash in amount: ");
                float amount;
                try {
                    amount = input.nextFloat();
                    if (amount < 0){
                        System.out.println("Invalid amount");
                        returnMenu();
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid amount");
                    input.nextLine();
                    returnMenu();
                    return;
                }
                cashIn(amount);
                returnMenu();
                break;
            }
            case 3: {
                System.out.print("Receiver Email: ");
                String receiverEmail = input.next();
                System.out.print("Amount to send: ");
                float amount = 0;
                try {
                    amount = input.nextFloat();
                    if (amount < 0 || amount == 0){
                        System.out.println("Invalid amount");
                        returnMenu();
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid amount");
                    returnMenu();
                }
                cashTransfer(receiverEmail, amount);
                break;
            }
            case 4: {
                System.out.println("How many transactions you want to display? ");
                System.out.print("Input: ");
                int count = 0;

                try {
                    count = input.nextInt();
                    input.nextLine();
                    if (count < 0 || count == 0){
                        System.out.println("Invalid amount");
                        returnMenu();
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid amount");
                    input.nextLine();
                    returnMenu();
                }
                showTransactions(count);
                returnMenu();
                break;
            }
            case 5: {
                System.out.println("Exiting...");
                user = null;
                input.close();
                System.exit(0);
                break;
            }
            default: {
                break;
            }
        }
    }

    //Refresh user object
    public static void refreshInfo(){
        try (Connection connection = con()){
            String getInfoQuery = "SELECT * FROM userInfo WHERE email = '" + user.getEmail() + "' AND password = '" + user.getPassword() + "'";
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet queryResult = statement.executeQuery(getInfoQuery);
            queryResult.next();
            user = new User(
                    queryResult.getString(1),
                    queryResult.getString(2),
                    queryResult.getFloat(3),
                    queryResult.getString(4),
                    queryResult.getString(5));

        }catch (SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void returnMenu(){

        System.out.println("Do you want to return to the menu?");
        System.out.println("1: Yes");
        System.out.println("2: Exit");
        System.out.print("Input: ");
        int option;
        try {
            option = input.nextInt();
            input.nextLine();
        }catch (InputMismatchException e){
            System.out.println(e.getLocalizedMessage());
            input.nextLine();
            returnMenu();
            return;
        }
        switch (option) {
            case 1: showMenu();
                break;
            case 2: {
                System.out.println("Exiting...");
                user = null;
                input.close();
                try {
                    con().close();
                } catch (SQLException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                System.exit(0);
                break;
            }
            default: {
                System.out.println("Invalid Option");
                System.out.println("Returning to menu...");
                showMenu();
                break;

            }
        }
    }

}
