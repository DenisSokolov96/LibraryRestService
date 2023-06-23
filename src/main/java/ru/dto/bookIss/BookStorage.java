package ru.dto.bookIss;

import ru.dto.author.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookStorage {

    private final List<BookIssued> storage = new ArrayList<>();

    public List<BookIssued> getBookStorage() {
        return storage;
    }

    public void addBook(BookIssued bookIssued) {
        this.storage.add(bookIssued);
    }
    public void addListBook(List<BookIssued> listBookIssued) {
        this.storage.addAll(listBookIssued);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof BookStorage)
        {
            BookStorage obj = (BookStorage)other;
            return Objects.equals(obj.getBookStorage(), this.getBookStorage());
        }
        else
            return false;
    }
}
