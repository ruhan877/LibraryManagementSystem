package models;

import java.time.LocalDate;


public class IssueRecord {
    private int issueId;
    private int bookId;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // Remains null until the book is physically returned
    private double fineAmount;


    public IssueRecord(int issueId, int bookId, int memberId, LocalDate issueDate, LocalDate dueDate, LocalDate returnDate, double fineAmount) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
    }


    public int getIssueId() { return issueId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
}
