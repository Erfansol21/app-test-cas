INSERT INTO users (uname, uemail, upassword, unumber)
VALUES
('user', 'user@example.com', '1234', 1234567890),
('sara', 'sara@example.com', 'pass123', 9876543210),
('alex', 'alex@example.com', 'letmein', 5551239876);

INSERT INTO product_table (
    pname,
    pprice,
    pdescription,
    default_serving_size,
    initial_stock,
    calories_per_serving,
    protein_grams,
    carbohydrate_grams,
    fat_grams
)
VALUES
(
    'Chicken Breast',
    7.99,
    'Lean protein',
    1,
    50,
    165,
    31,
    0,
    3.6
),
(
    'White Rice',
    2.49,
    'Basic carb fuel',
    1,
    100,
    130,
    2.7,
    28,
    0.3
),
(
    'Olive Oil',
    6.50,
    'Liquid gold',
    1,
    20,
    119,
    0,
    0,
    13.5
);