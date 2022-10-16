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
import project.exception.ValidatorServiceException;

import java.util.*;

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

    @DisplayName("Test should successfully to show books in library")
    @Test
    void shouldSuccessfullyToShowBooks() {
        List<Book> booksListToCreate = List.of(new Book(1, "My life, my achievements", "Henry Ford"));

        when(bookDao.findAll()).thenReturn(booksListToCreate);
        libraryService.showBooks();

        assertThat(verify(bookDao, times(2)).findAll());
    }

    @DisplayName("Test should fail to show books with no books in the library")
    @Test
    void shouldFailToShowBooksWithEmptyList() {
        List<Book> booksListToCreate = List.of();

        when(bookDao.findAll()).thenReturn(booksListToCreate);
        libraryService.showBooks();

        assertThat(verify(bookDao, times(1)).findAll());
    }

    @DisplayName("Test should successfully to show readers in library")
    @Test
    void shouldSuccessfullyToShowReaders() {
        List<Reader> readersListToCreate = List.of(new Reader(1, "Alexander Singeev"));

        when(readerDao.findAll()).thenReturn(readersListToCreate);
        libraryService.showReaders();

        assertThat(verify(readerDao, times(2)).findAll());
    }

    @DisplayName("Test should fail to show reader with no readers in the library")
    @Test
    void shouldFailToShowReadersWithEmptyList() {
        List<Reader> readersListToCreate = List.of();

        when(readerDao.findAll()).thenReturn(readersListToCreate);
        libraryService.showReaders();

        assertThat(verify(readerDao, times(1)).findAll());
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
        String expectedErrorMessage = "input cannot be empty!";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new book without separated")
    @Test
    void shouldFailToAddNewBookWithoutSeparated() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 33333";
        String userInput = expectedTitle + " " + expectedAuthor;
        String expectedErrorMessage = "input should be separated ' / '!";

        var exception = assertThrows(LibraryServiceException.class, () -> libraryService.addBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new book with invalid author name")
    @Test
    void shouldFailToAddNewBookWithInvalidAuthorName() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 33333";
        String userInput = expectedTitle + " / " + expectedAuthor;
        String expectedErrorMessage = """
                invalid name!
                                        
                1. Name must contain only letters
                2. Have more than two letters
                3. Maximum number of letters 100
                                        
                Fro example 'Danyl Zanuk'""";


        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.addBook(userInput));

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

        var exception = assertThrows(JdbcDaoException.class, () -> libraryService.addBook(userInput));

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
        String expectedErrorMessage = "input cannot be empty!";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.addReader(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to add new reader with invalid name")
    @Test
    void shouldFailToAddNewReaderWithInvalidName() {
        String userInput = "Alexander Singeev 1";
        String expectedErrorMessage = """                       
                invalid name!
                                        
                1. Name must contain only letters
                2. Have more than two letters
                3. Maximum number of letters 100
                                        
                Fro example 'Danyl Zanuk'""";


        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.addReader(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should throw JdbcException when Dao fails to save new reader")
    @Test
    void shouldTrowJdbcExceptionWhenDaoFailsToSaveNewReader() {
        String userInput = "Alexander Singeev";
        String expectedErrorMessage = "Failed to fetch generated ID from DB while saving new reader";

        when(readerDao.save(any())).thenThrow(new JdbcDaoException(expectedErrorMessage));

        var exception = assertThrows(JdbcDaoException.class, () -> libraryService.addReader(userInput));

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
                invalid input!
                                        
                1. The input cannot be empty
                2. Input should be separated ' / '
                2. You must enter only numbers
                3. The input must match the example

                Fro example '50 / 50'""";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.borrowBook(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to borrow book to reader with if the book or reader does not exist")
    @Test
    void shouldFailToBorrowBookWithIfBookOrReaderDoesNotExist() {
        int expectedBookId = 100;
        int expectedReaderId = 100;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "there is no such book or reader in the Library!";

        var exception = assertThrows(LibraryServiceException.class, () -> libraryService.borrowBook(userInput));

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
                invalid input!
                                        
                1. The input cannot be empty
                2. Input should be separated ' / '
                2. You must enter only numbers
                3. The input must match the example

                Fro example '50 / 50'""";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to return book to library with if the book or reader does not exist")
    @Test
    void shouldFailToReturnBookToLibraryWithIfBookOrReaderDoesNotExist() {
        int expectedBookId = 100;
        int expectedReaderId = 100;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "there is no such book or reader in the Library!";

        var exception = assertThrows(LibraryServiceException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to return book to library with no such borrowing exists")
    @Test
    void shouldFailToReturnBookToLibraryWithNoSuchBorrowingExists() {
        int expectedBookId = 5;
        int expectedReaderId = 13;
        String userInput = expectedBookId + " / " + expectedReaderId;
        String expectedErrorMessage = "no such borrowing exists";
        Book bookToCreate = new Book(5, "Change or die", "John Brandon");
        Reader readerToCreate = new Reader(13, "Oleg Nechiporenko");

        when(libraryDao.returnByBookIdAndReaderId(expectedBookId, expectedReaderId)).thenThrow(new JdbcDaoException(expectedErrorMessage));
        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));

        var exception = assertThrows(JdbcDaoException.class, () -> libraryService.returnBookToLibrary(userInput));

        assertThat(expectedErrorMessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should successfully to show all borrowed books by reader")
    @Test
    void shouldSuccessfullyToShowAllBorrowedBooksByReader() {
        int expectedReaderId = 1;
        String userInput = "1";
        Book bookToCreate = new Book(1, "My life, my achievements", "Henry Ford");
        List<Book> booksListToCreate = List.of(bookToCreate);
        Reader readerToCreate = new Reader(1, "Alexander Singeev");

        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));
        when(libraryDao.findAllBorrowedBooksByReaderId(expectedReaderId)).thenReturn(booksListToCreate);
        libraryService.showAllBorrowedBooksByReader(userInput);

        assertAll(
                () -> verify(readerDao, times(1)).findById(expectedReaderId),
                () -> verify(libraryDao, times(2)).findAllBorrowedBooksByReaderId(expectedReaderId)
        );
    }

    @DisplayName("Test should fail to show all borrowed books by reader with invalid input")
    @Test
    void shouldFailToShowAllBorrowedBooksByReaderWithInvalidInput() {
        String userInput = "a";
        String expectedErrormessage = """                       
                invalid input!
                                        
                1. The input cannot be empty
                2. You must enter only number
                3. The input must match the example

                Fro example '5'""";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.showAllBorrowedBooksByReader(userInput));

        assertThat(expectedErrormessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to show all borrowed books by reader with reader does not exist")
    @Test
    void shouldFailToShowAllBorrowedBooksByReaderWithReaderDoesNotExist() {
        String userInput = "100";
        String expectedErrormessage = "there is no such reader in the Library";

        var exception = assertThrows(LibraryServiceException.class, () -> libraryService.showAllBorrowedBooksByReader(userInput));

        assertThat(expectedErrormessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to show all borrowed books by reader with if reader did not borrow books")
    @Test
    void shouldFailToShowAllBorrowedBooksByReaderWithIfReaderDidNotBorrowBooks() {
        int expectedReaderId = 1;
        String userInput = "1";
        List<Book> booksListToCreate = List.of();
        Reader readerToCreate = new Reader(1, "Alexander Singeev");

        when(readerDao.findById(expectedReaderId)).thenReturn(Optional.of(readerToCreate));
        when(libraryDao.findAllBorrowedBooksByReaderId(expectedReaderId)).thenReturn(booksListToCreate);
        libraryService.showAllBorrowedBooksByReader(userInput);

        assertThat(verify(libraryDao, times(1)).findAllBorrowedBooksByReaderId(expectedReaderId));

    }

    @DisplayName("Test should successfully to show readers by current book")
    @Test
    void shouldSuccessfullyToShowAllReadersByCurrentBook() {
        int expectedBookId = 1;
        String userInput = "1";
        Reader readerToCreate = new Reader(1, "Alexander Singeev");
        List<Reader> readerListToSave = List.of(readerToCreate);
        Book bookToCreate = new Book(1, "My life, my achievements", "Henry Ford");

        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(libraryDao.findAllReadersByBookId(expectedBookId)).thenReturn(readerListToSave);
        libraryService.showAllReadersByCurrentBook(userInput);

        assertAll(
                () -> verify(bookDao, times(1)).findById(expectedBookId),
                () -> verify(libraryDao, times(2)).findAllReadersByBookId(expectedBookId)
        );
    }

    @DisplayName("Test should fail to show all readers by current book with invalid input")
    @Test
    void shouldFailToShowAllReadersByCurrentBookWithInvalidInput() {
        String userInput = "a";
        String expectedErrormessage = """                       
                invalid input!
                                        
                1. The input cannot be empty
                2. You must enter only number
                3. The input must match the example

                Fro example '5'""";

        var exception = assertThrows(ValidatorServiceException.class, () -> libraryService.showAllReadersByCurrentBook(userInput));

        assertThat(expectedErrormessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to show all readers by current book with book does not exist")
    @Test
    void shouldFailToShowAllReadersByCurrentBookWithBookDoesNotExist() {
        String userInput = "100";
        String expectedErrormessage = "there is no such book in the Library";

        var exception = assertThrows(LibraryServiceException.class, () -> libraryService.showAllReadersByCurrentBook(userInput));

        assertThat(expectedErrormessage).isEqualTo(exception.getLocalizedMessage());
    }

    @DisplayName("Test should fail to show all readers by current book with if was not borrowed by readers")
    @Test
    void shouldFailToShowAllReadersByCurrentBookWithIfWasNotBorrowedByReaders() {
        int expectedBookId = 1;
        String userInput = "1";
        List<Reader> readerListToCreate = List.of();
        Book bookToCreate = new Book(1, "My life, my achievements", "Henry Ford");

        when(bookDao.findById(expectedBookId)).thenReturn(Optional.of(bookToCreate));
        when(libraryDao.findAllReadersByBookId(expectedBookId)).thenReturn(readerListToCreate);
        libraryService.showAllReadersByCurrentBook(userInput);

        assertThat(verify(libraryDao, times(1)).findAllReadersByBookId(expectedBookId));
    }

    @DisplayName("Test should successfully to show all readers and borrowed books")
    @Test
    void shouldSuccessfullyToShowAllReadersAndBorrowedBooks() {
        Map<Reader, List<Book>> readerBooksMapToCreate = new HashMap<>();
        List<Book> bookList = new ArrayList<>();
        Reader readerToCreate = new Reader(1, "Alexander Singeev");
        Book bookToCreate = new Book(1, "My life, my achievements", "Henry Ford");
        bookList.add(bookToCreate);
        readerBooksMapToCreate.put(readerToCreate, bookList);

        when(libraryDao.findAllReadersAndBorrowedBooks()).thenReturn(readerBooksMapToCreate);
        libraryService.showAllReadersAndBorrowedBooks();

        assertThat(verify(libraryDao, times(2)).findAllReadersAndBorrowedBooks());

    }

    @DisplayName("Test should fail to show all readers and borrowed books with if not one reader borrowed a books")
    @Test
    void shouldFailToShowAllReadersAndBorrowedBooksWithEmptyMap() {
        Map<Reader, List<Book>> readerBooksMapToCreate = new HashMap<>();

        when(libraryDao.findAllReadersAndBorrowedBooks()).thenReturn(readerBooksMapToCreate);
        libraryService.showAllReadersAndBorrowedBooks();

        assertThat(verify(libraryDao, times(1)).findAllReadersAndBorrowedBooks());

    }
}