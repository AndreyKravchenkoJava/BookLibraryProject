package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryService {
    private LibraryDao libraryDao = new LibraryDaoPostgresqlImpl();
    private BookDao bookDao = new BookDaoPostgresqlImpl();
    private ReaderDao readerDao = new ReaderDaoPostgresqlImpl();

    public List<Book> showBooks() {
        if (!bookDao.findAll().isEmpty()) {
            return bookDao.findAll();
        } else {
            throw new NoSuchElementException("Failed to show all books: there are no books in the Library");
        }
    }

    public List<Reader> showReaders() {
        if (!readerDao.findAll().isEmpty()) {
            return readerDao.findAll();
        } else {
            throw new NoSuchElementException("Failed to show all readers: there are no readers in the Library");
        }
    }

    public Book addBook(String input) {

        String[] titleAndAuthorArray = input.split(" / ");
        String title = titleAndAuthorArray[0];
        String author = titleAndAuthorArray[1];

            if (parserName(author)) {
                Book book = new Book(title, author);
                return bookDao.save(book);
            } else {
                throw new IllegalArgumentException("""
                         Failed to create new book: invalid author name!
                        
                         1. Name must contain only letters
                         2. Have more than two letters
                         3. Maximum number of letters 100
                        
                         Fro example 'Danyl Zanuk'""");
            }
    }

    public Reader addReader(String input) {

            if (parserName(input)) {
                Reader reader = new Reader(input);
                return readerDao.save(reader);
            } else {
                throw new IllegalArgumentException("""                       
                        Failed to create new reader: invalid author name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""");
            }
    }

    public boolean borrowBook(String input) {
        int bookId;
        int readerId;

        if (parseTwoId(input)) {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            bookId = arrayIndicators[0];
            readerId = arrayIndicators[1];
        } else {
            throw new NumberFormatException("""                       
                        Failed to borrow book: invalid input!
                        
                        1. You must enter only numbers
                        2. The input must match the example

                        Fro example '50 / 50'""");
        }

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.borrowBookIdToReaderId(bookId, readerId);
        } else {
            throw new NoSuchElementException("Failed to return book: there is no such book or reader in the Library!");
        }
    }

    public boolean returnBookToLibrary(String input) {
        int bookId;
        int readerId;

        if (parseTwoId(input)) {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            bookId = arrayIndicators[0];
            readerId = arrayIndicators[1];
        } else {
            throw new NumberFormatException("""                       
                        Failed to return book: invalid input!
                        
                        1. You must enter only numbers
                        2. The input must match the example

                        Fro example '50 / 50'""");
        }

        if (bookDao.findById(bookId).isPresent() && readerDao.findById(readerId).isPresent()) {
            return libraryDao.returnByBookIdAndReaderId(bookId, readerId);
        } else {
            throw new NoSuchElementException("Failed to return book: there is no such book or reader in the Library!");
        }
    }

    public void showAllBorrowedBooksByReader(String input) {
        int readerId;

        if (parseOneId(input)) {
            readerId = Integer.parseInt(input);
        } else {
            throw new NumberFormatException("""                       
                        Failed to show all borrowed books by reader Id: invalid input!
                        
                        1. You must enter only number
                        2. The input must match the example

                        Fro example '5'""");
        }

        if (readerDao.findById(readerId).isPresent()) {
            libraryDao.findAllBorrowedBooksByReaderId(readerId).forEach(System.out::println);
        } else {
            throw new NoSuchElementException("Failed to show all borrowed books by reader Id: there is no such reader in the Library");
        }
    }

    public void showReadersByCurrentBook(String input) {
        int bookId;

        if (parseOneId(input)) {
            bookId = Integer.parseInt(input);
        } else {
            throw new NumberFormatException("""                       
                        Failed to show all readers by book Id: invalid input!
                        
                        1. You must enter only number
                        2. The input must match the example

                        Fro example '5'""");
        }

        if (bookDao.findById(bookId).isPresent()) {
            libraryDao.findAllReadersByBookId(bookId).forEach(System.out::println);
        } else {
            throw new NoSuchElementException("Failed to show all readers by book Id: there is no such book in the Library");
        }

    }

    public Map<Reader, List<Book>> showAllReadersAndBorrowedBooks() {
        if (!libraryDao.findAllReadersAndBorrowedBooks().isEmpty()) {
            return libraryDao.findAllReadersAndBorrowedBooks();
        } else {
            throw new NoSuchElementException("Failed to show all readers and borrowed books: not one reader borrowed a book");
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

    private boolean parserName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean parseOneId(String input) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean parseTwoId(String input) {
        Pattern pattern = Pattern.compile("^\\d+\\s\\/\\s\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

}
