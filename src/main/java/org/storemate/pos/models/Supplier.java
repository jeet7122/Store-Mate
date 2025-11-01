package org.storemate.pos.models;

import org.storemate.pos.models.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private int supplier_id;
    private String supplier_name;
    private String supplier_email;
    private String supplier_location;
    private Status  supplier_status;

    public Supplier() {
    }

    public Supplier(String supplier_name, String supplier_email, String supplier_location, Status supplier_status) {
        this.supplier_name = supplier_name;
        this.supplier_email = supplier_email;
        this.supplier_location = supplier_location;
        this.supplier_status = supplier_status;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_email() {
        return supplier_email;
    }

    public void setSupplier_email(String supplier_email) {
        this.supplier_email = supplier_email;
    }

    public String getSupplier_location() {
        return supplier_location;
    }

    public void setSupplier_location(String supplier_location) {
        this.supplier_location = supplier_location;
    }

    public Status getSupplier_status() {
        return supplier_status;
    }

    public void setSupplier_status(Status supplier_status) {
        this.supplier_status = supplier_status;
    }


    @Override
    public String toString() {
        return "Supplier{" +
                "supplier_id=" + supplier_id +
                ", supplier_name='" + supplier_name + '\'' +
                ", supplier_email='" + supplier_email + '\'' +
                ", supplier_location='" + supplier_location + '\'' +
                ", supplier_status=" + supplier_status +
                '}';
    }
}
