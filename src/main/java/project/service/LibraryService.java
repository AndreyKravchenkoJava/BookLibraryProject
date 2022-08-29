package project.service;

import project.dao.*;
import project.entity.Book;
import project.entity.Reader;

import java.util.Arrays;
import java.util.List;
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

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");

        String input = scanner.nextLine();
        String[] nameAndAuthorArray = input.split(" / ");

        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(nameAndAuthorArray[1]);

        if (matcher.find()) {
            bookDAO.save(new Book(nameAndAuthorArray[0], nameAndAuthorArray[1]));
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
        }
    }

    public void addReaders() {
        System.out.println("Please enter new reader full name!");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]{2,}$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            readerDAO.save(new Reader(input));
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
        }
    }

    public void borrowBook() {
        System.out.println("Please enter bookId and readerId separated by “/” for borrow. Like this: bookId / readerId");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = arrayIndicators[0];
        int readerId = arrayIndicators[1];

        libraryDAO.borrowBookIdToReaderId(bookId, readerId);
    }

    public void returnBookToLibrary() {
        System.out.println("Please enter bookId and readerId to return the book to the library. Like this: bookId / readerId");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] bookIDAndReaderId = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = bookIDAndReaderId[0];
        int readerId= bookIDAndReaderId[1];

        libraryDAO.returnBookIdFromReaderId(bookId, readerId);
    }

    public void showAllBorrowedBooksUser() {
        System.out.println("Please enter readerID");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        List<Book> bookList = libraryDAO.findAllBorrowedBooksByReaderId(input);

        bookList.forEach(System.out::println);
    }

    public void showReadersCurrentBook() {
        System.out.println("Please enter bookId");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        List<Reader> readerList = libraryDAO.findAllReadersByBookId(input);

        readerList.forEach(System.out::println);
    }

    public void showAllReadersAndBorrowedBook() {
        libraryDAO.findAllReadersAndBorrowedBooks().forEach((k, v) -> System.out.println(k + " : " + v));
    }
}
