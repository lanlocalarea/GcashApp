package com.ciicc.carlosgo;

import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        UserAuthentication userAuth = new UserAuthentication();
//        userAuth.Registration("Carlos","gocarlos519@gmail.com",9265305606L,(short) 1234);
//        System.out.println(userAuth.Login(9265305606L, (short) 1234));
//        System.out.println(userAuth.getName());
//        userAuth.changePin((short)1234, (short) 1234);
//        userAuth.logout();
//        System.out.println(userAuth.getName());
        CheckBalance balance = new CheckBalance();
        int userID = userAuth.Login(9265305606L, (short) 1234);
//        String currentBalance = df.format(balance.checkBalance(userAuth.getId()));
        System.out.println(userAuth.getName() + " balance is: Php " + df.format(balance.checkBalance(userID)));
        CashIn cashIn = new CashIn();
        cashIn.cashIn(userID, 100, 9265305606L);
        System.out.println(userAuth.getName() + " balance is: Php " + df.format(balance.checkBalance(userID)));
    }
}