package project.console;

import project.service.BookService;
import project.service.LibraryService;
import project.service.ReaderService;

import java.util.Scanner;

public class ConsoleUI {
    BookService bookService = new BookService();
    ReaderService readerService = new ReaderService();
    LibraryService libraryService = new LibraryService();

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

            case "1": {
                bookService.showBooks();
                break;
            }

            case "2": {
                readerService.showReaders();
                break;
            }

            case "3": {
                readerService.addReaders();
                break;
            }

            case "4": {
                bookService.addBook();
                break;
            }

            case "5": {
                libraryService.borrowBook();
                break;
            }

            case "exit": {
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
        System.out.println("");
        System.out.println("Type 'exit' to stop program and exit!");
    }
}