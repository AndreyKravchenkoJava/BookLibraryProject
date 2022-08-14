package project.repository;

import project.entity.Book;
import project.entity.Reader;

import java.util.HashMap;

public class LibraryRepository {
    public HashMap<Book, Reader> borrowedBooks = new HashMap<>();

}
