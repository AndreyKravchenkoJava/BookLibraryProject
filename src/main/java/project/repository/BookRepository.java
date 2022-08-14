package project.repository;

import project.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    public List<Book> bookList = new ArrayList<>();

    Book book1 = new Book("Java From Epam", "Igor Blinov");
    Book book2 = new Book("Metro 2035", "Dmitry Glukhovsky");
    Book book3 = new Book("The Subtle Art of Not Giving a Fuck", "Mark Manson");

    public BookRepository() {
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }
}
