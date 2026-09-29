-- Run after materials_table.sql (issuances references both materials and cleaners).

CREATE TABLE suppliers
    ( id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );

CREATE TABLE cleaners
    ( id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    contact_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );

CREATE TABLE issuances
    ( id SERIAL PRIMARY KEY,
    material_id INT NOT NULL REFERENCES materials(id),
    cleaner_id INT NOT NULL REFERENCES cleaners(id),
    quantity INT NOT NULL CHECK (quantity > 0),
    issued_by INT NOT NULL REFERENCES users(id),
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );

-- Sample data so the new screens aren't empty on first run.
INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
    ('CleanCo Supplies', 'Thabo Nkosi', '011-555-0101', 'sales@cleanco.co.za', '12 Industrial Rd, Midrand'),
    ('SparklePro Distributors', 'Anja van der Merwe', '021-555-0199', 'orders@sparklepro.co.za', '45 Main St, Cape Town');

INSERT INTO cleaners (name, department, contact_number) VALUES
    ('Sipho Dlamini', 'Facilities - Block A', '082-555-1234'),
    ('Grace Mokoena', 'Facilities - Block B', '083-555-5678');
