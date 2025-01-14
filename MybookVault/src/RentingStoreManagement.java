import java.sql.*;
import java.util.Scanner;

public class RentingStoreManagement {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/renting_store?useSSL=false";
    static final String USER = "root";
    static final String PASS = "Asdf1@2jkl";

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            Scanner scan = new Scanner(System.in); 

            clearScreen();
            System.out.println("\nWELCOME TO RENTING BOOK STORE MANAGEMENT SERVICE\n");
            mainMenu(stmt, scan);

            scan.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) { 
            se.printStackTrace();
        } catch (Exception e) { 
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    static void mainMenu(Statement stmt, Scanner scan) {
        System.out.println("Login as a- ");
        System.out.println("1. Buyer");
        System.out.println("2. Admin");
        System.out.println("0. Exit");

        System.out.print("\n\nENTER YOUR CHOICE : ");
        int choice = Integer.parseInt(scan.nextLine());
        clearScreen();

        switch (choice) {
            case 0:
                System.out.println("\nThank you for using this System!!\n\n");
                System.exit(0);
            case 1:
                buyerMenu(stmt, scan);
                break;
            case 2:
                if (authenticateAdmin(stmt, scan)) {
                    adminMenu(stmt, scan);
                } else {
                    System.out.println("Admin authentication failed. Returning to main menu.");
                }
                break;
            
            default:
                clearScreen();
                System.out.println("Please Enter a Valid Choice!!\n");
                break;
        }
        mainMenu(stmt, scan);
    }

    // buyer menu
    static void buyerMenu(Statement stmt, Scanner scan) {
        System.out.println("Please select appropriate option-");
        System.out.println("1. List of available books that are not rented");
        System.out.println("2. Rent a book");
        System.out.println("3. Return a book");
        System.out.println("4. Rate and review book");
        System.out.println("5. View profile");
        System.out.println("6. Search a book");
        System.out.println("0. Exit");

        System.out.print("\n\nENTER YOUR CHOICE : ");
        int choice = Integer.parseInt(scan.nextLine());
        clearScreen();

        switch (choice) {
            case 0:
                return;
            case 1:
                listAvailableBooks(stmt, scan);
                break;
            case 2:
                rentBook(stmt, scan);
                break;
            case 3:
                returnBook(stmt, scan);
                break;
            case 4:
                rateAndReviewBook(stmt, scan);
                break;
            case 5:
                viewProfileInformation(stmt, scan);
                break;
            case 6:
                searchForBook(stmt, scan);
                break;
            default:
                clearScreen();
                System.out.println("Please Enter a Valid Choice!!\n");
                break;
        }
        buyerMenu(stmt, scan);
    }

    //admin menu
    static void adminMenu(Statement stmt, Scanner scan) {
        System.out.println("Please select appropriate option-");
        System.out.println("1. List of all books");
        System.out.println("2. Add a new book");
        System.out.println("3. Delete a book");
        System.out.println("4. Add buyer");
        System.out.println("5. Update book");
        System.out.println("6. Book rented by buyer with review");
        System.out.println("7. Update buyer address");
        System.out.println("8. Book rented ");
        System.out.println("0. Exit");

        System.out.print("\n\nENTER YOUR CHOICE : ");
        int choice = Integer.parseInt(scan.nextLine());
        clearScreen();

        switch (choice) {
            case 0:
                return;
            case 1:
                listAllBooks(stmt, scan);
                break;
            case 2:
                addNewBook(stmt, scan);
                break;
            case 3:
                deleteBook(stmt, scan);
                break;
            case 4:
                addbuyer(stmt,scan);
                break;
            case 5:
                updateBook(stmt, scan);
                break;
            case 6:
                getBooksRentedByrBuyer(stmt,scan);
                break;
            case 7:
                updateBuyerAddress(stmt, scan);
                break;
            case 8:
                getBooksRentedByBuyer(stmt, scan);
                break;
            default:
                clearScreen();
                System.out.println("Please Enter a Valid Choice!!\n");
                break;
        }
        adminMenu(stmt, scan);
    }
    static boolean authenticateAdmin(Statement stmt, Scanner scan) {
        try {
            System.out.print("Enter your admin ID: ");
            String adminId = scan.nextLine();
            System.out.print("Enter your admin password: ");
            String adminPassword = scan.nextLine();
    
            String sql = "SELECT * FROM admin WHERE admin_id = '" + adminId + "' AND admin_password = '" + adminPassword + "'";
            ResultSet rs = stmt.executeQuery(sql);
    
            if (rs.next()) {
                System.out.println("Authentication successful!");
                return true;
            } else {
                System.out.println("Authentication failed. Please check your admin ID and password.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    static void listAvailableBooks(Statement stmt, Scanner scan) {
        try {
            String sql = "SELECT * FROM books WHERE issuer IS NULL";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("List of available books that are not rented:\n");
            while (rs.next()) {
                String id = rs.getString("book_id");
                String name = rs.getString("book_name");
                String author = rs.getString("book_author");
                int year = rs.getInt("publication_year");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Author: " + author);
                System.out.println("Publication Year: " + year);
                System.out.println();
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void rentBook(Statement stmt, Scanner scan) {
        try {
            System.out.print("Enter the ID of the book you want to rent: ");
            String bookId = scan.nextLine();
    
            // Check if the book is already rented
            String checkSql = "SELECT * FROM books WHERE book_id = '" + bookId + "' AND issuer IS NOT NULL";
            ResultSet rs = stmt.executeQuery(checkSql);
            if (rs.next()) {
                System.out.println("This book is already rented. Please choose another book.");
                return;
            }
    
            System.out.print("Enter your buyer ID: ");
            String buyerId = scan.nextLine();
    
            String updateSql = "UPDATE books SET issuer = '" + buyerId + "' WHERE book_id = '" + bookId + "'";
            int rowsAffected = stmt.executeUpdate(updateSql);
    
            if (rowsAffected > 0) {
                System.out.println("Book rented successfully!");
            } else {
                System.out.println("Failed to rent the book. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    static void returnBook(Statement stmt, Scanner scan) {
        Connection conn = null;
        try {
            conn = stmt.getConnection();
            conn.setAutoCommit(false); // Start transaction
    
            System.out.print("Enter the ID of the book you want to return: ");
            String bookId = scan.nextLine();
    
            String updateSql = "UPDATE books SET issuer = NULL WHERE book_id = '" + bookId + "'";
            int rowsAffected = stmt.executeUpdate(updateSql);
    
            if (rowsAffected > 0) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Failed to return the book. Please try again.");
            }
    
            // If successful, commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction if any exception occurs
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    static void listAllBooks(Statement stmt, Scanner scan) {
        try {
            String sql = "SELECT * FROM books";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("List of all books:\n");
            while (rs.next()) {
                String id = rs.getString("book_id");
                String name = rs.getString("book_name");
                String author = rs.getString("book_author");
                int year = rs.getInt("publication_year");
                String issuer = rs.getString("issuer");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Author: " + author);
                System.out.println("Publication Year: " + year);
                System.out.println("Issuer: " + (issuer == null ? "Available" : issuer));
                System.out.println();
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addNewBook(Statement stmt, Scanner scan) {
        Connection conn = null;
        try {
            conn = stmt.getConnection();
            conn.setAutoCommit(false); // Start transaction
    
            System.out.print("Enter the ID of the new book: ");
            String id = scan.nextLine();
    
            System.out.print("Enter the name of the new book: ");
            String name = scan.nextLine();
    
            System.out.print("Enter the author of the new book: ");
            String author = scan.nextLine();
    
            System.out.print("Enter the publication year of the new book: ");
            int year = Integer.parseInt(scan.nextLine());
    
            // Check if the book ID already exists
            String checkSql = "SELECT * FROM books WHERE book_id = '" + id + "'";
            ResultSet checkResult = stmt.executeQuery(checkSql);
            if (checkResult.next()) {
                System.out.println("Error: Book ID already exists. Please choose a different ID.");
                return;
            }
    
            String insertSql = "INSERT INTO books (book_id, book_name, book_author, publication_year) VALUES ('" + id + "', '" + name + "', '" + author + "', " + year + ")";
            int rowsAffected = stmt.executeUpdate(insertSql);
    
            if (rowsAffected > 0) {
                System.out.println("New book added successfully!");
            } else {
                System.out.println("Failed to add the new book. Please try again.");
            }
    
            // If successful, commit transaction
            conn.commit();
        } catch (SQLException | NumberFormatException e) {
            // Rollback transaction if any exception occurs
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    static void updateBook(Statement stmt, Scanner scan) {
        Connection conn = null;
        try {
            conn = stmt.getConnection();
            conn.setAutoCommit(false); // Start transaction
    
            System.out.print("Enter the ID of the book you want to update: ");
            String bookId = scan.nextLine();
    
            // Fetch book details from the database
            String query = "SELECT * FROM books WHERE book_id = '" + bookId + "'";
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                System.out.println("Current details of the book:");
                System.out.println("Name: " + rs.getString("book_name"));
                System.out.println("Author: " + rs.getString("book_author"));
                System.out.println("Publication Year: " + rs.getInt("publication_year"));
    
                System.out.print("\nEnter new name (press Enter to keep current): ");
                String newName = scan.nextLine().trim();
                newName = newName.isEmpty() ? rs.getString("book_name") : newName;
    
                System.out.print("Enter new author (press Enter to keep current): ");
                String newAuthor = scan.nextLine().trim();
                newAuthor = newAuthor.isEmpty() ? rs.getString("book_author") : newAuthor;
    
                System.out.print("Enter new publication year (press Enter to keep current): ");
                String newYearStr = scan.nextLine().trim();
                int newYear = newYearStr.isEmpty() ? rs.getInt("publication_year") : Integer.parseInt(newYearStr);
    
                // Update book details in the database
                String updateSql = "UPDATE books SET book_name = '" + newName + "', " +
                        "book_author = '" + newAuthor + "', " +
                        "publication_year = " + newYear + " " +
                        "WHERE book_id = '" + bookId + "'";
                int rowsAffected = stmt.executeUpdate(updateSql);
    
                if (rowsAffected > 0) {
                    System.out.println("Book details updated successfully!");
                } else {
                    System.out.println("Failed to update book details. Please try again.");
                }
            } else {
                System.out.println("Book not found!");
            }
    
            // If successful, commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction if any exception occurs
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    static void deleteBook(Statement stmt, Scanner scan) {
        Connection conn = null;
        try {
            conn = stmt.getConnection();
            conn.setAutoCommit(false); // Start transaction
    
            System.out.print("Enter the ID of the book you want to delete: ");
            String bookId = scan.nextLine();
    
            String deleteSql = "DELETE FROM books WHERE book_id = '" + bookId + "'";
            int rowsAffected = stmt.executeUpdate(deleteSql);
    
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Failed to delete the book. Please try again.");
            }
    
            // If successful, commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction if any exception occurs
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    static void addbuyer(Statement stmt, Scanner scan) {
        try {
            System.out.println("Enter buyer ID: ");
            String buyerId = scan.nextLine();
            System.out.println("Enter buyer name: ");
            String buyerName = scan.nextLine();
            System.out.println("Enter email: ");
            String email = scan.nextLine();
            System.out.println("Enter address: ");
            String address = scan.nextLine();
    
            String sql = String.format("INSERT INTO buyer VALUES('%s', '%s', '%s', '%s') ON DUPLICATE KEY UPDATE buyer_name = VALUES(buyer_name), email = VALUES(email), address = VALUES(address)",
                    buyerId, buyerName, email, address);
            int result = updateSqlStmt(stmt, sql);
    
            if (result != 0) {
                System.out.println("Buyer added successfully!");
            } else {
                System.out.println("Failed to add buyer.");
            }
    
            // Update books table if the book is not already rented
            String updateBooksSql = "UPDATE books SET issuer = '" + buyerId + "' WHERE issuer IS NULL LIMIT 1";
            int rowsAffected = stmt.executeUpdate(updateBooksSql);
    
            if (rowsAffected > 0) {
                System.out.println("Updated books table with the new buyer's ID.");
            } else {
                System.out.println("No available books to update in the books table.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    static int updateSqlStmt(Statement stmt, String sql) {
        try {
            int rs = stmt.executeUpdate(sql);
            return rs;
        } catch (SQLException se) {
            se.printStackTrace(); // Handle or log the exception appropriately
        }
        return 0;
    }
    static void rateAndReviewBook(Statement stmt, Scanner scan) {
        try {
            System.out.print("Enter the ID of the book you want to rate and review: ");
            String bookId = scan.nextLine();
    
            System.out.print("Enter your buyer ID: ");
            String buyerId = scan.nextLine();
    
            System.out.print("Enter your rating (1-5): ");
            int rating = Integer.parseInt(scan.nextLine());
    
            System.out.print("Enter your review: ");
            String reviewText = scan.nextLine();
    
            // Insert rating and review data
            String insertFeedbackSql = "INSERT INTO book_feedback (book_id, buyer_id, rating, review_text) VALUES ('" + bookId + "', '" + buyerId + "', " + rating + ", '" + reviewText + "')";
            int rowsAffected = stmt.executeUpdate(insertFeedbackSql);
    
            if (rowsAffected > 0) {
                System.out.println("Rating and review added successfully!");
            } else {
                System.out.println("Failed to add rating and review. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void getBooksRentedByBuyer(Statement stmt, Scanner scan) {
        try {
            System.out.print("Enter the ID of the buyer: ");
            String buyerId = scan.nextLine();
    
            String sql = "SELECT * FROM books WHERE issuer = '" + buyerId + "'";
            ResultSet rs = stmt.executeQuery(sql);
    
            System.out.println("Books Rented by Buyer '" + buyerId + "':\n");
            while (rs.next()) {
                String id = rs.getString("book_id");
                String name = rs.getString("book_name");
                String author = rs.getString("book_author");
                int year = rs.getInt("publication_year");
    
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Author: " + author);
                System.out.println("Publication Year: " + year);
                System.out.println();
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void getBooksRentedByrBuyer(Statement stmt, Scanner scan) {
        try {
            String sql = "SELECT b.book_id, b.book_name, b.book_author, b.publication_year, f.buyer_id, f.rating, f.review_text FROM books b INNER JOIN book_feedback f ON b.book_id = f.book_id";
            ResultSet rs = stmt.executeQuery(sql);
    
            System.out.println("List of books along with the details of the buyers who rated and reviewed them:\n");
            while (rs.next()) {
                String bookId = rs.getString("book_id");
                String bookName = rs.getString("book_name");
                String bookAuthor = rs.getString("book_author");
                int publicationYear = rs.getInt("publication_year");
                String buyerId = rs.getString("buyer_id");
                int rating = rs.getInt("rating");
                String reviewText = rs.getString("review_text");
    
                System.out.println("Book ID: " + bookId);
                System.out.println("Book Name: " + bookName);
                System.out.println("Author: " + bookAuthor);
                System.out.println("Publication Year: " + publicationYear);
                System.out.println("Reviewed by Buyer ID: " + buyerId);
                System.out.println("Rating: " + rating);
                System.out.println("Review: " + reviewText);
                System.out.println();
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    static void updateBuyerAddress(Statement stmt, Scanner scan) {
        Connection conn = null;
        try {
            conn = stmt.getConnection();
            conn.setAutoCommit(false); // Start transaction
    
            System.out.print("Enter buyer ID: ");
            String buyerId = scan.nextLine();
    
            // Fetch the current address from the database
            String getAddressSql = "SELECT address FROM buyer WHERE buyer_id = '" + buyerId + "'";
            ResultSet rs = stmt.executeQuery(getAddressSql);
    
            String previousAddress = "";
            if (rs.next()) {
                previousAddress = rs.getString("address");
                System.out.println("Previous address: " + previousAddress);
            } else {
                System.out.println("Buyer not found!");
                return;
            }
    
            // Prompt for the new address
            System.out.print("Enter the new address: ");
            String newAddress = scan.nextLine();
    
            // Update the address in the database
            String updateAddressSql = "UPDATE buyer SET address = '" + newAddress + "' WHERE buyer_id = '" + buyerId + "'";
            int rowsAffected = stmt.executeUpdate(updateAddressSql);
    
            if (rowsAffected > 0) {
                System.out.println("Buyer address updated successfully!");
            } else {
                System.out.println("Failed to update buyer address.");
            }
    
            // If successful, commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaction if any exception occurs
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back.");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true); // Reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    static void viewProfileInformation(Statement stmt, Scanner scan) {
        try {
            // Request buyer ID from the user
            System.out.print("Enter your buyer ID: ");
            String buyerId = scan.nextLine();
    
            // Query to retrieve the buyer's profile information
            String sql = "SELECT buyer_name, email, address FROM buyer WHERE buyer_id = '" + buyerId + "'";
            ResultSet rs = stmt.executeQuery(sql);
    
            if (rs.next()) {
                // Display the buyer's profile information
                String name = rs.getString("buyer_name");
                String email = rs.getString("email");
                String address = rs.getString("address");
    
                System.out.println("Buyer ID: " + buyerId);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Address: " + address);
            } else {
                System.out.println("Buyer not found!");
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void searchForBook(Statement stmt, Scanner scan) {
        try {
            System.out.print("Enter the name of the book you want to search for: ");
            String bookName = scan.nextLine();
    
            String sql = "SELECT * FROM books WHERE book_name LIKE '%" + bookName + "%'";
            ResultSet rs = stmt.executeQuery(sql);
    
            if (rs.next()) {
                System.out.println("Book found:");
                System.out.println("ID: " + rs.getString("book_id"));
                System.out.println("Name: " + rs.getString("book_name"));
                System.out.println("Author: " + rs.getString("book_author"));
                System.out.println("Publication Year: " + rs.getInt("publication_year"));
                System.out.println("Issuer: " + (rs.getString("issuer") == null ? "Available" : rs.getString("issuer")));
            } else {
                System.out.println("Book not found!");
            }
    
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

    static void clearScreen() {
        System.out.println("\033[H\033[J");
        System.out.flush();
    }
}
