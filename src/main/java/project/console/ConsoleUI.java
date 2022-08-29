package project.console;

import project.service.LibraryService;

import java.util.Scanner;

public class ConsoleUI {
    private LibraryService libraryService = new LibraryService();

    Scanner scanner = new Scanner(System.in);

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
            case "3" -> libraryService.addReaders();
            case "4" -> libraryService.addBook();
            case "5" -> libraryService.borrowBook();
            case "6" -> libraryService.returnBookToLibrary();
            case "7" -> libraryService.showAllBorrowedBooksUser();
            case "8" -> libraryService.showReadersCurrentBook();
            case "9" -> libraryService.showAllReadersAndBorrowedBook();
            case "exit" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }

    private void showMainMenu() {
        System.out.println("Please, select one of the following actions by typing the option's number and pressing Enter key:\n" +
                "[1] Show all books in the library\n" +
                "[2] Show all readers registered in the library\n" +
                "[3] Register new reader\n" +
                "[4] Add new book\n" +
                "[5] Borrow a book to a reader\n" +
                "[6] Return a book to the library\n" +
                "[7] Show all borrowed book by user Id\n" +
                "[8] Show current reader of a book with Id\n" +
                "[9] Show all readers and borrowed book\n" +
                "\n" +
                "Type 'exit' to stop program and exit!");
    }
}