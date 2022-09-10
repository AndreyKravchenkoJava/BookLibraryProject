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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LibraryServiceTestSecond {
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
    void addCorrectBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookDAO, times(1)).save(captor.capture());
        Book bookToSave = captor.getValue();

        assertThat(createdBook).isNotNull();
        assertThat(expectedTitle).isEqualTo(createdBook.getTitle());
        assertThat(expectedAuthor).isEqualTo(createdBook.getAuthor());
        assertThat(createdBook).isEqualTo(bookToSave);
    }

    @DisplayName("Testing adding wrong new book")
    @Test
    void addWrongBook() {
        String expectedTitle = "My life, my achievements";
        String expectedAuthor = "Henry Ford 1";
        String userInput = expectedTitle + " / " + expectedAuthor;
        Book createdBook = libraryService.addBook(userInput);

        verify(bookDAO, never()).save(any());

        assertThat(createdBook).isNull();
    }

    @DisplayName("Testing adding correct new reader")
    @Test
    void addCorrectReaders() {
        String userInput = "Alexander Singeev";
        Reader createdReader = libraryService.addReaders(userInput);

        ArgumentCaptor<Reader> captor = ArgumentCaptor.forClass(Reader.class);
        verify(readerDAO, times(1)).save(captor.capture());
        Reader readerToSave = captor.getValue();

        assertThat(createdReader).isNotNull();
        assertThat(userInput).isEqualTo(createdReader.getName());
        assertThat(createdReader).isEqualTo(readerToSave);
    }

    @DisplayName("Testing adding wrong new reader")
    @Test
    void addWrongReaders() {
        String userInput = "Alexander Singeev 1";
        Reader createdReader = libraryService.addReaders(userInput);

        verify(readerDAO, never()).save(any());

        assertThat(createdReader).isNull();
    }

    @DisplayName("Testing borrow book to reader by id")
    @Test
    void borrowBook() {
        int expectedBookId = 7;
        int expectedReaderId = 12;
        String userInput = "3 / 12";
        boolean flagService = libraryService.borrowBook(userInput);

        ArgumentCaptor<Integer> bookIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> readerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(libraryDAO, times(1)).borrowBookIdToReaderId(bookIdCaptor.capture(), readerIdCaptor.capture());
        int bookIdToSave = bookIdCaptor.getValue();
        int readerIdToSave = readerIdCaptor.getValue();


        assertThat(flagService).isTrue();
        assertThat(bookIdToSave).isEqualTo(expectedBookId);
        assertThat(readerIdToSave).isEqualTo(expectedReaderId);
    }


    @Test
    void returnBookToLibrary() {
    }
}