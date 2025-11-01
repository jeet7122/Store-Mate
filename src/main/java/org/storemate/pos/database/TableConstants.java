package org.storemate.pos.database;

public class TableConstants {


    // Employees Table
    public static final String EMPLOYEE_TABLE_NAME = "employee";
    public static final String EMPLOYEE_COLUMN_ID = "employee_id";
    public static final String EMPLOYEE_COLUMN_FIRST_NAME = "firstname";
    public static final String EMPLOYEE_COLUMN_USER_NAME = "username";
    public static final String EMPLOYEE_COLUMN_PASSWORD = "password";
    public static final String EMPLOYEE_COLUMN_ROLE = "role";
    public static final String EMPLOYEE_COLUMN_JOINING_DATE = "joining_date";
    public static final String EMPLOYEE_COLUMN_MODIFIED_PROFILE = "modified_profile";


    //Shift Table
    public static final String SHIFT_TABLE_NAME = "shift";
    public static final String SHIFT_COLUMN_ID = "shift_id";
    public static final String SHIFT_COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String SHIFT_COLUMN_LOGIN_TIME = "login_time";
    public static final String SHIFT_COLUMN_LOGOUT_TIME = "logout_time";



    //Category Table
    public static final String CATEGORY_TABLE_NAME = "category";
    public static final String CATEGORY_COLUMN_ID = "category_id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";
    public static final String CATEGORY_COLUMN_DESCRIPTION = "category_description";
    public static final String CATEGORY_COLUMN_STATUS = "status";

    //Product Table
    public static final String PRODUCT_TABLE_NAME = "product";
    public static final String PRODUCT_COLUMN_ID = "product_id";
    public static final String PRODUCT_COLUMN_NAME = "product_name";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "product_description";
    public static final String PRODUCT_COLUMN_AVAILABILITY = "availability";
    public static final String PRODUCT_COLUMN_QUANTITY = "quantity";
    public static final String PRODUCT_COLUMN_PRICE = "unit_price";
    public static final String PRODUCT_COLUMN_TAX_CATEGORY = "tax_category";

    //Supplier Table


}
