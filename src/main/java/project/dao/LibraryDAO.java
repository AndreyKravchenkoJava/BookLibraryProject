package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Book;
import project.entity.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LibraryDAO {

    public boolean save(int bookID, int readerId) {
        boolean flag = false;
        final String SQL_SAVE_BORROWING = "INSERT INTO book_reader(book_id, reader_id) VALUES(?,?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BORROWING)) {

            preparedStatement.setInt(1, bookID);
            preparedStatement.setInt(2, readerId);
            preparedStatement.execute();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    public boolean delete(int bookId, int readerId) {
        boolean flag = false;
        final String SQL_DELETE_BORROWING = "DELETE FROM book_reader WHERE book_id = (?) AND reader_id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BORROWING)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, readerId);
            preparedStatement.executeUpdate();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    public List<Book> getReaderBorrowedBook(int readerId) {
        List<Book> bookList = new ArrayList<>();
        final String SQL_GET_READER_BORROWED_BOOK = "SELECT book.id AS book_id, book.title, book.author FROM book \n" +
                "JOIN book_reader ON book_reader.book_id = book.id \n" +
                "WHERE book_reader.reader_id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READER_BORROWED_BOOK)) {

            preparedStatement.setInt(1, readerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookList.add(getBorrowedBook(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bookList;
    }

    public List<Reader> getReadersByCurrentBook(int bookId) {
        List<Reader> readerList = new ArrayList<>();
        final String SQL_GET_READERS_BY_CURRENT_BOOK = "SELECT reader.id, reader.name FROM reader JOIN book_reader\n" +
                "ON book_reader.reader_id = reader.id \n" +
                "WHERE book_reader.book_id = (?)\n" +
                "ORDER BY reader.id";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READERS_BY_CURRENT_BOOK)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                readerList.add(getBorrowedReader(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return readerList;
    }

    public Map<Reader, List<Book>> getAllReadersAndBorrowedBook() {
        Map<Reader, List<Book>> readerListMap = new HashMap<>();
        final String SQL_GET_ALL_READERS_AND_BORROWED_BOOK = "SELECT reader.id, reader.name, book.id AS book_id, book.title, book.author \n" +
                "FROM reader, book JOIN book_reader \n" +
                "ON book_reader.book_id = book.id WHERE reader.id = book_reader.reader_id";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_READERS_AND_BORROWED_BOOK)) {

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Reader reader = getBorrowedReader(resultSet);
                Book book = getBorrowedBook(resultSet);

                if (readerListMap.keySet().contains(reader)) {
                    readerListMap.get(reader).add(book);
                } else {
                    List<Book> books = new ArrayList<>();
                    books.add(book);
                    readerListMap.put(reader, books);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return readerListMap;
    }

    private Reader getBorrowedReader(ResultSet resultSet) {
        Reader reader = new Reader();

        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            reader.setId(id);
            reader.setName(name);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reader;
    }

    private Book getBorrowedBook(ResultSet resultSet) {
        Book book = new Book();

        try {
            int id = resultSet.getInt("book_id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");

            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return book;
    }
}
