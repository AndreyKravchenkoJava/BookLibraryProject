package project.dao;

import project.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);

    Optional<Book> findById(int bookId);

    List<Book> findAll();
}
