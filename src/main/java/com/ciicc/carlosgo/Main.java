package com.ciicc.carlosgo;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        UserAuthentication userAuth = new UserAuthentication();
        CheckBalance balance = new CheckBalance();
        CashIn cashIn = new CashIn();
        CashTransfer cashTransfer = new CashTransfer();
        Transactions transactions = new Transactions();

        int loggedInId = 0;
        boolean loggedIn = false;

        System.out.println("========== Welcome to GcashApp ==========");

        // Registration or Login Loop
        while (!loggedIn) {
            try {
                System.out.println("\n1. Login");
                System.out.println("2. Register");
                System.out.print("Choose an option [1-2]: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Mobile Number: +63 ");
                        long loginNumber = scanner.nextLong();

                        System.out.print("Enter PIN: ");
                        short loginPin = scanner.nextShort();

                        loggedInId = userAuth.Login(loginNumber, loginPin);
                        if (loggedInId != 0) {
                            loggedIn = true;
                        } else {
                            System.out.println("Login failed. Try again.\n");
                        }
                        break;

                    case 2:

                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter Mobile Number: +63 ");
                        long number = scanner.nextLong();

                        System.out.print("Enter 4-digit PIN: ");
                        short pin = scanner.nextShort();

                        userAuth.Registration(name, email, number, pin);
                        break;

                    default:
                        System.out.println("Invalid option. Please choose 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers where required.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Post-login menu
        boolean running = true;
        while (running) {
            try {
                System.out.println("\n========== Main Menu ==========");
                System.out.println("1. Check Balance");
                System.out.println("2. Cash-In");
                System.out.println("3. Transfer");
                System.out.println("4. View Transaction History");
                System.out.println("5. Change PIN");
                System.out.println("6. Logout");
                System.out.print("Choose an option [1-6]: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        float currentBalance = balance.checkBalance(loggedInId);
                        System.out.printf("Current Balance: ₱%,.2f%n", currentBalance);
                        break;

                    case 2:
                        System.out.print("Enter phone number to cash in from: +63 ");
                        long fromNumber = scanner.nextLong();
                        System.out.print("Enter amount to cash in: ");
                        float cashInAmount = scanner.nextFloat();
                        cashIn.cashIn(loggedInId, cashInAmount, fromNumber);
                        break;

                    case 3:
                        System.out.print("Enter recipient phone number: +63 ");
                        long toNumber = scanner.nextLong();
                        System.out.print("Enter amount to transfer: ");
                        float transferAmount = scanner.nextFloat();
                        cashTransfer.cashTransfer(loggedInId, transferAmount, toNumber);
                        break;

                    case 4:
                        transactions.viewUserAll(loggedInId);
                        break;

                    case 5:
                        System.out.print("Enter current PIN: ");
                        short currentPin = scanner.nextShort();
                        System.out.print("Enter new PIN: ");
                        short newPin = scanner.nextShort();
                        userAuth.changePin(loggedInId, currentPin, newPin);
                        break;

                    case 6:
                        userAuth.logout();
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please select between 1-6.");
                }

                if (running) {
                    System.out.print("\nDo you want to do another transaction? (yes/no): ");
                    scanner.nextLine(); // consume newline
                    String again = scanner.nextLine().toLowerCase();
                    if (!again.equals("yes")) {
                        userAuth.logout();
                        running = false;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers where required.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        System.out.println("Thank you for using GcashApp. Goodbye!");
        scanner.close();
    }
}
