package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import project.dao.*;
import project.entity.Book;
import project.entity.Reader;
import project.exception.JdbcDaoException;
import project.exception.LibraryServiceException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void shouldSuccessfullyToAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book bookToCreate = new Book(0, expectedTitle, expectedAuthor);


        when(bookDao.save(any())).thenReturn(bookToCreate);
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

    @DisplayName("Test should fail to add new book with empty input")
    @Test
    void shouldFailToAddNewBookWithEmptyInput() {
        String userInput = "";
        String expectedErrorMessage = "Failed to create new book: input cannot be empty";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new book without separated")
    @Test
    void shouldFailToAddNewBookWithoutSeparated() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 33333";
        String userInput = expectedTitle + " " + expectedAuthor;
        String expectedErrorMessage = "Failed to create new book: input should be separated ' / '";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new book with invalid author name")
    @Test
    void shouldFailToAddNewBookWithInvalidAuthorName() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 33333";
        String userInput = expectedTitle + " / " + expectedAuthor;
        String expectedErrorMessage = """
                         Failed to create new book: invalid author name!
                        
                         1. Name must contain only letters
                         2. Have more than two letters
                         3. Maximum number of letters 100
                        
                         Fro example 'Danyl Zanuk'""";


        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should trow JdbcException when Dao fails to add new book")
    @Test
    void shouldTrowJdbcExceptionWhenDaoFailsToAddNewBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford";
        String userInput = expectedTitle + " / " + expectedAuthor;
        String expectedErrorMessage = "Failed to fetch generated ID from DB while saving new book";

        when(bookDao.save(any())).thenThrow(new JdbcDaoException(expectedErrorMessage));

        Exception exception = assertThrows(JdbcDaoException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should successfully add new reader ")
    @Test
    void shouldSuccessfullyToAddNewReader() {
        String userInput = "Alexander Singeev";
        Reader readerToCreate = new Reader(0, userInput);

        when(readerDao.save(any())).thenReturn(readerToCreate);
        Reader createdReader = libraryService.addReader(userInput);
        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDao, times(1)).save(captor.capture());
        Reader readerToSave = captor.getValue();

        assertAll(
                () -> assertThat(createdReader).isNotNull(),
                () -> assertThat(userInput).isEqualTo(createdReader.getName()),
                () -> assertThat(createdReader).isEqualTo(readerToSave)
        );
    }

    @DisplayName("Test should fail to add new reader with empty input")
    @Test
    void shouldFailToAddNewReaderWithEmptyInput() {
        String userInput = "";
        String expectedErrorMessage = "Failed to create new reader: input cannot be empty";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.addReader(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new reader with invalid name")
    @Test
    void shouldFailToAddNewReaderWithInvalidName() {
        String userInput = "Alexander Singeev 1";
        String expectedErrorMessage = """                       
                        Failed to create new reader: invalid reader name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""";


        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.addReader(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should trow JdbcException when Dao fails to save new reader")
    @Test
    void shouldTrowJdbcExceptionWhenDaoFailsToSaveNewReader() {
        String userInput = "Alexander Singeev";
        String expectedErrorMessage = "Failed to fetch generated ID from DB while saving new reader";

        when(readerDao.save(any())).thenThrow(new JdbcDaoException(expectedErrorMessage));

        Exception exception = assertThrows(JdbcDaoException.class, () -> libraryService.addReader(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should successfully to borrow book to reader")
    @Test
    void shouldSuccessfullyToBorrowBookToReader() {
        int expectedBookId = 1;
        int expectedReaderId = 12;
        String userInput = expectedBookId + " / " + expectedReaderId;
        Book bookToCreate = new Book(1, "Java From Epam", "Igor Blinov");
        Reader readerToCreate = new Reader(12, "Yaroslav Kernitskiy");

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDao.borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);
        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));

        boolean flag = libraryService.borrowBook(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).borrowBookIdToReaderId(expectedBookId, expectedReaderId),
                () -> assertThat(flag).isTrue(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)
        );
    }

    @DisplayName("Test should fail to borrow book to reader with invalid input")
    @Test
    void shouldFailToBorrowBookWithInvalidInput() {
        String userInput = "a / b";
        String expectedErrorMessage = """                       
                        Failed to borrow book: invalid input!
                        
                        1. The input cannot be empty
                        2. Input should be separated ' / '
                        2. You must enter only numbers
                        3. The input must match the example

                        Fro example '50 / 50'""";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.borrowBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to borrow book to reader with if the book or reader does not exist")
    @Test
    void shouldFailToBorrowBookWithIfBookOrReaderDoesNotExist() {
        int expectedBookId = 100;
        int expectedReaderId = 100;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "Failed to return book: there is no such book or reader in the Library!";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.borrowBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should successfully to return book to the Library")
    @Test
    void shouldSuccessfullyToReturnBookToLibrary() {
        int expectedBookId = 2;
        int expectedReaderId = 13;
        String userInput = expectedBookId + " / " + expectedReaderId;
        Book bookToCreate = new Book(2, "The Art of Loving", "Erich Fromm");
        Reader readerToCreate = new Reader(13, "Oleg Nechiporenko");

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        when(libraryDao.returnByBookIdAndReaderId(bookIdCaptor.capture(), readerIdCaptor.capture())).thenReturn(true);
        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));

        boolean flag = libraryService.returnBookToLibrary(userInput);

        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();

        assertAll(
                () -> verify(libraryDao, times(1)).returnByBookIdAndReaderId(expectedBookId, expectedReaderId),
                () -> assertThat(flag).isTrue(),
                () -> assertThat(bookIdToSave).isEqualTo(expectedBookId),
                () -> assertThat(readerIdToSave).isEqualTo(expectedReaderId)
        );
    }

    @DisplayName("Test should fail to return book to the Library with invalid input")
    @Test
    void shouldFailToReturnBookToLibraryWithInvalidInput() {
        String userInput = "a / b";
        String expectedErrorMessage = """                       
                        Failed to return book: invalid input!
                        
                        1. The input cannot be empty
                        2. Input should be separated ' / '
                        2. You must enter only numbers
                        3. The input must match the example

                        Fro example '50 / 50'""";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to return book to library with if the book or reader does not exist")
    @Test
    void shouldFailToReturnBookToLibraryWithIfBookOrReaderDoesNotExist() {
        int expectedBookId = 100;
        int expectedReaderId = 100;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "Failed to return book: there is no such book or reader in the Library!";

        Exception exception = assertThrows(LibraryServiceException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to return book to library with no such borrowing exists")
    @Test
    void shouldFailToReturnBookToLibraryWithNoSuchBorrowingExists() {
        int expectedBookId = 5;
        int expectedReaderId = 13;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "Failed to return this book: no such borrowing exists";
        Book bookToCreate = new Book(5, "Change or die", "John Brandon");
        Reader readerToCreate = new Reader(13, "Oleg Nechiporenko");

        when(libraryDao.returnByBookIdAndReaderId(expectedBookId, expectedReaderId)).thenThrow(new JdbcDaoException(expectedErrorMessage));
        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));


        Exception exception = assertThrows(JdbcDaoException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }
}