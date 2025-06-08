package com.ciicc.carlosgo;
import java.time.*;

public class Transaction {
    private final String email;
    private final String transactionType;
    private final String recipient;
    private final float amount;
    private final LocalDateTime date;

    public Transaction(String email, String transactionType, String recipient, float amount, LocalDateTime date) {
        this.email = email;
        this.transactionType = transactionType;
        this.recipient = recipient;
        this.amount = amount;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
