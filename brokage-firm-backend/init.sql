CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS assets (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    asset_name VARCHAR(255) NOT NULL,
    size NUMERIC(15, 2) NOT NULL DEFAULT 0,
    usable_size NUMERIC(15, 2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    asset_name VARCHAR(255) NOT NULL,
    order_side VARCHAR(10) NOT NULL,
    size NUMERIC(15, 2) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    status VARCHAR(10) NOT NULL,
    create_date TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_customers_username ON customers(username);
CREATE INDEX IF NOT EXISTS idx_assets_customer_id ON assets(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON orders(customer_id);

INSERT INTO customers (username, password, role) VALUES
    ('admin', 'admin', 'ADMIN');
