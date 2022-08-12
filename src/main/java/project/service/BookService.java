package project.service;

import project.entity.Book;
import project.repository.BookRepository;

public class BookService {
    BookRepository bookRepository = new BookRepository();

    Book book1 = new Book(1, "Java From Epam", "Igor Blinov");
    Book book2 = new Book(2, "Metro 2035", "Dmitry Glukhovsky");
    Book book3 = new Book(3, "The Subtle Art of Not Giving a Fuck", "Mark Manson");

    public void addBooks() {
        bookRepository.bookList.add(book1);
        bookRepository.bookList.add(book2);
        bookRepository.bookList.add(book3);
    }

    public void showBooks() {
        System.out.println(bookRepository.bookList);
    }
}
