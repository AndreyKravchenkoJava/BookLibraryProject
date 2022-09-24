package project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import project.dao.BookDao;
import project.dao.BookDaoPostgresqlImpl;
import project.dao.LibraryDao;
import project.dao.LibraryDaoPostgresqlImpl;
import project.dao.ReaderDao;
import project.dao.ReaderDaoPostgresqlImpl;
import project.entity.Book;
import project.entity.Reader;
import project.exception.JdbcDaoException;

class LibraryServiceTest {
    private LibraryService libraryService;
    private LibraryDao libraryDao;
    private BookDao bookDao;
    private ReaderDao readerDao;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
        libraryDao = mock(LibraryDaoPostgresqlImpl.class);
        bookDao = mock(BookDaoPostgresqlImpl.class);
        readerDao = mock(ReaderDaoPostgresqlImpl.class);
        libraryService.setLibraryDao(libraryDao);
        libraryService.setBookDao(bookDao);
        libraryService.setReaderDao(readerDao);
    }

    @DisplayName("Test should successfully add new book")
    @Test
    void shouldSuccessfullyAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(1)).save(captor.capture());
        Book bookToSave = captor.getValue();

        assertAll(
                () -> assertThat(createdBook).isNotNull(),
                () -> assertThat(expectedTitle).isEqualTo(createdBook.getTitle()),
                () -> assertThat(expectedAuthor).isEqualTo(createdBook.getAuthor()),
                () -> assertThat(createdBook).isEqualTo(bookToSave)
        );
    }

    @Test
    void shouldFailToCreateNewBookWithInvalidAuthorName() {
        var expectedErrorMessage = """
                        Failed to create new book: invalid author name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""";
        var expectedTitle = "My life, my achievements";
        var expectedAuthor = "Henry Ford 3";
        var userInput = expectedTitle + " / " + expectedAuthor;

        var exception = assertThrows(
            IllegalArgumentException.class, () -> libraryService.addBook(userInput));
        assertEquals(expectedErrorMessage, exception.getLocalizedMessage());
    }

    @Test
    void shouldTrowJdbcExceptionWhenDaoFailsToSaveNewBook() {
        var expectedErrorMessage = "some DB error message";
        var expectedTitle = "My life, my achievements";
        var expectedAuthor = "Henry Ford";
        var userInput = expectedTitle + " / " + expectedAuthor;

        when(bookDao.save(any())).thenThrow(new JdbcDaoException(expectedErrorMessage));

        var exception = assertThrows(
            JdbcDaoException.class, () -> libraryService.addBook(userInput));
        assertEquals(expectedErrorMessage, exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new reader")
    @Test
    void shouldFailAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 1";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        verify(bookDao, never()).save(any());

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
        verify(readerDao, times(1)).save(captor.capture());
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

        verify(readerDao, never()).save(any());

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
        when(libraryDao.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);

        boolean flag = libraryService.borrowBook(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).borrowBookIdToReaderId(bookIdToSave, readerIdToSave),
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
        when(libraryDao.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(false).thenAnswer(invocationOnMock -> {
            throw new SQLException();
        });

        boolean flag = libraryService.borrowBook(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).borrowBookIdToReaderId(bookIdToSave, readerIdToSave),
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
        when(libraryDao.returnBookIdFromReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);

        boolean flag = libraryService.returnBookToLibrary(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).returnBookIdFromReaderId(bookIdToSave, readerIdToSave),
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
        when(libraryDao.returnBookIdFromReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(false).thenThrow(new NoSuchElementException());

        boolean flag = libraryService.returnBookToLibrary(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).returnBookIdFromReaderId(bookIdToSave, readerIdToSave),
                () -> assertThat(flag).isFalse(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)
        );
    }
}