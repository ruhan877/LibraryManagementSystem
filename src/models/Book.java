package models;

public class Book {

    private int bookId;
    private String title;
    private String status; // Can be 'AVAILABLE' or 'ISSUED'


    public Book(String title) {
        this.title = title;
        this.status = "AVAILABLE"; // Default state for a new book
    }

    public Book(int bookId, String title, String status) {
        this.bookId = bookId;
        this.title = title;
        this.status = status;
    }


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
