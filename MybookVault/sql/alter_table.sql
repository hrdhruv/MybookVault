-- Add Foreign Key Constraint to Books Table
ALTER TABLE books
ADD CONSTRAINT fk_issuer FOREIGN KEY (issuer) REFERENCES buyer(buyer_id);

-- Add Foreign Key Constraint to Admin Table
ALTER TABLE admin
ADD CONSTRAINT fk_admin FOREIGN KEY (admin_id) REFERENCES buyer(buyer_id);
