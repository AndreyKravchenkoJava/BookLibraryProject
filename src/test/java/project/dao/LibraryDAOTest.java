package project.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.entity.Book;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LibraryDAOTest {
    private LibraryDAO libraryDAO;

    @BeforeEach
    void setUp() {
        libraryDAO = new LibraryDAO();
    }

    @Test
    void borrowBookIdToReaderId() {
        boolean flag = libraryDAO.borrowBookIdToReaderId(5, 10);

        assertThat(flag).isTrue();
    }

    @Test
    void returnBookIdFromReaderId() {
    }

    @Test
    void findAllBorrowedBooksByReaderId() {
        List<Book> bookList = new ArrayList<>();

        Book bookFirst = new Book(4, "No Brakes: My Top Gear Years", "Jeremy Clarkson");
        Book bookSecond = new Book(7, "Season of Storms", "Andrzej Sapkowski");
        Book bookThree = new Book(8, "Women", "Charles Bukowski");

        bookList.add(bookFirst);
        bookList.add(bookSecond);
        bookList.add(bookThree);

        assertEquals(libraryDAO.findAllBorrowedBooksByReaderId(3), bookList);
    }

    @Test
    void findAllReadersByBookId() {
    }

    @Test
    void findAllReadersAndBorrowedBooks() {
    }
}