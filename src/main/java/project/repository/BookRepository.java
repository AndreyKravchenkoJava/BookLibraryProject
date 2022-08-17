package project.repository;

import project.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {
    private List<Book> bookList = new ArrayList<>();

    Book book1 = new Book("Java From Epam", "Igor Blinov");
    Book book2 = new Book("Metro 2035", "Dmitry Glukhovsky");
    Book book3 = new Book("The Subtle Art of Not Giving a Fuck", "Mark Manson");

    public BookRepository() {
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }

    public void save(Book book) {
        bookList.add(book);
    }

    public Book findBookById(int bookId) {
        Book book = bookList.stream().filter(b -> b.getId() == bookId).collect(Collectors.toList()).get(0);

        return book;
    }

    public List<Book> findAll() {
        return bookList;
    }

    public void delete(Book book) {
        bookList.remove(book);
    }
}
