package project.repository;

import project.entity.Book;
import project.entity.Reader;

import java.util.HashMap;

public class LibraryRepository {
    private HashMap<Book, Reader> borrowedBooks = new HashMap<>();

    public HashMap<Book, Reader> findAll() {
        return borrowedBooks;
    }

    public void borrowBookToReader(Book book, Reader reader) {
        borrowedBooks.put(book, reader);
    }
}
