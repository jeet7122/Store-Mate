# Models

## Employees:
- id
- first_name
- user_name
- password
- roles
- status
- created_at
- updated_at

## Shift
- shift_id
- employee_id
- last_login
- last_logout
- shift_status

## Product
- product_id
- product_name
- product_description
- category_id
- tax_category
- selling_price
- quantity
- status

## Supplier
- supplier_id
- supp_name
- email
- location
- status


## Supplier_Product_Table
- supplier_id
- product_id
- cost_price
- last_updated

## Sale Item
- sale_id
- product_id
- quantity
- selling_price
- tax_rate
- tax_amount

## Sale
- sale_id
- employee_id
- sale_date
- total_amount
- payment_method
- tax_amount
- discount_amount
- sale_status

## Product_category
- category_id
- category_name
- category_description
- status

## Purchase
- purchase_id
- supplier_id
- purchase_date
- tax_amount
- total_amount
- purchase_status

## Purchase_Items
- purchase_id
- product_id
- quantity
- cost_price
- tax_rate
- tax_amount
