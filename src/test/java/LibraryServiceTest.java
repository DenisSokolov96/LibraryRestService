import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import context.Context;
import ru.dto.author.Author;
import ru.dto.bookIss.BookIssued;
import ru.dto.bookIss.BookStorage;
import ru.dto.issued.Issued;
import ru.dto.issued.IssuedStorage;
import ru.libraryService.LibraryService;

import static org.assertj.core.api.Assertions.*;

public class LibraryServiceTest {
    private static LibraryService libraryService;

    @BeforeAll
    public static void initService(){
        libraryService =  new Context().initLibraryService();
    }

    /**
     * Список книг, которые "на руках" у заданного читателя
     */
    @Test
    public void getListBooksOnHands() {
        BookStorage bookStorage = libraryService.getIssuedBooks("Илья", "Евгеньевич", "Ширяев");
        BookStorage bookStorageTest = new BookStorage();
        bookStorageTest.addBook(
                BookIssued.builder().title("Вий")
                        .author(
                                Author.builder()
                                        .firstName("Николай")
                                        .lastName("Гоголь")
                                        .middleName("Васильевич")
                                        .build())
                        .build());
        bookStorageTest.addBook(
                BookIssued.builder().title("Дубровский")
                        .author(
                                Author.builder()
                                        .firstName("Александр")
                                        .lastName("Пушкин")
                                        .middleName("Сергеевич")
                                        .build())
                        .build());
        assertThat(bookStorageTest)
                .isEqualTo(bookStorage);
    }

    /**
     * Издания которые разобраны читателями (все книги издания "на руках")
     */
    @Test
    public void getListIssuedOnHandsAll() {
        IssuedStorage issuedStorage = libraryService.getIssuedBooksAll();
        IssuedStorage issuedStorageTest = new IssuedStorage();
        issuedStorageTest.addIss(
                Issued.builder().isbn("978-7-00054-181-6")
                        .title("Дубровский")
                        .languageBook("ru")
                        .publishedYear(1832)
                        .countBooks(3)
                        .author(
                                Author.builder()
                                        .firstName("Александр")
                                        .middleName("Сергеевич")
                                        .lastName("Пушкин")
                                        .build())
                        .build());
        issuedStorageTest.addIss(
                Issued.builder().isbn("978-7-00054-183-6")
                        .title("Вий")
                        .languageBook("ru")
                        .publishedYear(1835)
                        .countBooks(2)
                        .author(
                                Author.builder()
                                        .firstName("Николай")
                                        .middleName("Васильевич")
                                        .lastName("Гоголь")
                                        .build())
                        .build());
        assertThat(issuedStorageTest)
                .isEqualTo(issuedStorage);
    }


    /**
     * получать количество оставшихся книг в библиотеке указанного издания
     */
    @Test
    public void getCountBooksByISBN() {
        int k = libraryService.getRemainingBooks("978-7-00054-185-6");
        assertThat(2)
                .isEqualTo(k);
    }
}
