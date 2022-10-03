package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Book;
import project.entity.Reader;
import project.exception.JdbcDaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LibraryDaoPostgresqlImpl implements LibraryDao {

    @Override
    public boolean borrowBookIdToReaderId(int bookId, int readerId) {
        boolean flag = false;
        final String SQL_SAVE_BORROWING = "INSERT INTO book_reader(book_id, reader_id) VALUES(?,?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BORROWING)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, readerId);
            flag = preparedStatement.execute();

        } catch (SQLException e) {
            System.err.println("Failed to borrow book for reader due to DB error: " + e.getLocalizedMessage());
        }
        return flag;
    }

    @Override
    public boolean returnByBookIdAndReaderId(int bookId, int readerId) {
        boolean flag = false;
        int amount;
        final String SQL_DELETE_BORROWING = "DELETE FROM book_reader WHERE book_id = (?) AND reader_id = (?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BORROWING)) {

            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, readerId);
            amount = preparedStatement.executeUpdate();

            if (amount > 0) {
                flag = true;
            } else {
                throw new JdbcDaoException("Failed to return this book: no such borrowing exists");
            }

        } catch (SQLException e) {
            System.err.println("Failed to return book due to DB error: " + e.getLocalizedMessage());
            throw new JdbcDaoException(e);
        }
        return flag;
    }

    @Override
    public List<Book> findAllBorrowedBooksByReaderId(int readerId) {
        List<Book> bookList = new ArrayList<>();
        final String SQL_GET_READER_BORROWED_BOOK = "SELECT book.id AS book_id, book.title, book.author FROM book \n" +
                "JOIN book_reader ON book_reader.book_id = book.id \n" +
                "WHERE book_reader.reader_id = (?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READER_BORROWED_BOOK)) {

            preparedStatement.setInt(1, readerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookList.add(mapToBook(resultSet));
            }

            resultSet.close();

        } catch (SQLException e) {
            System.err.println("Failed to find all borrowed books by reader Id due to DB error: " + e.getLocalizedMessage());
        }
        return bookList;
    }

    @Override
    public List<Reader> findAllReadersByBookId(int bookId) {
        List<Reader> readerList = new ArrayList<>();
        final String SQL_GET_READERS_BY_CURRENT_BOOK = "SELECT reader.id, reader.name FROM reader JOIN book_reader\n" +
                "ON book_reader.reader_id = reader.id \n" +
                "WHERE book_reader.book_id = (?)\n" +
                "ORDER BY reader.id";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_READERS_BY_CURRENT_BOOK)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                readerList.add(mapToReader(resultSet));
            }

            resultSet.close();

        } catch (SQLException e) {
            System.err.println("Failed to find all readers by book Id due to DB error: " + e.getLocalizedMessage());
        }
        return readerList;
    }

    @Override
    public Map<Reader, List<Book>> findAllReadersAndBorrowedBooks() {
        Map<Reader, List<Book>> readerListMap = new HashMap<>();
        final String SQL_GET_ALL_READERS_AND_BORROWED_BOOK = "SELECT reader.id, reader.name, book.id AS book_id, book.title, book.author \n" +
                "FROM reader, book JOIN book_reader \n" +
                "ON book_reader.book_id = book.id WHERE reader.id = book_reader.reader_id";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_READERS_AND_BORROWED_BOOK)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reader reader = mapToReader(resultSet);
                Book book = mapToBook(resultSet);

                if (readerListMap.containsKey(reader)) {
                    readerListMap.get(reader).add(book);
                } else {
                    List<Book> books = new ArrayList<>();
                    books.add(book);
                    readerListMap.put(reader, books);
                }
            }

            resultSet.close();

        } catch (SQLException e) {
            System.err.println("Failed to find all readers and borrowed books due to DB error: " + e.getLocalizedMessage());
        }
        return readerListMap;
    }

    private Reader mapToReader(ResultSet resultSet) {
        Reader reader = new Reader();

        try {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            reader.setId(id);
            reader.setName(name);

        } catch (SQLException e) {
            System.err.println("Failed to read reader due to DB error: " + e.getLocalizedMessage());
        }
        return reader;
    }

    private Book mapToBook(ResultSet resultSet) {
        Book book = new Book();

        try {
            int id = resultSet.getInt("book_id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");

            book.setId(id);
            book.setTitle(title);
            book.setAuthor(author);

        } catch (SQLException e) {
            System.err.println("Failed to read all books due to DB error: " + e.getLocalizedMessage());
        }
        return book;
    }
}
