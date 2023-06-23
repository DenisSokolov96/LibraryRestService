package ru.dto.author;

import java.util.Objects;

public final class Author {
    private final String firstName;
    private final String middleName;
    private final String lastName;

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
    }

    private Author(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Author)
        {
            Author obj = (Author)other;
            return Objects.equals(obj.getFirstName(), this.getFirstName())
                    && Objects.equals(obj.getMiddleName(), this.getMiddleName())
                    && Objects.equals(obj.getLastName(), this.getLastName());
        }
        else
            return false;
    }

    public static class AuthorBuilder {

        private String firstName;
        private String middleName;
        private String lastName;
        private AuthorBuilder() {
        }

        public AuthorBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthorBuilder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public AuthorBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Author build() {
            return new Author(firstName, middleName, lastName);
        }
    }
}

