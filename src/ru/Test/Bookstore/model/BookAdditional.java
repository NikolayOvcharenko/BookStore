package ru.Test.Bookstore.model;

public class BookAdditional {
    public BookGenge getGenge() {
        return genge;
    }

    public void setGenge(BookGenge genge) {
        this.genge = genge;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private BookGenge genge;
    private int count;

    public BookAdditional(BookGenge genge, int count) {
        this.genge = genge;
        this.count = count;
    }
}
