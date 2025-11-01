package org.storemate.pos.database;

public class TableConstants {

    // Employees Table
    public static final String EMPLOYEE_TABLE_NAME = "employees";
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
    public static final String CATEGORY_TABLE_NAME = "categories";
    public static final String CATEGORY_COLUMN_ID = "category_id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";
    public static final String CATEGORY_COLUMN_DESCRIPTION = "category_description";
    public static final String CATEGORY_COLUMN_STATUS = "status";

    //Product Table
    public static final String PRODUCT_TABLE_NAME = "products";
    public static final String PRODUCT_COLUMN_ID = "product_id";
    public static final String PRODUCT_COLUMN_NAME = "product_name";
    public static final String PRODUCT_COLUMN_DESCRIPTION = "product_description";
    public static final String PRODUCT_COLUMN_AVAILABILITY = "availability";
    public static final String PRODUCT_COLUMN_QUANTITY = "quantity";
    public static final String PRODUCT_COLUMN_PRICE = "unit_price";
    public static final String PRODUCT_COLUMN_TAX_CATEGORY = "tax_category";

    //Supplier Table
    public static final String SUPPLIER_TABLE_NAME = "suppliers";
    public static final String SUPPLIER_COLUMN_ID = "supplier_id";
    public static final String SUPPLIER_COLUMN_NAME = "supplier_name";
    public static final String SUPPLIER_COLUMN_EMAIL = "supplier_email";
    public static final String SUPPLIER_COLUMN_ADDRESS = "supplier_location";
    public static final String SUPPLIER_COLUMN_CONNECTION = "connection";

    //Product_Supplier Join Table
    public static final String PRODUCT_SUPPLIER_TABLE_NAME = "product_supplier";
    public static final String PRODUCT_SUPPLIER_COLUMN_SUPPLIER_ID = "supplier_id";
    public static final String PRODUCT_SUPPLIER_COLUMN_PRODUCT_ID = "product_id";
    public static final String PRODUCT_SUPPLIER_COLUMN_COST_PRICE = "cost_price";
    public static final String PRODUCT_SUPPLIER_COLUMN_LAST_UPDATED_AT = "last_updated";


    //Purchase Table
    public static final String PURCHASE_TABLE_NAME = "purchases";
    public static final String PURCHASE_COLUMN_ID = "purchase_id";
    public static final String PURCHASE_COLUMN_SUPPLIER_ID = "supplier_id";
    public static final String PURCHASE_COLUMN_TAX_AMOUNT = "tax_amount";
    public static final String PURCHASE_COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String PURCHASE_COLUMN_PURCHASE_DATE = "purchase_date";
    public static final String PURCHASE_COLUMN_STATUS = "status";

    //Purchase Item Table
    public static final String PURCHASE_ITEM_TABLE_NAME = "purchase_items";
    public static final String PURCHASE_ITEM_PURCHASE_ID = "purchase_id";
    public static final String PURCHASE_ITEM_PRODUCT_ID = "product_id";
    public static final String PURCHASE_ITEM_QUANTITY = "quantity";
    public static final String PURCHASE_ITEM_UNIT_PRICE = "unit_price";
    public static final String PURCHASE_ITEM_TAX_CATEGORY = "tax_category";
    public static final String PURCHASE_ITEM_TAX_AMOUNT = "tax_amount";
    public static final String PURCHASE_ITEM_TOTAL_AMOUNT = "total_amount";

    //Sale Table
    public static final String SALE_TABLE_NAME = "sales";
    public static final String SALE_COLUMN_ID = "sale_id";
    public static final String SALE_COLUMN_EMPLOYEE_ID = "employee_id";
    public static final String SALE_COLUMN_STATUS = "status";
    public static final String SALE_COLUMN_SUBTOTAL = "subtotal";
    public static final String SALE_COLUMN_TAX_AMOUNT = "tax_amount";
    public static final String SALE_COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String SALE_COLUMN_SALE_DATE_AND_TIME = "sale_date";

    //Sale Items
    public static final String SALE_ITEM_TABLE_NAME = "sale_items";
    public static final String SALE_ITEM_SALE_ID = "sale_id";
    public static final String SALE_ITEM_PRODUCT_ID = "product_id";
    public static final String SALE_ITEM_QUANTITY = "quantity";
    public static final String SALE_ITEM_UNIT_PRICE = "unit_price";
    public static final String SALE_ITEM_TAX_AMOUNT = "tax_amount";
    public static final String SALE_ITEM_TAX_CATEGORY = "tax_category";
    public static final String SALE_ITEM_TOTAL_AMOUNT = "total_amount";

}
