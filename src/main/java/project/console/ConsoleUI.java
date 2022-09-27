package project.console;

import project.service.LibraryService;

import java.util.Scanner;

public class ConsoleUI {
    private final LibraryService libraryService = new LibraryService();

    private final Scanner scanner = new Scanner(System.in);

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
            case "1" -> libraryService.showBooks();
            case "2" -> libraryService.showReaders();
            case "3" -> addReaderToLibrary();
            case "4" -> addBookToLibrary();
            case "5" -> borrowBook();
            case "6" -> returnBookToLibrary();
            case "7" -> showAllBorrowedBooksByReaderId();
            case "8" -> showAllReadersByCurrentBook();
            case "9" -> libraryService.showAllReadersAndBorrowedBook();
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

    private void addReaderToLibrary() {
        System.out.println("Please enter new reader full name!");
        String input = scanner.nextLine();
        try {
            if (libraryService.addReader(input) != null) {
                System.out.println("Reader added successfully");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void addBookToLibrary() {
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");
        String input = scanner.nextLine();
        try {
            if (libraryService.addBook(input) != null) {
                System.out.println("Book added successfully");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void borrowBook() {
        System.out.println("Please enter bookId and readerId separated by “/” for borrow. Like this: bookId / readerId");
        String input = scanner.nextLine();
        try {
            if (libraryService.borrowBook(input)) {
                System.out.println("Book successfully borrowed");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void returnBookToLibrary() {
        System.out.println("Please enter bookId and readerId to return the book to the library. Like this: bookId / readerId");
        String input = scanner.nextLine();
        try {
            if (libraryService.returnBookToLibrary(input)) {
                System.out.println("Book successfully returned");
            }
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void showAllBorrowedBooksByReaderId() {
        System.out.println("Please enter readerID");
        String input = scanner.nextLine();
        try {
            libraryService.showAllBorrowedBooksReader(input);
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void showAllReadersByCurrentBook() {
        System.out.println("Please enter bookId");
        String input = scanner.nextLine();
        try {
            libraryService.showReadersCurrentBook(input);
        } catch (RuntimeException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}