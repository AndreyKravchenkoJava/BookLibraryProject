package project.console;

import project.service.LibraryService;

import java.util.Scanner;

public class ConsoleUI {
    private LibraryService libraryService = new LibraryService();

    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to the library!");

        while (true) {
            runLibrary();
        }
    }

    private void runLibrary() {
        showMainMenu();
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> showAllBooksInLibrary();
            case "2" -> showAllReadersInLibrary();
            case "3" -> addReaderToLibrary();
            case "4" -> addBookToLibrary();
            case "5" -> borrowBookFromLibrary();
            case "6" -> returnBookToLibrary();
            case "7" -> showAllBorrowedBooksByReader();
            case "8" -> showAllReadersByCurrentBook();
            case "9" -> showAllReadersAndBorrowedBooks();
            case "exit" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }

    private void showMainMenu() {
        System.out.println("""
                Please, select one of the following actions by typing the option's number and pressing Enter key:
                [1] Show all books in the library
                [2] Show all readers registered in the library
                [3] Register new reader
                [4] Add new book
                [5] Borrow a book to a reader
                [6] Return a book to the library
                [7] Show all borrowed book by user Id
                [8] Show current reader of a book with Id
                [9] Show all readers and borrowed book
                                
                Type 'exit' to stop program and exit!
                """);
    }

    private void showAllBooksInLibrary() {
        try {
            libraryService.showBooks();
        } catch (RuntimeException e) {
            System.err.println("Failed to find all books due to DB error: " + e.getLocalizedMessage());
        }
    }

    private void showAllReadersInLibrary() {
        try {
            libraryService.showReaders();
        } catch (RuntimeException e) {
            System.err.println("Failed to find all readers due to DB error: " + e.getLocalizedMessage());
        }
    }

    private void addReaderToLibrary() {
        System.out.println("Please enter new reader full name!");
        String input = scanner.nextLine();
        try {
            libraryService.addReader(input);
            System.out.println("Reader added successfully");
        } catch (RuntimeException e) {
            System.err.println("Failed to save new reader: " + e.getLocalizedMessage());
        }
    }

    private void addBookToLibrary() {
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");
        String input = scanner.nextLine();
        try {
            libraryService.addBook(input);
            System.out.println("Book added successfully");
        } catch (RuntimeException e) {
            System.err.println("Failed to save new book: " + e.getLocalizedMessage());
        }
    }

    private void borrowBookFromLibrary() {
        System.out.println("Please enter bookId and readerId separated by “/” for borrow. Like this: bookId / readerId");
        String input = scanner.nextLine();
        try {
            libraryService.borrowBook(input);
            System.out.println("Book successfully borrowed");
        } catch (RuntimeException e) {
            System.err.println("Failed to borrow book for reader: " + e.getLocalizedMessage());
        }
    }

    private void returnBookToLibrary() {
        System.out.println("Please enter bookId and readerId to return the book to the library. Like this: bookId / readerId");
        String input = scanner.nextLine();
        try {
            libraryService.returnBookToLibrary(input);
            System.out.println("Book successfully returned");
        } catch (RuntimeException e) {
            System.err.println("Failed to return book: " + e.getLocalizedMessage());
        }
    }

    private void showAllBorrowedBooksByReader() {
        System.out.println("Please enter readerID");
        String input = scanner.nextLine();
        try {
            libraryService.showAllBorrowedBooksByReader(input);
        } catch (RuntimeException e) {
            System.err.println("Failed to find all borrowed books by reader Id: " + e.getLocalizedMessage());
        }
    }

    private void showAllReadersByCurrentBook() {
        System.out.println("Please enter bookId");
        String input = scanner.nextLine();
        try {
            libraryService.showAllReadersByCurrentBook(input);
        } catch (RuntimeException e) {
            System.err.println("Failed to find all readers by book Id: " + e.getLocalizedMessage());
        }
    }

    private void showAllReadersAndBorrowedBooks() {
        try {
            libraryService.showAllReadersAndBorrowedBooks();
        } catch (RuntimeException e) {
            System.err.println("Failed to find all readers and borrowed books due to DB error: " + e.getLocalizedMessage());
        }
    }
}