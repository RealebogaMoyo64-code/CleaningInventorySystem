-- Run this against the same cleaning_inventory_db database as your users table.

CREATE TABLE materials
    ( id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    unit VARCHAR(20) NOT NULL,
    reorder_level INT NOT NULL DEFAULT 0 CHECK (reorder_level >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );

-- A few sample rows so the Materials screen and Dashboard have something to show immediately.
INSERT INTO materials (name, quantity, unit, reorder_level) VALUES
    ('All-Purpose Cleaner', 40, 'bottles', 10),
    ('Microfiber Cloths', 8, 'packs', 15),
    ('Floor Polish', 20, 'litres', 5),
    ('Trash Bags (Large)', 150, 'rolls', 30);
