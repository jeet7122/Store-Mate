package org.storemate.pos.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a financial transaction in the system.
 * <p>
 * Stores details such as transaction ID, type (e.g., sale, return),
 * payment method, amount, and the timestamp of the transaction.
 * @author Jeet
 */
public class Transaction {
    private String id;
    private String type;
    private String paymentMethod;
    private BigDecimal amount;
    private Timestamp date;

    public Transaction() {
    }

    public Transaction(String id, String type, String paymentMethod, BigDecimal amount, Timestamp date){
        this.id = id;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.date = date;
    }

    public String getId(){ return id; }
    public String getType(){ return type; }
    public String getPaymentMethod(){ return paymentMethod; }
    public BigDecimal getAmount(){ return amount; }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
