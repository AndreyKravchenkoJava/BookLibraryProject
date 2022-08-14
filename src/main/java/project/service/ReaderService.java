package project.service;

import project.entity.Reader;
import project.repository.ReaderRepository;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReaderService {
    ReaderRepository readerRepository = new ReaderRepository();

    public void showReaders() {
        System.out.println(readerRepository.readerList);
    }

    public void addReaders() {
        System.out.println("Please enter new reader full name!");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        Pattern pattern = Pattern.compile("^[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            readerRepository.readerList.add(new Reader(input));
        } else {
            System.out.println("Name must contain only letters and have more than two letters");
        }
    }
}
