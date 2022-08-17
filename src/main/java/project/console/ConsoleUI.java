package project.console;

import project.service.LibraryService;

import java.util.Scanner;

public class ConsoleUI {
    private LibraryService libraryService = new LibraryService();

    Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to the library!");

        while (true) {
            getCover();
        }
    }

    private void getCover() {
        getMessage();
        String choice = getChoice();

        switch (choice) {
            case "1" -> libraryService.showBooks();
            case "2" -> libraryService.showReaders();
            case "3" -> libraryService.addReaders();
            case "4" -> libraryService.addBook();
            case "5" -> libraryService.borrowBook();
            case "6" -> libraryService.returnBookToLibrary();
            case "7" -> libraryService.showAllBorrowedBooksUser();
            case "8" -> libraryService.showReadersCurrentBook();
            case "exit" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }

    private String getChoice() {
        return String.valueOf(scanner.next());
    }


    private void getMessage() {
        System.out.println("Please, select one of the following actions by typing the option's number and pressing Enter key: ");
        System.out.println("[1] Show all books in the library");
        System.out.println("[2] Show all readers registered in the library");
        System.out.println("[3] Register new reader");
        System.out.println("[4] Add new book");
        System.out.println("[5] Borrow a book to a reader");
        System.out.println("[6] Return a book to the library");
        System.out.println("[7] Show all borrowed book by user Id");
        System.out.println("[8] Show current reader of a book with Id");
        System.out.println("");
        System.out.println("Type 'exit' to stop program and exit!");
    }
}