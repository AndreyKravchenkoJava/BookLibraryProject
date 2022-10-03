package project.service;

import org.apache.commons.lang3.StringUtils;
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
            System.out.printf("There are no readers in the Library");
        }
    }

    public Book addBook(String input) {

        if (input == null || input.length() == 0) {
            throw new LibraryServiceException("Failed to create new book: input cannot be empty");
        } else if (StringUtils.countMatches(input, " / ") != 1) {
            throw new LibraryServiceException("Failed to create new book: input should be separated ' / '");
        }

        String[] titleAndAuthorArray = input.split(" / ");
        String title = titleAndAuthorArray[0];
        String author = titleAndAuthorArray[1];

        if (ValidatorService.validateName(author)) {
            Book book = new Book(title, author);
            return bookDao.save(book);
        } else {
            throw new LibraryServiceException("""
                         Failed to create new book: invalid author name!
                        
                         1. Name must contain only letters
                         2. Have more than two letters
                         3. Maximum number of letters 100
                        
                         Fro example 'Danyl Zanuk'""");
        }
    }

    public Reader addReader(String input) {

        if (input == null || input.length() == 0) {
            throw new LibraryServiceException("Failed to create new reader: input cannot be empty");
        }

        if (ValidatorService.validateName(input)) {
            Reader reader = new Reader(input);
            return readerDao.save(reader);
        } else {
            throw new LibraryServiceException("""                       
                        Failed to create new reader: invalid reader name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""");
        }
    }

    public boolean borrowBook(String input) {
        int bookId;
        int readerId;

        if (ValidatorService.validateTwoId(input)) {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            bookId = arrayIndicators[0];
            readerId = arrayIndicators[1];
        } else {
            throw new LibraryServiceException("""                       
                        Failed to borrow book: invalid input!
                        
                        1. The input cannot be empty
                        2. Input should be separated ' / '
                        2. You must enter only numbers
                        3. The input must match the example

                        Fro example '50 / 50'""");
        }

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.borrowBookIdToReaderId(bookId, readerId);
        } else {
            throw new LibraryServiceException("Failed to return book: there is no such book or reader in the Library!");
        }
    }

    public boolean returnBookToLibrary(String input) {
        int bookId;
        int readerId;

        if (ValidatorService.validateTwoId(input)) {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            bookId = arrayIndicators[0];
            readerId = arrayIndicators[1];
        } else {
            throw new LibraryServiceException("""                       
                        Failed to return book: invalid input!
                        
                        1. The input cannot be empty
                        2. Input should be separated ' / '
                        2. You must enter only numbers
                        3. The input must match the example

                        Fro example '50 / 50'""");
        }

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.returnByBookIdAndReaderId(bookId, readerId);
        } else {
            throw new LibraryServiceException("Failed to return book: there is no such book or reader in the Library!");
        }
    }

    public void showAllBorrowedBooksByReader(String input) {
        int readerId;

        if (ValidatorService.validateOneId(input)) {
            readerId = Integer.parseInt(input);
        } else {
            throw new LibraryServiceException("""                       
                        Failed to show all borrowed books by reader Id: invalid input!
                        
                        1. The input cannot be empty
                        2. You must enter only number
                        3. The input must match the example

                        Fro example '5'""");
        }

        if (readerDao.findById(readerId).isEmpty()) {
            throw new LibraryServiceException("Failed to show all borrowed books by reader Id: there is no such reader in the Library");
        }

        if (!libraryDao.findAllBorrowedBooksByReaderId(readerId).isEmpty()) {
            libraryDao.findAllBorrowedBooksByReaderId(readerId).forEach(System.out::println);
        } else {
            System.out.println("This reader did not borrow books");
        }
    }

    public void showReadersByCurrentBook(String input) {
        int bookId;

        if (ValidatorService.validateOneId(input)) {
            bookId = Integer.parseInt(input);
        } else {
            throw new LibraryServiceException("""                       
                        Failed to show all readers by book Id: invalid input!
                        
                        1. The input cannot be empty
                        2. You must enter only number
                        3. The input must match the example

                        Fro example '5'""");
        }

        if (bookDao.findById(bookId).isEmpty()) {
            throw new LibraryServiceException("Failed to show all readers by book Id: there is no such book in the Library");
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
            System.out.println("Not one reader borrowed a book");
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
