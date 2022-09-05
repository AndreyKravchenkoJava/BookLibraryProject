package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import project.dao.LibraryDAO;
import project.entity.Book;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

class LibraryServiceTest {
    private LibraryService libraryService;
    @Mock
    private LibraryDAO libraryDAO;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
        libraryDAO = Mockito.mock(LibraryDAO.class);
    }

    @Test
    void showBooks() {
    }

    @Test
    void showReaders() {
    }

    @Test
    void addBook() {
    }

    @Test
    void addReaders() {
    }

    @Test
    void borrowBook() {
    }

    @Test
    void returnBookToLibrary() {
    }

    @Test
    @DisplayName("Test show all borrowed books user by id")
    void showAllBorrowedBooksUser() {
        List<Book> bookList = new ArrayList<>();

        Book bookFirst = new Book(4, "No Brakes: My Top Gear Years", "Jeremy Clarkson");
        Book bookSecond = new Book(7, "Season of Storms", "Andrzej Sapkowski");
        Book bookThree = new Book(8, "Women", "Charles Bukowski");

        bookList.add(bookFirst);
        bookList.add(bookSecond);
        bookList.add(bookThree);

        when(libraryDAO.findAllBorrowedBooksByReaderId(3)).thenReturn(bookList);
        assertEquals(libraryDAO.findAllBorrowedBooksByReaderId(3), bookList);
    }

    @Test
    void showReadersCurrentBook() {
    }

    @Test
    void showAllReadersAndBorrowedBook() {
    }
}