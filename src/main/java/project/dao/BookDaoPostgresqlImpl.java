package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Book;
import project.exception.JdbcDaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoPostgresqlImpl implements BookDao {

    @Override
    public Book save(Book book) {
        final String SQL_SAVE_BOOK = "INSERT INTO book(title, author) VALUES(?,?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BOOK, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.execute();


            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                book.setId(resultSet.getInt("id"));
                return book;
            } else {
                throw new JdbcDaoException("Failed to fetch generated ID from DB while saving new book");
            }

        } catch (SQLException e) {
            System.err.println("Failed to save new book due to DB error: " + e.getLocalizedMessage());
            throw new JdbcDaoException(e);
        }
    }

    @Override
    public Optional<Book> findById(int bookId) {
        final String SQL_FIND_BOOK_BY_ID = "SELECT * FROM book WHERE id = (?)";
        Book book;

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BOOK_BY_ID)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                book = mapToBook(resultSet);
                return Optional.ofNullable(book);
            }

        } catch (SQLException e) {
            System.err.println("Failed to find book by Id due to DB error: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        final String SQL_FIND_ALL_BOOKS = "SELECT * FROM book";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BOOKS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookList.add(mapToBook(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Failed to find all books due to DB error: " + e.getLocalizedMessage());
        }
        return bookList;
    }

    private Book mapToBook(ResultSet resultSet) {
        Book book = new Book();

        try {
            int id = resultSet.getInt("id");
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
