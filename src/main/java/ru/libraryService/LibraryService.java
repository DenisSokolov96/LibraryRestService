package ru.libraryService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.*;
import ru.datasource.DataSourceProvider;
import ru.dto.author.Author;
import ru.dto.bookIss.BookIssued;
import ru.dto.bookIss.BookStorage;
import ru.dto.issued.Issued;
import ru.dto.issued.IssuedStorage;
import ru.dto.reader.Reader;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.logging.Logger;

import static jakarta.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.jooq.impl.DSL.*;
import static ru.jooq.Tables.*;

@RequestScoped
public class LibraryService {
    private final Logger logger = Logger.getLogger(LibraryService.class.getName());
    private DataSource ds;
    private DSLContext dsl;

    @Inject
    public LibraryService(DataSourceProvider ds) {
        this.ds = ds.provide();
        dsl = DSL.using(this.ds, ds.sqlDialect());
        logger.info("Start LibraryService...");
    }

    public LibraryService() {
    }

    public BookStorage getIssuedBooks(String firstName, String middleName, String lastName) {
        BookStorage bookStorage = new BookStorage();
        bookStorage.addListBook(
                dsl.select(BOOKS_EDITIONS.TITLE, AUTHORS.FIRST_NAME, AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .from(READERS)
                        .leftJoin(ISSUED_BOOKS)
                        .on(READERS.LAST_NAME.eq(lastName))
                        .and(READERS.FIRST_NAME.eq(firstName))
                        .and(READERS.MIDDLE_NAME.eq(middleName))
                        .leftJoin(BOOKS_EDITIONS)
                        .on(BOOKS_EDITIONS.BOOK_ID.eq(ISSUED_BOOKS.EDITION))
                        .leftJoin(AUTHORS)
                        .on(BOOKS_EDITIONS.AUTHOR.eq(AUTHORS.AUTHOR_ID))
                        .where(READERS.READER_ID.eq(ISSUED_BOOKS.READER))
                        .fetch()
                        .map(r -> BookIssued.builder()
                                .title(r.original(BOOKS_EDITIONS.TITLE))
                                .author(
                                        Author.builder()
                                                .firstName(r.original(AUTHORS.FIRST_NAME))
                                                .middleName(r.original(AUTHORS.MIDDLE_NAME))
                                                .lastName(r.original(AUTHORS.LAST_NAME))
                                                .build()
                                )
                                .build()
                        )
        );
        logger.info("getIssuedBooks...");
        return bookStorage;
    }

    public IssuedStorage getIssuedBooksAll() {
        IssuedStorage issuedStorage = new IssuedStorage();
        issuedStorage.addListIssue(
                dsl.select(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.TITLE, BOOKS_EDITIONS.PUBLISHED_YEAR,
                                BOOKS_EDITIONS.LANGUAGE_BOOK, BOOKS_EDITIONS.COUNT_BOOKS, AUTHORS.FIRST_NAME,
                                AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .from(ISSUED_BOOKS)
                        .leftJoin(BOOKS_EDITIONS)
                        .on(BOOKS_EDITIONS.BOOK_ID.eq(ISSUED_BOOKS.EDITION))
                        .leftJoin(AUTHORS)
                        .on(AUTHORS.AUTHOR_ID.eq(BOOKS_EDITIONS.AUTHOR))
                        .groupBy(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.COUNT_BOOKS, BOOKS_EDITIONS.TITLE,
                                BOOKS_EDITIONS.PUBLISHED_YEAR, BOOKS_EDITIONS.LANGUAGE_BOOK, AUTHORS.FIRST_NAME,
                                AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .having(count(ISSUED_BOOKS.ISSUED_ID).ge(BOOKS_EDITIONS.COUNT_BOOKS))
                        .fetch()
                        .map(r -> Issued.builder()
                                .isbn(r.original(BOOKS_EDITIONS.ISBN))
                                .title(r.original(BOOKS_EDITIONS.TITLE))
                                .publishedYear(r.original(BOOKS_EDITIONS.PUBLISHED_YEAR))
                                .languageBook(r.original(BOOKS_EDITIONS.LANGUAGE_BOOK))
                                .countBooks(r.original(BOOKS_EDITIONS.COUNT_BOOKS))
                                .author(
                                        Author.builder()
                                                .firstName(r.original(AUTHORS.FIRST_NAME))
                                                .middleName(r.original(AUTHORS.MIDDLE_NAME))
                                                .lastName(r.original(AUTHORS.LAST_NAME))
                                                .build()
                                )
                                .build())
        );
        logger.info("getIssuedBooksAll...");
        return issuedStorage;
    }

    public int getRemainingBooks(String isbn) {
        logger.info("getRemainingBooks...");
        Optional<Integer> k = dsl.select((BOOKS_EDITIONS.COUNT_BOOKS.minus(count(ISSUED_BOOKS.ISSUED_ID))).as("count_books"))
                .from(ISSUED_BOOKS)
                .rightJoin(BOOKS_EDITIONS)
                .on(BOOKS_EDITIONS.BOOK_ID.eq(ISSUED_BOOKS.EDITION))
                .leftJoin(AUTHORS)
                .on(AUTHORS.AUTHOR_ID.eq(BOOKS_EDITIONS.AUTHOR))
                .where(BOOKS_EDITIONS.ISBN.eq(isbn))
                .groupBy(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.COUNT_BOOKS, BOOKS_EDITIONS.TITLE,
                        BOOKS_EDITIONS.PUBLISHED_YEAR, BOOKS_EDITIONS.LANGUAGE_BOOK, AUTHORS.FIRST_NAME,
                        AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                .fetchOptional()
                .map(r -> r.original(BOOKS_EDITIONS.COUNT_BOOKS));
        return k.orElse(0);
    }

    public IssuedStorage getPopularIss() {
        IssuedStorage issuedStorage = new IssuedStorage();
        issuedStorage.addListIssue(
                dsl.select(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.TITLE, BOOKS_EDITIONS.PUBLISHED_YEAR,
                                BOOKS_EDITIONS.LANGUAGE_BOOK, BOOKS_EDITIONS.COUNT_BOOKS, AUTHORS.FIRST_NAME,
                                AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .from(ISSUED_BOOKS)
                        .leftJoin(BOOKS_EDITIONS)
                        .on(BOOKS_EDITIONS.BOOK_ID.eq(ISSUED_BOOKS.EDITION))
                        .leftJoin(AUTHORS)
                        .on(AUTHORS.AUTHOR_ID.eq(BOOKS_EDITIONS.AUTHOR))
                        .groupBy(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.COUNT_BOOKS, BOOKS_EDITIONS.TITLE,
                                BOOKS_EDITIONS.PUBLISHED_YEAR, BOOKS_EDITIONS.LANGUAGE_BOOK, AUTHORS.FIRST_NAME,
                                AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .orderBy(
                                round(count(ISSUED_BOOKS.ISSUED_ID).div((BOOKS_EDITIONS.COUNT_BOOKS.pow(2))),
                                        2).desc())
                        .fetch()
                        .map(r -> Issued.builder()
                                .isbn(r.original(BOOKS_EDITIONS.ISBN))
                                .title(r.original(BOOKS_EDITIONS.TITLE))
                                .publishedYear(r.original(BOOKS_EDITIONS.PUBLISHED_YEAR))
                                .languageBook(r.original(BOOKS_EDITIONS.LANGUAGE_BOOK))
                                .countBooks(r.original(BOOKS_EDITIONS.COUNT_BOOKS))
                                .author(
                                        Author.builder()
                                                .firstName(r.original(AUTHORS.FIRST_NAME))
                                                .middleName(r.original(AUTHORS.MIDDLE_NAME))
                                                .lastName(r.original(AUTHORS.LAST_NAME))
                                                .build()
                                )
                                .build())
        );
        logger.info("getPopularIss...");
        return issuedStorage;
    }

    public BookStorage getBooksSortBySpeedReading() {
        BookStorage bookStorage = new BookStorage();
        bookStorage.addListBook(
                dsl.select(BOOKS_EDITIONS.TITLE, AUTHORS.FIRST_NAME, AUTHORS.MIDDLE_NAME, AUTHORS.LAST_NAME)
                        .from(BOOKS_EDITIONS)
                        .join(ISSUED_BOOKS)
                        .on(ISSUED_BOOKS.EDITION.eq(BOOKS_EDITIONS.BOOK_ID))
                        .leftJoin(AUTHORS)
                        .on(BOOKS_EDITIONS.AUTHOR.eq(AUTHORS.AUTHOR_ID))
                        .groupBy(BOOKS_EDITIONS.ISBN, BOOKS_EDITIONS.TITLE, AUTHORS.FIRST_NAME, AUTHORS.MIDDLE_NAME,
                                AUTHORS.LAST_NAME)
                        .orderBy(avg(dateDiff(currentDate(), ISSUED_BOOKS.ISSUE_DATE)).desc())
                        .fetch()
                        .map(r -> BookIssued.builder()
                                .title(r.original(BOOKS_EDITIONS.TITLE))
                                .author(
                                        Author.builder()
                                                .firstName(r.original(AUTHORS.FIRST_NAME))
                                                .middleName(r.original(AUTHORS.MIDDLE_NAME))
                                                .lastName(r.original(AUTHORS.LAST_NAME))
                                                .build()
                                )
                                .build())
        );
        logger.info("getBooksSortBySpeedReading...");
        return bookStorage;
    }

    public String addReader(String first_name, String middle_name, String last_name, String address, String passport_sn) {
        Reader reader = Reader.builder()
                .firstName(first_name)
                .middleName(middle_name)
                .lastName(last_name)
                .address(address)
                .passportSN(passport_sn)
                .build();
        logger.info("addReader");
        return "add reader (id): " + dsl.insertInto(READERS)
                .set(dsl.newRecord(READERS, reader))
                .returning(READERS.READER_ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting entity" + READERS.READER_ID))
                .get(READERS.READER_ID);
    }

    public String addAuthor(String firstName, String middleName, String lastName) {
        Author author = Author.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .build();
        logger.info("addAuthor");
        return "add author (id): " + dsl.insertInto(AUTHORS)
                .set(dsl.newRecord(AUTHORS, author))
                .returning(AUTHORS.AUTHOR_ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting entity" + AUTHORS.AUTHOR_ID))
                .get(AUTHORS.AUTHOR_ID);
    }

    public String delReader(String passport_sn) {
        logger.info("delReader");
        return "count del (id): " + dsl.delete(READERS)
                .where(READERS.PASSPORT_SN.eq(passport_sn))
                .execute();
    }

    public String addIssue(String isbn, String title, int author, int year,
                           String language_book, int count_books) {
        logger.info("addIssue");
        return "add issue (id): " + dsl.insertInto(BOOKS_EDITIONS)
                .set(BOOKS_EDITIONS.ISBN, isbn)
                .set(BOOKS_EDITIONS.TITLE, title)
                .set(BOOKS_EDITIONS.AUTHOR, author)
                .set(BOOKS_EDITIONS.PUBLISHED_YEAR, year)
                .set(BOOKS_EDITIONS.LANGUAGE_BOOK, language_book)
                .set(BOOKS_EDITIONS.COUNT_BOOKS, count_books)
                .returning(BOOKS_EDITIONS.BOOK_ID)
                .fetchOptional()
                .orElseThrow(() -> new DataAccessException("Error inserting entity" + BOOKS_EDITIONS.BOOK_ID))
                .get(BOOKS_EDITIONS.BOOK_ID);
    }

    @Transactional(NOT_SUPPORTED)
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(ds)
                .load();
        flyway.migrate();
        flyway.info();
    }

}
