package org.storemate.pos.database;

import static org.storemate.pos.database.TableConstants.*;

public class DBTable {

    public static final String CREATE_EMPLOYEE_TABLE =
            "CREATE TABLE " + EMPLOYEE_TABLE_NAME + " ("
                    + EMPLOYEE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + EMPLOYEE_COLUMN_FIRST_NAME + " VARCHAR(50) NOT NULL, "
                    + EMPLOYEE_COLUMN_USER_NAME + " VARCHAR(50) NOT NULL UNIQUE, "
                    + EMPLOYEE_COLUMN_PASSWORD + " VARCHAR(100) NOT NULL, "
                    + EMPLOYEE_COLUMN_JOINING_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + EMPLOYEE_COLUMN_MODIFIED_PROFILE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                    + ");";

    public static final String CREATE_SHIFT_TABLE =
            "CREATE TABLE " + SHIFT_TABLE_NAME + " ("
                    + SHIFT_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SHIFT_COLUMN_EMPLOYEE_ID + " INT NOT NULL, "
                    + SHIFT_COLUMN_LOGIN_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SHIFT_COLUMN_LOGOUT_TIME + " TIMESTAMP NULL DEFAULT NULL, "
                    + "FOREIGN KEY (" + SHIFT_COLUMN_EMPLOYEE_ID + ") REFERENCES "
                    + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + ")"
                    + ");";


    public static final String CREATE_SUPPLIER_TABLE =
            "CREATE TABLE " + SUPPLIER_TABLE_NAME + " ("
            + SUPPLIER_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
            + SUPPLIER_COLUMN_NAME + " VARCHAR(50) NOT NULL, "
            + SUPPLIER_COLUMN_EMAIL + " VARCHAR(55) NOT NULL, "
            + SUPPLIER_COLUMN_ADDRESS + " VARCHAR(50) NOT NULL, "
            + SUPPLIER_COLUMN_CONNECTION + " VARCHAR(50) NOT NULL); ";


    public static final String CREATE_PRODUCT_SUPPLIER_TABLE =
            "CREATE TABLE " + PRODUCT_SUPPLIER_TABLE_NAME + " ("
            + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + " INT PRIMARY KEY, "
            + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + " INT NOT NULL PRIMARY KEY, "
            + PRODUCT_SUPPLIER_COLUMN_COST_PRICE + " DECIMAL(5,2) NOT NULL, "
            + PRODUCT_SUPPLIER_COLUMN_LAST_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
}
