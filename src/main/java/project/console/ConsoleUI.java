package project.console;

import project.service.BookService;
import project.service.ReaderService;

import java.util.Scanner;

public class ConsoleUI {
    BookService bookService = new BookService();
    ReaderService readerService = new ReaderService();

    Scanner scanner = new Scanner(System.in);

    public void run() {
        bookService.addBooks();
        readerService.addReaders();

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
        System.out.println("");
        System.out.println("Type 'exit' to stop program and exit!");
    }
}