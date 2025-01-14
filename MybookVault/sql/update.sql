-- Update a record in the Buyer Table
UPDATE buyer 
SET email = 'john.doe@example.com', address = '456 Maple St' 
WHERE buyer_id = 'B001';

-- Update a record in the Books Table
UPDATE books 
SET publication_year = 2021 
WHERE book_id = 'ISBN001';

-- Update a record in the Admin Table
UPDATE admin 
SET admin_password = 'newpassword' 
WHERE admin_id = 'A001';

