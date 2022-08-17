package project.service;

import project.entity.Book;
import project.entity.Reader;
import project.repository.BookRepository;
import project.repository.LibraryRepository;
import project.repository.ReaderRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LibraryService {
    private LibraryRepository libraryRepository = new LibraryRepository();
    private BookRepository bookRepository = new BookRepository();
    private ReaderRepository readerRepository = new ReaderRepository();

    public void showBooks() {
        System.out.println(bookRepository.findAll());
    }

    public void showReaders() {
        System.out.println(readerRepository.findAll());
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");

        String input = scanner.nextLine();
        String[] nameAndAuthorArray = input.split(" / ");

        Pattern pattern = Pattern.compile("^[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(nameAndAuthorArray[1]);

        if (matcher.find()) {
            bookRepository.save(new Book(nameAndAuthorArray[0], nameAndAuthorArray[1]));
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
            readerRepository.save(new Reader(input));
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

        Book bookToBorrow = bookRepository.findBookById(bookId);
        Reader newBorrowed = readerRepository.findReaderById(readerId);

        libraryRepository.borrowBookToReader(bookToBorrow, newBorrowed);
        bookRepository.delete(bookToBorrow);
    }

    public void returnBookToLibrary() {
        System.out.println("Please enter bookId to return the book to the library");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        Book returningBook = new Book();

        for (Book book : libraryRepository.findAll().keySet()) {
            if (book.getId() == input) {
                returningBook = book;
            }
        }

        libraryRepository.findAll().remove(returningBook);
        bookRepository.save(returningBook);
    }

    public void showAllBorrowedBooksUser() {
        System.out.println("Please enter readerID");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        System.out.println(libraryRepository.findAll().entrySet().stream().filter(r -> r.getValue().getId() == input).collect(Collectors.toList()));
    }

    public void showReadersCurrentBook() {
        System.out.println("Please enter bookId");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        System.out.println(libraryRepository.findAll().entrySet().stream().filter(b -> b.getKey().getId() == input).collect(Collectors.toList()));
    }
}
