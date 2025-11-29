-- ---------------------------------------------
-- DUMMY DATA FOR STOREMATE POS (LARGE DATASET)
-- ---------------------------------------------

-- ================================
-- INSERT: CATEGORIES (20 rows)
-- ================================
INSERT INTO categories (category_name, category_description, status) VALUES
('Beverages', 'All drink products', 'ACTIVE'),
('Snacks', 'Chips, biscuits, namkeen', 'ACTIVE'),
('Dairy', 'Milk, yogurt, cheese', 'ACTIVE'),
('Bakery', 'Bread and baked items', 'ACTIVE'),
('Frozen Foods', 'Frozen meals, ice cream', 'ACTIVE'),
('Produce', 'Vegetables and fruits', 'ACTIVE'),
('Household', 'Cleaning & home items', 'ACTIVE'),
('Beauty', 'Cosmetics & skincare', 'ACTIVE'),
('Stationery', 'Pens, notebooks, office items', 'ACTIVE'),
('Meat', 'Fresh & frozen meat', 'ACTIVE'),
('Seafood', 'Fish & related items', 'ACTIVE'),
('Electronics', 'Small electronics', 'ACTIVE'),
('Baby Products', 'Baby care items', 'ACTIVE'),
('Pet Supplies', 'Pet foods and toys', 'ACTIVE'),
('Health', 'Medicines, supplements', 'ACTIVE'),
('Grains', 'Rice, flour, lentils', 'ACTIVE'),
('Spices', 'Masala & seasoning', 'ACTIVE'),
('Condiments', 'Sauces & dips', 'ACTIVE'),
('Breakfast Items', 'Cereal, oats, spreads', 'ACTIVE'),
('Canned Goods', 'Canned food items', 'ACTIVE');



-- ================================
-- INSERT: EMPLOYEES (20 rows)
-- ================================
INSERT INTO employees (email, username, password, role) VALUES
('admin@storemate.com', 'admin', 'admin123', 'ADMIN'),
('cashier1@storemate.com', 'cashier1', 'cashier123', 'CASHIER'),
('cashier2@storemate.com', 'cashier2', 'cashier123', 'CASHIER'),
('cashier3@storemate.com', 'cashier3', 'cashier123', 'CASHIER'),
('manager1@storemate.com', 'manager1', 'manager123', 'MANAGER'),
('stock1@storemate.com', 'stock1', 'stock123', 'STOCK'),
('stock2@storemate.com', 'stock2', 'stock123', 'STOCK'),
('assistant1@storemate.com', 'assistant1', 'assist123', 'ASSISTANT'),
('assistant2@storemate.com', 'assistant2', 'assist123', 'ASSISTANT'),
('hr1@storemate.com', 'hr1', 'hr123', 'HR'),
('hr2@storemate.com', 'hr2', 'hr123', 'HR'),
('cashlead@storemate.com', 'cashlead', 'lead123', 'CASHIER_LEAD'),
('supervisor1@storemate.com', 'supervisor1', 'sup123', 'SUPERVISOR'),
('supervisor2@storemate.com', 'supervisor2', 'sup123', 'SUPERVISOR'),
('itadmin@storemate.com', 'itadmin', 'it123', 'IT'),
('support1@storemate.com', 'support1', 'sup123', 'SUPPORT'),
('support2@storemate.com', 'support2', 'sup123', 'SUPPORT'),
('trainer1@storemate.com', 'trainer1', 'train123', 'TRAINER'),
('trainer2@storemate.com', 'trainer2', 'train123', 'TRAINER'),
('auditor@storemate.com', 'auditor', 'audit123', 'AUDITOR');



-- ================================
-- INSERT: SUPPLIERS (20 rows)
-- ================================
INSERT INTO suppliers (supplier_name, supplier_email, supplier_location, connection) VALUES
('FreshBev Ltd', 'contact@freshbev.com', 'Toronto', 'Phone'),
('SnackWorld Inc', 'sales@snackworld.com', 'Mississauga', 'Email'),
('DairyPure Co', 'support@dairypure.com', 'Brampton', 'Phone'),
('BakeHouse Foods', 'orders@bakehouse.com', 'Toronto', 'Email'),
('FrozenKing Ltd', 'info@frozenking.com', 'Calgary', 'Phone'),
('VeggieFarm', 'hello@veggiefarm.com', 'Vancouver', 'Email'),
('HomeClean Supply', 'contact@homeclean.com', 'Ottawa', 'Phone'),
('GlowBeauty Corp', 'support@glowbeauty.com', 'Toronto', 'Email'),
('WriteWell Stationery', 'info@writewell.com', 'Montreal', 'Phone'),
('MeatMaster', 'orders@meatmaster.com', 'Winnipeg', 'Phone'),
('OceanFresh', 'sales@oceanfresh.com', 'Halifax', 'Email'),
('TechPoint Electronics', 'contact@techpoint.com', 'Toronto', 'Email'),
('BabyJoy', 'hello@babyjoy.com', 'Regina', 'Phone'),
('PetWorld Canada', 'support@petworld.com', 'Calgary', 'Email'),
('HealthPlus Pharma', 'info@healthplus.com', 'Toronto', 'Phone'),
('GrainFarmers Co', 'orders@grainfarmers.com', 'Saskatoon', 'Email'),
('SpiceHeaven', 'contact@spiceheaven.com', 'Edmonton', 'Phone'),
('SaucyFoods', 'hello@saucyfoods.com', 'Hamilton', 'Email'),
('MorningBite', 'sales@morningbite.com', 'London', 'Email'),
('CannedGoodies Ltd', 'info@cannedgoodies.com', 'Quebec City', 'Phone');



-- ================================
-- INSERT: PRODUCTS (20 rows)
-- ================================
INSERT INTO products (product_id, product_name, product_description, category_id, tax_category, unit_price, quantity, availability) VALUES
('P1001', 'Coca-Cola 500ml', 'Cold drink', 1, 'HST', 1.99, 120, 'IN_STOCK'),
('P1002', 'Pepsi 500ml', 'Cold drink', 1, 'HST', 1.89, 100, 'IN_STOCK'),
('P1003', 'Sprite 500ml', 'Cold drink', 1, 'HST', 1.89, 95, 'IN_STOCK'),
('P2001', 'Lays Classic', 'Chips', 2, 'HST', 2.29, 150, 'IN_STOCK'),
('P2002', 'Doritos Nacho', 'Chips', 2, 'HST', 2.49, 140, 'IN_STOCK'),
('P2003', 'Oreo Biscuits', 'Biscuits', 2, 'HST', 1.79, 130, 'IN_STOCK'),
('P3001', 'Milk 2% 1L', 'Milk pack', 3, 'HST', 2.49, 70, 'IN_STOCK'),
('P3002', 'Cheddar Cheese', 'Dairy', 3, 'HST', 4.99, 40, 'IN_STOCK'),
('P3003', 'Yogurt Cup', 'Flavoured yogurt', 3, 'HST', 1.49, 90, 'IN_STOCK'),
('P4001', 'Whole Wheat Bread', 'Bread', 4, 'HST', 2.99, 60, 'IN_STOCK'),
('P4002', 'Buns Pack', 'Bakery buns', 4, 'HST', 2.49, 80, 'IN_STOCK'),
('P5001', 'Frozen Pizza', 'Cheese pizza', 5, 'HST', 6.99, 35, 'IN_STOCK'),
('P5002', 'Ice Cream Tub', 'Vanilla ice cream', 5, 'HST', 5.49, 50, 'IN_STOCK'),
('P6001', 'Apples 1kg', 'Fresh apples', 6, 'HST', 3.99, 60, 'IN_STOCK'),
('P6002', 'Bananas 1kg', 'Fresh bananas', 6, 'HST', 2.49, 80, 'IN_STOCK'),
('P7001', 'Dish Soap', 'Household cleaning', 7, 'HST', 3.29, 40, 'IN_STOCK'),
('P8001', 'Face Wash', 'Skincare product', 8, 'HST', 5.99, 35, 'IN_STOCK'),
('P9001', 'Notebook A4', 'Stationery notebook', 9, 'HST', 1.99, 200, 'IN_STOCK'),
('P9002', 'Ball Pen Blue', 'Blue pen', 9, 'HST', 0.99, 300, 'IN_STOCK'),
('P10001', 'Chicken Breast 1kg', 'Fresh meat', 10, 'HST', 8.99, 25, 'IN_STOCK');



-- ======================================
-- INSERT: PRODUCT → SUPPLIER (20 rows)
-- ======================================
INSERT INTO product_supplier (product_id, supplier_id, cost_price) VALUES
('P1001', 1, 1.00),
('P1002', 1, 1.00),
('P1003', 1, 1.00),

('P2001', 2, 1.10),
('P2002', 2, 1.20),
('P2003', 2, 0.90),

('P3001', 3, 1.40),
('P3002', 3, 3.00),
('P3003', 3, 0.90),

('P4001', 4, 1.50),
('P4002', 4, 1.20),

('P5001', 5, 4.00),
('P5002', 5, 3.20),

('P6001', 6, 2.00),
('P6002', 6, 1.40),

('P7001', 7, 1.80),

('P8001', 8, 3.40),

('P9001', 9, 0.60),
('P9002', 9, 0.20);
