package project.dao;

import project.entity.Book;

import java.util.List;

public interface BookDao {
    boolean save(Book book);

    List<Book> findAll();
}
