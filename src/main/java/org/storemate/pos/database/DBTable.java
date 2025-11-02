package org.storemate.pos.database;
import static org.storemate.pos.database.TableConstants.*;

public class DBTable {

    public static final String  CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                    CATEGORY_COLUMN_ID + " INT PRIMARY KEY AUTO_INCREMENT, " +
                    CATEGORY_COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
                    CATEGORY_COLUMN_DESCRIPTION + " TEXT, " +
                    CATEGORY_COLUMN_STATUS + " VARCHAR(20) NOT NULL" +
                    ");";

    public static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + PRODUCT_TABLE_NAME + " (" +
                    PRODUCT_COLUMN_ID + " VARCHAR(50) PRIMARY KEY, " +
                    PRODUCT_COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
                    PRODUCT_COLUMN_DESCRIPTION + " TEXT, " +
                    PURCHASE_ITEM_PRODUCT_ID + " INT NOT NULL, " +
                    PRODUCT_COLUMN_TAX_CATEGORY + " VARCHAR(50) NOT NULL, " +
                    PRODUCT_COLUMN_PRICE + " DECIMAL(10,2) NOT NULL, " +
                    PRODUCT_COLUMN_QUANTITY + " INT NOT NULL, " +
                    PRODUCT_COLUMN_AVAILABILITY + " VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (" + PURCHASE_ITEM_PRODUCT_ID + ") REFERENCES " + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + ")" +
                    ");";
}


