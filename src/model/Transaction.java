package model;


import java.time.LocalDate;

public class Transaction {
    private int id;
    private int memberId;
    private int bookId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status; // "ISSUED", "RETURNED", "OVERDUE"

    public Transaction() {}

    public Transaction(int id, int memberId, int bookId, LocalDate issueDate, LocalDate returnDate, String status) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Transaction #" + id + " [Book=" + bookId + ", Member=" + memberId + ", Status=" + status + "]";
    }
}
