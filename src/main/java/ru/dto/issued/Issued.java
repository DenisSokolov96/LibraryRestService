package ru.dto.issued;

import ru.dto.author.Author;

import java.util.Objects;

public final class Issued {
    private final String isbn;
    private final String title;
    private final Author author;
    private final int publishedYear;
    private final String languageBook;
    private final int countBooks;

    public static IssuedBuilder builder() {
        return new IssuedBuilder();
    }

    private Issued(String isbn, String title, Author author, int publishedYear, String languageBook, int countBooks) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.languageBook = languageBook;
        this.countBooks = countBooks;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public String getLanguageBook() {
        return languageBook;
    }

    public int getCountBooks() {
        return countBooks;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Issued)
        {
            Issued obj = (Issued)other;
            return Objects.equals(obj.getIsbn(), this.getIsbn())
                    && Objects.equals(obj.getTitle(), this.getTitle())
                    && Objects.equals(obj.getAuthor(), this.getAuthor())
                    && Objects.equals(obj.getPublishedYear(), this.getPublishedYear())
                    && Objects.equals(obj.getLanguageBook(), this.getLanguageBook())
                    && Objects.equals(obj.getCountBooks(), this.getCountBooks());
        }
        else
            return false;
    }

    public static class IssuedBuilder {
        private String isbn;
        private String title;
        private Author author;
        private int publishedYear;
        private String languageBook;
        private int countBooks;

        private IssuedBuilder() {
        }

        public IssuedBuilder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public IssuedBuilder title(String title) {
            this.title = title;
            return this;
        }

        public IssuedBuilder author(Author author) {
            this.author = author;
            return this;
        }

        public IssuedBuilder publishedYear(int publishedYear) {
            this.publishedYear = publishedYear;
            return this;
        }

        public IssuedBuilder languageBook(String languageBook) {
            this.languageBook = languageBook;
            return this;
        }

        public IssuedBuilder countBooks(int countBooks) {
            this.countBooks = countBooks;
            return this;
        }

        public Issued build() {
            return new Issued(isbn, title, author, publishedYear, languageBook, countBooks);
        }
    }
}
