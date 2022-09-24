package project.dao;

import project.entity.Book;

import java.util.List;

public interface BookDao {
    Book save(Book book);

    List<Book> findAll();
}
