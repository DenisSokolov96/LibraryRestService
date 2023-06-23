package ru.dto.bookIss;

import ru.dto.author.Author;
import ru.dto.issued.Issued;

import java.util.Objects;

public final class BookIssued {
    private final String title;
    private final Author author;

    public static BookIssuedBuilder builder() {
        return new BookIssuedBuilder();
    }

    private BookIssued(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof BookIssued)
        {
            BookIssued obj = (BookIssued)other;
            return Objects.equals(obj.getTitle(), this.getTitle())
                    && Objects.equals(obj.getAuthor(), this.getAuthor());
        }
        else
            return false;
    }

    public static class BookIssuedBuilder {
        private String title;
        private Author author;

        private BookIssuedBuilder() {
        }

        public BookIssuedBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookIssuedBuilder author(Author author) {
            this.author = author;
            return this;
        }

        public BookIssued build(){
            return new BookIssued(title, author);
        }
    }

}
