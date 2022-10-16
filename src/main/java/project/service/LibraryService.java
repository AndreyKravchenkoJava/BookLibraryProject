package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;
import project.exception.LibraryServiceException;

import java.util.*;

public class LibraryService {
    private LibraryDao libraryDao = new LibraryDaoPostgresqlImpl();
    private BookDao bookDao = new BookDaoPostgresqlImpl();
    private ReaderDao readerDao = new ReaderDaoPostgresqlImpl();

    public void showBooks() {
        if (!bookDao.findAll().isEmpty()) {
            bookDao.findAll().forEach(System.out::println);
        } else {
            System.out.println("There are no books in the Library");
        }
    }

    public void showReaders() {
        if (!readerDao.findAll().isEmpty()) {
            readerDao.findAll().forEach(System.out::println);
        } else {
            System.out.println("There are no readers in the Library");
        }
    }

    public Book addBook(String input) {
        ValidatorService.isInputAddBookValid(input);

        String[] titleAndAuthorArray = input.split(" / ");
        String title = titleAndAuthorArray[0];
        String author = titleAndAuthorArray[1];

        ValidatorService.isNameReaderOrAuthorValid(author);

        Book book = new Book(title, author);
        return bookDao.save(book);
    }

    public Reader addReader(String input) {
        ValidatorService.isInputAddReaderValid(input);

        ValidatorService.isNameReaderOrAuthorValid(input);

        Reader reader = new Reader(input);
        return readerDao.save(reader);

    }

    public boolean borrowBook(String input) {
        ValidatorService.isTwoIdValid(input);

        int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = arrayIndicators[0];
        int readerId = arrayIndicators[1];

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.borrowBookIdToReaderId(bookId, readerId);
        } else {
            throw new LibraryServiceException("there is no such book or reader in the Library!");
        }
    }

    public boolean returnBookToLibrary(String input) {
        ValidatorService.isTwoIdValid(input);

        int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = arrayIndicators[0];
        int readerId = arrayIndicators[1];

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.returnByBookIdAndReaderId(bookId, readerId);
        } else {
            throw new LibraryServiceException("there is no such book or reader in the Library!");
        }
    }

    public void showAllBorrowedBooksByReader(String input) {
        ValidatorService.isOneIdValid(input);

        int readerId = Integer.parseInt(input);

        if (readerDao.findById(readerId).isEmpty()) {
            throw new LibraryServiceException("there is no such reader in the Library");
        }

        if (!libraryDao.findAllBorrowedBooksByReaderId(readerId).isEmpty()) {
            libraryDao.findAllBorrowedBooksByReaderId(readerId).forEach(System.out::println);
        } else {
            System.out.println("This reader did not borrow books");
        }
    }

    public void showAllReadersByCurrentBook(String input) {
        ValidatorService.isOneIdValid(input);

        int bookId = Integer.parseInt(input);

        if (bookDao.findById(bookId).isEmpty()) {
            throw new LibraryServiceException("there is no such book in the Library");
        }

        if (!libraryDao.findAllReadersByBookId(bookId).isEmpty()) {
            libraryDao.findAllReadersByBookId(bookId).forEach(System.out::println);
        } else {
            System.out.println("This book was not borrowed by readers");
        }

    }

    public void showAllReadersAndBorrowedBooks() {
        if (!libraryDao.findAllReadersAndBorrowedBooks().isEmpty()) {
            libraryDao.findAllReadersAndBorrowedBooks().forEach((r, b) -> System.out.println(r + " : " + b));
        } else {
            System.out.println("Not one reader borrowed a books");
        }
    }

    public void setLibraryDao(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }
}
