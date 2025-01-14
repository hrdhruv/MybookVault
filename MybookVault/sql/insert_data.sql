-- Insert Data into Buyer Table
INSERT INTO buyer (buyer_id, buyer_name, email, address)
VALUES 
    ('B001', 'Harsh Dhruv', 'Harsh@gamil.com', '123 Main St'),
    ('B002', 'Alice Bob', 'alice@gamil.com', '456 back St'),
    ('B003', 'Rohan desai', 'Rohan@gamil.com', '789 Oak St');

-- Insert Data into Books Table
INSERT INTO books (book_id, book_name, book_author, publication_year, issuer)
VALUES 
    ('ISBN001', 'Book of Knowledge', 'Jane Arthur', 2020, 'B001'),
    ('ISBN002', 'Great Adventures', 'John Writer', 2018, 'B002'),
    ('ISBN003', 'Mystery Novel', 'spencer Novelist', 2019, 'B003');

-- Insert Data into Admin Table
INSERT INTO admin (admin_id, admin_name, admin_password)
VALUES 
    ('A001', 'Admin1', 'adminpass123456'),
    ('A002', 'Admin2', 'securepassword'),
    ('A003', 'Admin3', 'password');
