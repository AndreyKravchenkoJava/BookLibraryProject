package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class LibraryService {
    private LibraryDAO libraryDAO = new LibraryDAO();
    private BookDAO bookDAO = new BookDAO();
    private ReaderDAO readerDAO = new ReaderDAO();

    public void showBooks() {
        bookDAO.findAll().forEach(System.out::println);
    }

    public void showReaders() {
        readerDAO.findAll().forEach(System.out::println);
    }

    public Book addBook(String input) {
        Book book = null;

        try {
            String[] titleAndAuthorArray = input.split(" / ");

            String title = titleAndAuthorArray[0];
            String author = titleAndAuthorArray[1];

            Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
            Matcher matcher = pattern.matcher(author);

            if (matcher.find()) {
                book = new Book(title, author);
                bookDAO.save(book);
            } else {
                throw new IllegalArgumentException("""
                        
                        Fail when adding name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return book;
    }

    public Reader addReaders(String input) {
        Reader reader = null;

        try {
            Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                reader = new Reader(input);
                readerDAO.save(reader);
            } else {
                throw new IllegalArgumentException("""
                        
                        Fail when adding name!
                        
                        1. Name must contain only letters
                        2. Have more than two letters
                        3. Maximum number of letters 100
                        
                        Fro example 'Danyl Zanuk'""");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public boolean borrowBook(String input) {
        boolean flag = false;

        try {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            int bookId = arrayIndicators[0];
            int readerId = arrayIndicators[1];
            flag = libraryDAO.borrowBookIdToReaderId(bookId, readerId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean returnBookToLibrary(String input) {
        boolean flag = false;

        try {
            int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            int bookId = arrayIndicators[0];
            int readerId = arrayIndicators[1];

            flag = libraryDAO.returnBookIdFromReaderId(bookId, readerId);

            if (!flag) {
                throw new NoSuchElementException("""
                        
                        Fail when returning book in Library!
                        
                        Possible reasons:
                        1. There is no such book
                        2. There is no such reader
                        3. Reader with this Id has not borrowed a book with this Id""");
            }

        } catch (NoSuchElementException | NumberFormatException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void showAllBorrowedBooksReader(String input) {

        try {
            int readerId = Integer.parseInt(input);

            if (!libraryDAO.findAllBorrowedBooksByReaderId(readerId).isEmpty()) {
                libraryDAO.findAllBorrowedBooksByReaderId(readerId).forEach(System.out::println);
            } else {
                throw new NoSuchElementException("""
                        
                        Fail when show borrowed books by readers:
                        
                        Possible reasons:
                        1. There is no such reader
                        2. The reader did not borrow the books""");
            }

        } catch (NoSuchElementException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void showReadersCurrentBook(String input) {

        try {
            int bookId = Integer.parseInt(input);

            if (!libraryDAO.findAllReadersByBookId(bookId).isEmpty()) {
                libraryDAO.findAllReadersByBookId(bookId).forEach(System.out::println);
            } else {
                throw new NoSuchElementException("""
                        
                        Fail when show reader by current book:
                        
                        Possible reasons:
                        1. There is no such book
                        2. This book has not been borrowed by more than one reader""");
            }

        } catch (NoSuchElementException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void showAllReadersAndBorrowedBook() {
        libraryDAO.findAllReadersAndBorrowedBooks().forEach((k, v) -> System.out.println(k + " : " + v));
    }

    public void setLibraryDAO(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void setReaderDAO(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }
}
