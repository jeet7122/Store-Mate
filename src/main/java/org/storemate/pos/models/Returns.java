package org.storemate.pos.models;

import org.storemate.pos.models.enums.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a return transaction in the POS system.
 * <p>
 * Contains information such as the return ID, associated sale ID,
 * the employee processing the return, payment method used for the refund,
 * the date of the return, and the total amount refunded.
 * @author Jeet
 */
public class Returns {
    private int returnID;
    private int saleID;
    private int employeeID;
    private PaymentMethod paymentMethod;
    private Timestamp returnDate;
    private BigDecimal totalAmount;


    @Override
    public String toString() {
        return "Returns{" +
                "returnID=" + returnID +
                ", saleID=" + saleID +
                ", employeeID=" + employeeID +
                ", paymentMethod=" + paymentMethod +
                ", returnDate=" + returnDate +
                ", totalAmount=" + totalAmount +
                '}';
    }

    public Returns() {
    }


    public Returns(int returnID, int saleID, int employeeID, PaymentMethod paymentMethod, Timestamp returnDate, BigDecimal totalAmount) {
        this.returnID = returnID;
        this.saleID = saleID;
        this.employeeID = employeeID;
        this.paymentMethod = paymentMethod;
        this.returnDate = returnDate;
        this.totalAmount = totalAmount;
    }

    public int getReturnID() {
        return returnID;
    }

    public void setReturnID(int returnID) {
        this.returnID = returnID;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
