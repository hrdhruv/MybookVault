-- SQL script to create tables

use renting_store;

-- Create Buyer Table
CREATE TABLE buyer (
    buyer_id VARCHAR(100) NOT NULL,
    buyer_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    address VARCHAR(255),
    CONSTRAINT pk_buyer PRIMARY KEY (buyer_id)
);

-- Create Books Table
CREATE TABLE books (
    book_id VARCHAR(100) NOT NULL,
    book_name VARCHAR(100) NOT NULL,
    book_author VARCHAR(100) NOT NULL,
    publication_year INTEGER NOT NULL,
    issuer VARCHAR(100),
    CONSTRAINT pk_books PRIMARY KEY (book_id)
);

-- Create Admin Table
CREATE TABLE admin (
    admin_id VARCHAR(100) NOT NULL,
    admin_name VARCHAR(100) NOT NULL,
    admin_password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (admin_id)
);

CREATE TABLE book_feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id VARCHAR(100) NOT NULL,
    buyer_id VARCHAR(100) NOT NULL,
    rating INT,
    review_text VARCHAR(255),
    CONSTRAINT fk_book_feedback_book FOREIGN KEY (book_id) REFERENCES books(book_id),
    CONSTRAINT fk_book_feedback_buyer FOREIGN KEY (buyer_id) REFERENCES buyer(buyer_id)
);

