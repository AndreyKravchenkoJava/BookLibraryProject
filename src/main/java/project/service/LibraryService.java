package project.service;

import project.entity.Book;
import project.entity.Reader;
import project.repository.BookRepository;
import project.repository.LibraryRepository;
import project.repository.ReaderRepository;

import java.util.Arrays;
import java.util.Scanner;

public class LibraryService {
    LibraryRepository libraryRepository = new LibraryRepository();

    public void borrowBook() {
        BookRepository bookRepository = new BookRepository();
        ReaderRepository readerRepository = new ReaderRepository();

        System.out.println(bookRepository.bookList);
        System.out.println(readerRepository.readerList);

        // Автоинкремент работеат не так, как я хочу. Id-шники обновляються в этом методе, и их счет начинаеться с 4, пробывал изменить, не получилось

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int[] arrayIndicators = Arrays.stream(input.split(" / ")).mapToInt(Integer::parseInt).toArray();
        int bookId = arrayIndicators[0];
        int readerId = arrayIndicators[1];

        for (int i = 0; i < bookRepository.bookList.size(); i++) {
            for (int j = 0; j < readerRepository.readerList.size(); j++) {

                if (bookRepository.bookList.get(i).getId() == bookId && readerRepository.readerList.get(j).getId() == readerId) {
                    Book book = new Book(bookRepository.bookList.get(i).getName(), bookRepository.bookList.get(i).getAuthor());
                    Reader reader = new Reader(readerRepository.readerList.get(j).getName());
                    System.out.println("book");

                    libraryRepository.borrowedBooks.put(book, reader);
                }
            }
        }

        System.out.println(libraryRepository.borrowedBooks);
    }
}
