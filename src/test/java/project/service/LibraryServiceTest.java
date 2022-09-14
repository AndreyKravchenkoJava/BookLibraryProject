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

    @DisplayName("Testing adding correct new book")
    @Test
    void addCorrectBookTest() {
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

    @DisplayName("Testing adding wrong new book")
    @Test
    void addWrongBookTest() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 1";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        verify(bookDAO, never()).save(any());

        assertThat(createdBook).isNull();

        //при проверке на exception выдает ошибку
    }

    @DisplayName("Testing adding correct new reader")
    @Test
    void addCorrectReadersTest() {
        String userInput = "Alexander Singeev";
        Reader createdReader = libraryService.addReaders(userInput);

        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDAO, times(1)).save(captor.capture());
        Reader readerToSave = captor.getValue();

        assertAll(
                () -> assertThat(createdReader).isNotNull(),
                () -> assertThat(userInput).isEqualTo(createdReader.getName()),
                () -> assertThat(createdReader).isEqualTo(readerToSave)
        );
    }

    @DisplayName("Testing adding wrong new reader")
    @Test
    void addWrongReadersTest() {
        String userInput = "Alexander Singeev 1";
        Reader createdReader = libraryService.addReaders(userInput);

        verify(readerDAO, never()).save(any());

        assertThat(createdReader).isNull();

        //при проверке на exception выдает ошибку
    }

    @DisplayName("Testing borrow book to reader by id")
    @Test
    void correctBorrowBookTest() throws SQLException {
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

    @DisplayName("Testing borrow book to reader by id")
    @Test
    void wrongBorrowBookTest() throws SQLException {
        int expectedBookId = 20;
        int expectedReaderId = 20;
        String userInput = expectedBookId + " / " + expectedReaderId;

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDAO.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(false).thenThrow(new SQLException());

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

    @DisplayName("Testing return book to reader by id")
    @Test
    void returnCorrectBookToLibraryTest() {
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

    @DisplayName("Testing return book to reader by id")
    @Test
    void returnWrongBookToLibraryTest() {
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