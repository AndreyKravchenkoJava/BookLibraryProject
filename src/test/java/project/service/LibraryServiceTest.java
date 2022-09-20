package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import project.dao.BookDAO;
import project.dao.LibraryDAO;
import project.dao.ReaderDAO;
import project.entity.Book;
import project.entity.Reader;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class LibraryServiceTest {
    private LibraryService libraryService;
    private LibraryDAO libraryDAO;
    private BookDAO bookDAO;
    private ReaderDAO readerDAO;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
        libraryDAO = mock(LibraryDAO.class);
        bookDAO = mock(BookDAO.class);
        readerDAO = mock(ReaderDAO.class);
        libraryService.setLibraryDAO(libraryDAO);
        libraryService.setBookDAO(bookDAO);
        libraryService.setReaderDAO(readerDAO);
    }

    @DisplayName("Test should successfully add new book")
    @Test
    void shouldSuccessfullyAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDAO, times(1)).save(captor.capture());
        Book bookToSave = captor.getValue();

        assertAll(
                () -> assertThat(createdBook).isNotNull(),
                () -> assertThat(expectedTitle).isEqualTo(createdBook.getTitle()),
                () -> assertThat(expectedAuthor).isEqualTo(createdBook.getAuthor()),
                () -> assertThat(createdBook).isEqualTo(bookToSave)
        );
    }

    @DisplayName("Test should fail to add new reader")
    @Test
    void shouldFailAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 1";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        verify(bookDAO, never()).save(any());

        assertThat(createdBook).isNull();

        //при проверке на exception выдает ошибку
    }

    @DisplayName("Test should successfully add new reader ")
    @Test
    void shouldSuccessfullyAddNewReader() {
        String userInput = "Alexander Singeev";
        Reader createdReader = libraryService.addReaders(userInput);
        System.out.println(createdReader);

        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDAO, times(1)).save(captor.capture());
        Reader readerToSave = captor.getValue();
        System.out.println(readerToSave);

        assertAll(
                () -> assertThat(createdReader).isNotNull(),
                () -> assertThat(userInput).isEqualTo(createdReader.getName()),
                () -> assertThat(createdReader).isEqualTo(readerToSave)
        );
    }

    @DisplayName("Test should fail to add new reader")
    @Test
    void shouldFailAddNewReader() {
        String userInput = "Alexander Singeev 1";
        Reader createdReader = libraryService.addReaders(userInput);

        verify(readerDAO, never()).save(any());

        assertThat(createdReader).isNull();

        //при проверке на exception выдает ошибку
    }

    @DisplayName("Test should successfully borrow book to reader")
    @Test
    void shouldSuccessfullyBorrowBookToReader() {
        int expectedBookId = 1;
        int expectedReaderId = 12;
        String userInput = expectedBookId + " / " + expectedReaderId;

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDAO.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);

        boolean flag = libraryService.borrowBook(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDAO, times(1)).borrowBookIdToReaderId(bookIdToSave, readerIdToSave),
                () -> assertThat(flag).isTrue(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)

        );
    }

    @DisplayName("Test should fail to borrow book to non existing reader")
    @Test
    void shouldFailBorrowBookToReader() {
        int expectedBookId = 20;
        int expectedReaderId = 20;
        String userInput = expectedBookId + " / " + expectedReaderId;

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDAO.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(false).thenAnswer(invocationOnMock -> {
            throw new SQLException();
        });

        boolean flag = libraryService.borrowBook(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDAO, times(1)).borrowBookIdToReaderId(bookIdToSave, readerIdToSave),
                () -> assertThat(flag).isFalse(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)

        );
    }

    @DisplayName("Test should successfully return book to the Library")
    @Test
    void shouldSuccessfullyReturnBookToLibrary() {
        int expectedBookId = 1;
        int expectedReaderId = 13;
        String userInput = expectedBookId + " / " + expectedReaderId;

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDAO.returnBookIdFromReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);

        boolean flag = libraryService.returnBookToLibrary(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDAO, times(1)).returnBookIdFromReaderId(bookIdToSave, readerIdToSave),
                () -> assertThat(flag).isTrue(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)
        );
    }

    @DisplayName("Test should fail return book to the Library")
    @Test
    void shouldFailReturnBookToLibrary() {
        int expectedBookId = 20;
        int expectedReaderId = 20;
        String userInput = expectedBookId + " / " + expectedReaderId;

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDAO.returnBookIdFromReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(false).thenThrow(new NoSuchElementException());

        boolean flag = libraryService.returnBookToLibrary(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDAO, times(1)).returnBookIdFromReaderId(bookIdToSave, readerIdToSave),
                () -> assertThat(flag).isFalse(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)
        );
    }
}