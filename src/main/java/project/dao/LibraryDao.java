package project.dao;

import project.entity.Book;
import project.entity.Reader;

import java.util.List;
import java.util.Map;

public interface LibraryDao {
    boolean borrowBookIdToReaderId(int bookId, int readerId);

    boolean returnBookIdFromReaderId(int bookId, int readerId);

    List<Book> findAllBorrowedBooksByReaderId(int readerId);

    List<Reader> findAllReadersByBookId(int bookId);

    Map<Reader, List<Book>> findAllReadersAndBorrowedBooks();
}
