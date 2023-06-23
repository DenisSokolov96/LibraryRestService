package ru.restService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.dto.bookIss.BookStorage;
import ru.dto.issued.IssuedStorage;
import ru.libraryService.LibraryService;


import java.util.logging.Logger;

@Path("/Library")
@ApplicationScoped
public class RestService {
    private final Logger logger = Logger.getLogger(RestService.class.getName());

    @Inject
    private LibraryService libraryService;

    @PostConstruct
    public void init(){
        logger.info("RestService startup...");
    }

    // №2 Список книг, которые "на руках" у заданного читателя
    @POST
    @Path("issuedBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public BookStorage issuedBooks(
            @FormParam("first_name") String firstName,
            @FormParam("middle_name") String middleName,
            @FormParam("last_name") String lastName
    ) {
        logger.info("Запрос: №2 Список книг, которые \"на руках\" у заданного читателя.");
        return libraryService.getIssuedBooks(firstName, middleName, lastName);
    }

    // №3 Издания которые разобраны читателями (все книги издания "на руках")
    @POST
    @Path("issuedBooksAll")
    @Produces(MediaType.APPLICATION_JSON)
    public IssuedStorage issuedBooksAll() {
        logger.info("Запрос: №3 Издания которые разобраны читателями (все книги издания \"на руках\")");
        return libraryService.getIssuedBooksAll();
    }

    // №4.1 добавление читателя
    @PUT
    @Path("addReader")
    public String addReader(
            @FormParam("first_name") String first_name,
            @FormParam("middle_name") String middle_name,
            @FormParam("last_name") String last_name,
            @FormParam("address") String address,
            @FormParam("passport_sn") String passport_sn
    ) {
        return libraryService.addReader(first_name, middle_name, last_name, address, passport_sn);
    }

    // №4.2 добавление автора
    @PUT
    @Path("addAuthor")
    public String addAuthor(
            @FormParam("first_name") String firstName,
            @FormParam("middle_name") String middleName,
            @FormParam("last_name") String lastName
    ) {
        return libraryService.addAuthor(firstName, middleName, lastName);
    }

    // №4.3 добавление издания
    @PUT
    @Path("addIssue")
    public String addIssue(
            @FormParam("isbn") String isbn,
            @FormParam("title") String title,
            @FormParam("author") int author,
            @FormParam("published_year") int year,
            @FormParam("language_book") String language_book,
            @FormParam("count_books") int count_books
    ) {
        return libraryService.addIssue(isbn, title, author, year, language_book, count_books);
    }

    // №4.4 удаление читателя
    @DELETE
    @Path("delReader")
    public String delReader(@FormParam("passport_sn") String passport_sn) {
        return libraryService.delReader(passport_sn);
    }

    // №5 получать количество оставшихся книг в библиотеке указанного издания
    @POST
    @Path("countBooksByISBN")
    @Produces(MediaType.APPLICATION_JSON)
    public int countBooksByISBN(@FormParam("isbn") String isbn) {
        logger.info("Запрос: №5 получать количество оставшихся книг в библиотеке указанного издания");
        return libraryService.getRemainingBooks(isbn);
    }

    // №6 Список изданий отсортированных по популярности в настоящий момент
    @POST
    @Path("popularBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public IssuedStorage popularIss() {
        logger.info("Запрос: №6 Список изданий отсортированных по популярности в настоящий момент");
        return libraryService.getPopularIss();
    }

    // №7 Список книг отсортированный по "скорости чтения" (пользования)
    @POST
    @Path("speedReading")
    @Produces(MediaType.APPLICATION_JSON)
    public BookStorage speedReading() {
        logger.info("Запрос:  №7 Список книг отсортированный по \"скорости чтения\" (пользования)");
        return libraryService.getBooksSortBySpeedReading();
    }
}
