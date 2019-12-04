package ru.Test.Bookstore.model;

public class Book {
    private long id;
    private String title;
    private String author;
    private double price;
    private BookGenge genre;



    public Book(long id, String title, String author, double price, BookGenge genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BookGenge getGenre() {
        return genre;
    }

    public void setGenre(BookGenge genre) {
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
