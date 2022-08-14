package project.service;

import project.entity.Book;
import project.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookService {
    BookRepository bookRepository = new BookRepository();

    public void showBooks() {
        System.out.println(bookRepository.bookList);
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter new book name and author separated by “/”. Like this: name / author");

        String input = scanner.nextLine();
        String[] nameAndAuthorArray = input.split(" / ");

        Pattern pattern = Pattern.compile("^[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(nameAndAuthorArray[1]);

        if (matcher.find()) {
            bookRepository.bookList.add(new Book(nameAndAuthorArray[0], nameAndAuthorArray[1]));
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
        }
    }
}
