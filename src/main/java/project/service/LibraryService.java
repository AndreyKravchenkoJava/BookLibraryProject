package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
            Matcher matcher = pattern.matcher(titleAndAuthorArray[1]);

            if (matcher.find()) {
                book = new Book(titleAndAuthorArray[0], titleAndAuthorArray[1]);
                bookDAO.save(book);
            } else {
                throw new IllegalArgumentException("Name author must contain only letters and have more than two letters");
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
                throw new IllegalArgumentException("Name must contain only letters and have more than two letters");
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean returnBookToLibrary(String input) {
        boolean flag = false;

        try {
            int[] bookIDAndReaderId = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
            int bookId = bookIDAndReaderId[0];
            int readerId= bookIDAndReaderId[1];

            flag = libraryDAO.returnBookIdFromReaderId(bookId, readerId);

            if (flag == false) {
                throw new NoSuchElementException("Book Id: " + bookId + " or reader Id: " + readerId + " is not in the Library or reader did not borrow this book");
            }

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void showAllBorrowedBooksUser() {
        System.out.println("Please enter readerID");

        try {
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();

            if (libraryDAO.findAllBorrowedBooksByReaderId(input).size() != 0) {
                libraryDAO.findAllBorrowedBooksByReaderId(input).forEach(System.out::println);
            } else {
                throw new NoSuchElementException("Reader Id: " + input + " did not take the book or the reader is not in the Library");
            }

        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    public void showReadersCurrentBook() {
        System.out.println("Please enter bookId");

        try {
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();

            if (libraryDAO.findAllReadersByBookId(input).size() != 0) {
                libraryDAO.findAllReadersByBookId(input).forEach(System.out::println);
            } else {
                throw new NoSuchElementException("Book:" + input + " was not borrowed by readers or the book is not in the Library");
            }

        } catch (NoSuchElementException e) {
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
