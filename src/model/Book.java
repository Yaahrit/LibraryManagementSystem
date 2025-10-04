package model;
public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private int year;
    private int copiesAvailable;

    public Book() {}

    public Book(int id, String title, String author, String publisher, String isbn, int year, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.year = year;
        this.copiesAvailable = copiesAvailable;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
