package service;

import config.DatabaseUtil;
import repository.LibraryRepository;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class LibraryService {

    private final LibraryRepository repository;

    // Constructor maps dependency injection framework
    public LibraryService() {
        this.repository = new LibraryRepository();
    }


    public void issueBook(int bookId, int memberId) throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Turn off automatic saving to start our transactional group block
            conn.setAutoCommit(false);

            try {
                // Step A: Check if the target book is actually available
                String status = repository.getBookStatus(conn, bookId);
                if (status == null) {
                    throw new Exception("❌ System error: Book ID does not exist.");
                }
                if (!"AVAILABLE".equals(status)) {
                    throw new Exception("⚠️ Notice: Book is currently unavailable or checked out.");
                }

                // Step B: Mark book as 'ISSUED'
                repository.updateBookStatus(conn, bookId, "ISSUED");

                // Step C: Log transaction history entry (with a standard 14-day checkout span)
                LocalDate today = LocalDate.now();
                LocalDate dueDate = today.plusDays(14);
                repository.insertIssueRecord(conn, bookId, memberId, today, dueDate);

                // Commit: Permanently save all steps simultaneously to PostgreSQL
                conn.commit();
                System.out.println("🚀 Success: Transaction logged and book checked out successfully.");

            } catch (Exception e) {
                // Rollback: Undo every single step if even one error occurred
                conn.rollback();
                throw e; // Pass message upwards to be displayed in our UI dialog box
            }
        }
    }


    public double returnBook(int bookId) throws Exception {
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Step A: Find the active ledger tracking index for this asset item
                int issueId = repository.getActiveIssueId(conn, bookId);
                if (issueId == -1) {
                    throw new Exception("❌ System error: No active checkout tracking record found for this asset.");
                }

                // Step B: Read expected due date and calculate any pending overdue penalties
                LocalDate dueDate = repository.getDueDate(conn, issueId);
                LocalDate today = LocalDate.now();
                double calculatedFine = 0.0;

                long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                if (daysOverdue > 0) {
                    // Charges ₹5.00 (or $5.00) flat penalty for every single late day logged
                    calculatedFine = daysOverdue * 5.00;
                }

                // Step C: Close active history ledger card using current time stamp index metrics
                repository.finaliseReturnRecord(conn, issueId, today, calculatedFine);

                // Step D: Return physical status back to 'AVAILABLE' in general inventory listings
                repository.updateBookStatus(conn, bookId, "AVAILABLE");

                conn.commit();
                return calculatedFine; // Return fine amount to UI so it can alert the operator

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
