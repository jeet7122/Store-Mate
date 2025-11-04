package org.storemate.pos.database;
import static org.storemate.pos.database.TableConstants.*;

public class DBTable {
    public static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                    CATEGORY_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, " +
                    CATEGORY_COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
                    CATEGORY_COLUMN_DESCRIPTION + " TEXT, " +
                    CATEGORY_COLUMN_STATUS + " VARCHAR(20) NOT NULL" +
                    ");";

    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + PRODUCT_TABLE_NAME + " (" +
                    PRODUCT_COLUMN_ID + " VARCHAR(255) PRIMARY KEY, " +
                    PRODUCT_COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
                    PRODUCT_COLUMN_DESCRIPTION + " TEXT, " +
                    PRODUCT_COLUMN_CATEGORY_ID + " INT NOT NULL, " +
                    PRODUCT_COLUMN_TAX_CATEGORY + " VARCHAR(10) NOT NULL, " +
                    PRODUCT_COLUMN_PRICE + " DECIMAL(10,2) NOT NULL, " +
                    PRODUCT_COLUMN_QUANTITY + " INT NOT NULL, " +
                    PRODUCT_COLUMN_AVAILABILITY + " VARCHAR(20) NOT NULL, " +
                    "FOREIGN KEY (" + PRODUCT_COLUMN_CATEGORY_ID + ") REFERENCES " + CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + ")" +
                    ");";

    //Employee's Tables Schema
    public static final String CREATE_EMPLOYEE_TABLE =
            "CREATE TABLE " + EMPLOYEE_TABLE_NAME + " ("
                    + EMPLOYEE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + EMPLOYEE_COLUMN_FIRST_NAME + " VARCHAR(50) NOT NULL, "
                    + EMPLOYEE_COLUMN_USER_NAME + " VARCHAR(50) NOT NULL UNIQUE, "
                    + EMPLOYEE_COLUMN_PASSWORD + " VARCHAR(100) NOT NULL, "
                    + EMPLOYEE_COLUMN_JOINING_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + EMPLOYEE_COLUMN_MODIFIED_PROFILE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                    + ");";

    //Shift Tables Schema

    public static final String CREATE_SHIFT_TABLE =
            "CREATE TABLE " + SHIFT_TABLE_NAME + " ("
                    + SHIFT_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SHIFT_COLUMN_EMPLOYEE_ID + " INT NOT NULL, "
                    + SHIFT_COLUMN_LOGIN_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SHIFT_COLUMN_LOGOUT_TIME + " TIMESTAMP NULL DEFAULT NULL, "
                    + "FOREIGN KEY (" + SHIFT_COLUMN_EMPLOYEE_ID + ") REFERENCES "
                    + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + ")"
                    + ");";


    //Suppliers Tables Schema
    public static final String CREATE_SUPPLIER_TABLE =
            "CREATE TABLE " + SUPPLIER_TABLE_NAME + " ("
                    + SUPPLIER_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SUPPLIER_COLUMN_NAME + " VARCHAR(50) NOT NULL, "
                    + SUPPLIER_COLUMN_EMAIL + " VARCHAR(55) NOT NULL, "
                    + SUPPLIER_COLUMN_ADDRESS + " VARCHAR(50) NOT NULL, "
                    + SUPPLIER_COLUMN_CONNECTION + " VARCHAR(50) NOT NULL); ";


    //Suppliers and Products join Tables Schema
    public static final String CREATE_PRODUCT_SUPPLIER_TABLE =
            "CREATE TABLE " + PRODUCT_SUPPLIER_TABLE_NAME + " ("
                    + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + " VARCHAR(255) NOT NULL PRIMARY KEY, "
                    + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + " INT NOT NULL PRIMARY KEY, "
                    + PRODUCT_SUPPLIER_COLUMN_COST_PRICE + " DECIMAL(5,2) NOT NULL, "
                    + PRODUCT_SUPPLIER_COLUMN_LAST_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (" + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "),"
                    + "FOREIGN KEY (" + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + ") REFERENCES "
                    + SUPPLIER_TABLE_NAME + "(" + SUPPLIER_COLUMN_ID + "));";


    //Sales Table Schema
    public static final String CREATE_SALES_TABLE =
            "CREATE TABLE " + SALE_TABLE_NAME + " ("
                    + SALE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SALE_COLUMN_EMPLOYEE_ID + " INT NOT NULL, "
                    + SALE_COLUMN_SUBTOTAL + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_STATUS + " VARCHAR(30) NOT NULL, "
                    + "FOREIGN KEY (" + SALE_COLUMN_EMPLOYEE_ID + ") REFERENCES "
                    + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + "));";


    //Sale_Item Table Schema
    public static final String CREATE_SALE_ITEM_TABLE =
            "CREATE TABLE " + SALE_ITEM_TABLE_NAME + " ("
                    + SALE_ITEM_SALE_ID + " INT NOT NULL PRIMARY KEY, "
                    + SALE_ITEM_PRODUCT_ID + " VARCHAR(255) NOT NULL PRIMARY KEY, "
                    + SALE_ITEM_QUANTITY + " INT NOT NULL, "
                    + SALE_ITEM_TAX_CATEGORY + " VARCHAR(10) NOT NULL, "
                    + SALE_ITEM_TAX_AMOUNT + " DECIMAL(5,2), NOT NULL, "
                    + SALE_ITEM_TOTAL_AMOUNT + " DECIMAL(5,2), NOT NULL, "
                    + "FOREIGN KEY (" + SALE_ITEM_SALE_ID + ") REFERENCES "
                    + SALE_TABLE_NAME + "(" + SALE_COLUMN_ID + "),"
                    + "FOREIGN KEY (" + SALE_ITEM_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "));";
    //Purchase Table Schema
    public static final String CREATE_PURCHASE_TABLE =
            "CREATE TABLE " + PURCHASE_TABLE_NAME + " ("
                    + PURCHASE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + PURCHASE_COLUMN_SUPPLIER_ID + " INT NOT NULL PRIMARY KEY, "
                    + PURCHASE_COLUMN_TAX_AMOUNT + " DECIMAL(5,2), NOT NULL, "
                    + PURCHASE_COLUMN_TOTAL_AMOUNT + " DECIMAL(5,2), NOT NULL, "
                    + PURCHASE_COLUMN_STATUS + " VARCHAR(20) NOT NULL, "
                    + PURCHASE_COLUMN_PURCHASE_DATE + " DATE DEFAULT (CURRENT_DATE), "
                    + "FOREIGN KEY (" + PURCHASE_COLUMN_SUPPLIER_ID + ") REFERENCES "
                    + SUPPLIER_TABLE_NAME + "(" + SUPPLIER_COLUMN_ID + "));";

    //Purchase Item Table Schema
    public static final String CREATE_PURCHASE_ITEM_TABLE =
            "CREATE TABLE " + PURCHASE_ITEM_TABLE_NAME + " ("
                    + PURCHASE_ITEM_PURCHASE_ID + " INT PRIMARY KEY NOT NULL, "
                    + PURCHASE_ITEM_PRODUCT_ID + " VARCHAR(255) NOT NULL PRIMARY KEY, "
                    + PURCHASE_ITEM_QUANTITY + " INT NOT NULL, "
                    + PURCHASE_ITEM_TAX_CATEGORY + " VARCHAR(10) NOT NULL, "
                    + PURCHASE_ITEM_UNIT_PRICE + " DECIMAL(5,2), NOT NULL, "
                    + PURCHASE_ITEM_TAX_AMOUNT + " DECIMAL(5,2), NOT NULL, "
                    + PURCHASE_ITEM_TOTAL_AMOUNT + " DECIMAL(5,2), NOT NULL,"
                    + "FOREIGN KEY (" + PURCHASE_ITEM_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "));";
}
