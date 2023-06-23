package ru.dto.reader;

public final class Reader {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String address;
    private final String passportSn;

    public static ReaderBuilder builder() {
        return new ReaderBuilder();
    }

    private Reader(String firstName, String middleName, String lastName, String address, String passportSn) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.address = address;
        this.passportSn = passportSn;
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

    public String getAddress() {
        return address;
    }

    public String getPassportSn() {
        return passportSn;
    }

    public static class ReaderBuilder {

        private String lastName;
        private String firstName;
        private String middleName;
        private String address;
        private String passportSn;

        private ReaderBuilder() {
        }

        public ReaderBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ReaderBuilder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public ReaderBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReaderBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ReaderBuilder passportSN(String passportSN) {
            this.passportSn = passportSN;
            return this;
        }

        public Reader build() {
            return new Reader(firstName, middleName, lastName, address, passportSn);
        }

    }
}
