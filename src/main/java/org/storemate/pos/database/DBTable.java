package org.storemate.pos.database;
import static org.storemate.pos.database.TableConstants.*;

public class DBTable {
    public static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE_NAME + " (" +
                    CATEGORY_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, " +
                    CATEGORY_COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
                    CATEGORY_COLUMN_DESCRIPTION + " TEXT, " +
                    CATEGORY_COLUMN_STATUS + " VARCHAR(20) NOT NULL" +
                    ");";

    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE_NAME + " (" +
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
            "CREATE TABLE IF NOT EXISTS " + EMPLOYEE_TABLE_NAME + " ("
                    + EMPLOYEE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + EMPLOYEE_COLUMN_EMAIL + " VARCHAR(255) NOT NULL, "
                    + EMPLOYEE_COLUMN_USER_NAME + " VARCHAR(50) NOT NULL UNIQUE, "
                    + EMPLOYEE_COLUMN_PASSWORD + " VARCHAR(100) NOT NULL, "
                    + EMPLOYEE_COLUMN_ROLE + " VARCHAR(50) NOT NULL, "
                    + EMPLOYEE_COLUMN_JOINING_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + EMPLOYEE_COLUMN_MODIFIED_PROFILE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                    + ");";

    //Shift Tables Schema
    public static final String CREATE_SHIFT_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SHIFT_TABLE_NAME + " ("
                    + SHIFT_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SHIFT_COLUMN_EMPLOYEE_ID + " INT NOT NULL, "
                    + SHIFT_COLUMN_START_FLOAT_CASH + " DECIMAL(5,2) NOT NULL, "
                    + SHIFT_COLUMN_END_FLOAT_CASH + " DECIMAL(5,2) NOT NULL, "
                    + SHIFT_COLUMN_LOGIN_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SHIFT_COLUMN_LOGOUT_TIME + " TIMESTAMP NULL DEFAULT NULL, "
                    + "FOREIGN KEY (" + SHIFT_COLUMN_EMPLOYEE_ID + ") REFERENCES "
                    + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + ")"
                    + ");";


    //Suppliers Tables Schema
    public static final String CREATE_SUPPLIER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SUPPLIER_TABLE_NAME + " ("
                    + SUPPLIER_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SUPPLIER_COLUMN_NAME + " VARCHAR(50) NOT NULL, "
                    + SUPPLIER_COLUMN_EMAIL + " VARCHAR(55) NOT NULL, "
                    + SUPPLIER_COLUMN_ADDRESS + " VARCHAR(50) NOT NULL, "
                    + SUPPLIER_COLUMN_CONNECTION + " VARCHAR(50) NOT NULL); ";


    //Suppliers and Products join Tables Schema
    public static final String CREATE_PRODUCT_SUPPLIER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PRODUCT_SUPPLIER_TABLE_NAME + " ("
                    + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + " VARCHAR(255) NOT NULL, "
                    + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + " INT NOT NULL, "
                    + PRODUCT_SUPPLIER_COLUMN_COST_PRICE + " DECIMAL(5,2) NOT NULL, "
                    + PRODUCT_SUPPLIER_COLUMN_LAST_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY(" + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + ", " + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + "), "
                    + "FOREIGN KEY (" + PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                    + "FOREIGN KEY (" + PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID + ") REFERENCES "
                    + SUPPLIER_TABLE_NAME + "(" + SUPPLIER_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE );";


    //Sales Table Schema
    public static final String CREATE_SALES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SALE_TABLE_NAME + " ("
                    + SALE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + SALE_COLUMN_EMPLOYEE_ID + " INT NOT NULL, "
                    + SALE_COLUMN_SUBTOTAL + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + SALE_COLUMN_PAYMENT_METHOD + " VARCHAR(20) NOT NULL, "
                    + SALE_COLUMN_DISCOUNT_AMOUNT + " DECIMAL(5,2), "
                    + SALE_COLUMN_STATUS + " VARCHAR(30) NOT NULL, "
                    + SALE_COLUMN_SALE_DATE_AND_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (" + SALE_COLUMN_EMPLOYEE_ID + ") REFERENCES "
                    + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + "));";


    //Sale_Item Table Schema
    public static final String CREATE_SALE_ITEM_TABLE =
            "CREATE TABLE IF NOT EXISTS " + SALE_ITEM_TABLE_NAME + " ("
                    + SALE_ITEM_SALE_ID + " INT NOT NULL, "
                    + SALE_ITEM_PRODUCT_ID + " VARCHAR(255) NOT NULL, "
                    + SALE_ITEM_UNIT_PRICE + " DECIMAL(5,2) NOT NULL, "
                    + SALE_ITEM_QUANTITY + " INT NOT NULL, "
                    + SALE_ITEM_TAX_CATEGORY + " VARCHAR(10) NOT NULL, "
                    + SALE_ITEM_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + SALE_ITEM_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + "PRIMARY KEY (" + SALE_ITEM_SALE_ID + ", " + SALE_ITEM_PRODUCT_ID + "), "
                    + "FOREIGN KEY (" + SALE_ITEM_SALE_ID + ") REFERENCES "
                    + SALE_TABLE_NAME + "(" + SALE_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                    + "FOREIGN KEY (" + SALE_ITEM_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE );";
    //Purchase Table Schema
    public static final String CREATE_PURCHASE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PURCHASE_TABLE_NAME + " ("
                    + PURCHASE_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
                    + PURCHASE_COLUMN_SUPPLIER_ID + " INT NOT NULL, "
                    + PURCHASE_COLUMN_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + PURCHASE_COLUMN_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + PURCHASE_COLUMN_STATUS + " VARCHAR(20) NOT NULL, "
                    + PURCHASE_COLUMN_PAYMENT_METHOD + " VARCHAR(20) NOT NULL, "
                    + PURCHASE_COLUMN_PURCHASE_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY (" + PURCHASE_COLUMN_SUPPLIER_ID + ") REFERENCES "
                    + SUPPLIER_TABLE_NAME + "(" + SUPPLIER_COLUMN_ID + "));";

    //Purchase Item Table Schema
    public static final String CREATE_PURCHASE_ITEM_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PURCHASE_ITEM_TABLE_NAME + " ("
                    + PURCHASE_ITEM_PURCHASE_ID + " INT NOT NULL, "
                    + PURCHASE_ITEM_PRODUCT_ID + " VARCHAR(255) NOT NULL PRIMARY KEY, "
                    + PURCHASE_ITEM_QUANTITY + " INT NOT NULL, "
                    + PURCHASE_ITEM_TAX_CATEGORY + " VARCHAR(10) NOT NULL, "
                    + PURCHASE_ITEM_UNIT_PRICE + " DECIMAL(5,2) NOT NULL, "
                    + PURCHASE_ITEM_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
                    + PURCHASE_ITEM_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL,"
                    + "FOREIGN KEY (" + PURCHASE_ITEM_PRODUCT_ID + ") REFERENCES "
                    + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "));";


    public static final String CREATE_RETURNS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + RETURN_TABLE_NAME + " ("
            + RETURN_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
            + RETURN_COLUMN_PROCESSED_BY + " INT NOT NULL, "
            + RETURN_COLUMN_REFUND_METHOD + " VARCHAR(20) NOT NULL, "
            + RETURN_COLUMN_REFUND_TOTAL + " DECIMAL(5,2) NOT NULL, "
            + RETURN_COLUMN_RETURN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY (" + RETURN_COLUMN_PROCESSED_BY + ") REFERENCES "
            + EMPLOYEE_TABLE_NAME + "(" + EMPLOYEE_COLUMN_ID + ")); ";

    public static final String CREATE_RETURN_ITEMS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + RETURN_ITEMS_TABLE_NAME + " ("
            + RETURN_ITEMS_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, "
            + RETURN_ITEMS_COLUMN_RETURN_ID + " INT NOT NULL, "
            + RETURN_ITEMS_COLUMN_PRODUCT_ID + " VARCHAR(255) NOT NULL, "
            + RETURN_ITEMS_COLUMN_RETURN_REASON + " VARCHAR(25) NOT NULL, "
            + RETURN_ITEMS_COLUMN_PRODUCT_QUANTITY + " INT NOT NULL, "
            + RETURN_ITEMS_COLUMN_PRODUCT_UNIT_PRICE + " DECIMAL(5,2) NOT NULL, "
            + RETURN_ITEMS_COLUMN_TAX_AMOUNT + " DECIMAL(5,2) NOT NULL, "
            + RETURN_ITEMS_COLUMN_TOTAL_AMOUNT + " DECIMAL(5,2) NOT NULL, "
            + "FOREIGN KEY (" + RETURN_ITEMS_COLUMN_RETURN_ID + ") REFERENCES "
            + RETURN_TABLE_NAME + "(" + RETURN_COLUMN_ID + "), "
            + "FOREIGN KEY (" + RETURN_ITEMS_COLUMN_PRODUCT_ID + ") REFERENCES "
            + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "));";


}
