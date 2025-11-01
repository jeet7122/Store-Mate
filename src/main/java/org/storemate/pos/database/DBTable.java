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
}
