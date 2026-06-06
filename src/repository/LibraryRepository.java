package repository;

import models.Book;
import java.sql.*;
import java.time.LocalDate;


public class LibraryRepository {

    // 1. READ: Fetch a book's current availability status
    public String getBookStatus(Connection conn, int bookId) throws SQLException {
        String sql = "SELECT status FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                }
            }
        }
        return null; // Return null if book ID does not exist
    }

    // 2. UPDATE: Change a book's status ('AVAILABLE' or 'ISSUED')
    public void updateBookStatus(Connection conn, int bookId, String status) throws SQLException {
        String sql = "UPDATE books SET status = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    // 3. CREATE: Insert a new checkout log entry into issued_books
    public void insertIssueRecord(Connection conn, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate) throws SQLException {
        String sql = "INSERT INTO issued_books (book_id, member_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            stmt.setDate(3, Date.valueOf(issueDate));
            stmt.setDate(4, Date.valueOf(dueDate));
            stmt.executeUpdate();
        }
    }

    // 4. READ: Find an active rental log details to compute returns
    public int getActiveIssueId(Connection conn, int bookId) throws SQLException {
        String sql = "SELECT issue_id FROM issued_books WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("issue_id");
                }
            }
        }
        return -1; // Return -1 indicating no active rental log found
    }

    // 5. READ: Get the expected due date for an active rental log
    public LocalDate getDueDate(Connection conn, int issueId) throws SQLException {
        String sql = "SELECT due_date FROM issued_books WHERE issue_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, issueId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("due_date").toLocalDate();
                }
            }
        }
        return null;
    }

    // 6. UPDATE: Close an active log entry with return timestamp and fine calculated details
    public void finaliseReturnRecord(Connection conn, int issueId, LocalDate returnDate, double fineAmount) throws SQLException {
        String sql = "UPDATE issued_books SET return_date = ?, fine_amount = ? WHERE issue_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setDouble(2, fineAmount);
            stmt.setInt(3, issueId);
            stmt.executeUpdate();
        }
    }
}
