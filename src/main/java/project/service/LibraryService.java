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
    private LibraryDAOImpl libraryDAOImpl = new LibraryDAOImpl();
    private BookDAOImpl bookDAOImpl = new BookDAOImpl();
    private ReaderDAOImpl readerDAOImpl = new ReaderDAOImpl();

    public void showBooks() {
        System.out.println(bookDAOImpl.findAll());
    }

    public void showReaders() {
        System.out.println(readerDAOImpl.findAll());
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");

        String input = scanner.nextLine();
        String[] nameAndAuthorArray = input.split(" / ");

        Pattern pattern = Pattern.compile("^[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(nameAndAuthorArray[1]);

        if (matcher.find()) {
            bookDAOImpl.save(new Book(nameAndAuthorArray[0], nameAndAuthorArray[1]));
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
        }
    }

    public void addReaders() {
        System.out.println("Please enter new reader full name!");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        Pattern pattern = Pattern.compile("^[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            readerDAOImpl.save(new Reader(input));
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

        libraryDAOImpl.save(bookId, readerId);
    }

    public void returnBookToLibrary() {
        System.out.println("Please enter bookId and readerId to return the book to the library. Like this: bookId / readerId");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] bookIDAndReaderId = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = bookIDAndReaderId[0];
        int readerId= bookIDAndReaderId[1];

        libraryDAOImpl.delete(bookId, readerId);
    }

    public void showAllBorrowedBooksUser() {
        System.out.println("Please enter readerID");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        List<Book> bookList = libraryDAOImpl.getReaderBorrowedBook(input);

        System.out.println(bookList);
    }

    public void showReadersCurrentBook() {
        System.out.println("Please enter bookId");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        List<Reader> readerList = libraryDAOImpl.getReadersByCurrentBook(input);

        System.out.println(readerList);
    }

    public void showAllReadersAndBorrowedBook() {
        System.out.println(libraryDAOImpl.getAllReadersAndBorrowedBook());
    }
}
