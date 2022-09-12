package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;

import java.util.Arrays;
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
        String[] titleAndAuthorArray = input.split(" / ");

        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(titleAndAuthorArray[1]);

        if (matcher.find()) {
            Book book = new Book(titleAndAuthorArray[0], titleAndAuthorArray[1]);
            bookDAO.save(book);
            return book;
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
            return null;
        }
    }

    public Reader addReaders(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            Reader reader = new Reader(input);
            readerDAO.save(reader);
            return reader;
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
            return null;
        }
    }

    public boolean borrowBook(String input) {
        int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = arrayIndicators[0];
        int readerId = arrayIndicators[1];

        boolean flag = libraryDAO.borrowBookIdToReaderId(bookId, readerId);

        return flag;
    }

    public boolean returnBookToLibrary(String input) {
        int[] bookIDAndReaderId = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = bookIDAndReaderId[0];
        int readerId= bookIDAndReaderId[1];

        boolean flag = libraryDAO.returnBookIdFromReaderId(bookId, readerId);

        return flag;
    }

    public void showAllBorrowedBooksUser() {
        System.out.println("Please enter readerID");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        libraryDAO.findAllBorrowedBooksByReaderId(input).forEach(System.out::println);
    }

    public void showReadersCurrentBook() {
        System.out.println("Please enter bookId");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        libraryDAO.findAllReadersByBookId(input).forEach(System.out::println);
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
