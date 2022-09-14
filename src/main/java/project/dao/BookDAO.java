package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public boolean save(Book book) throws IllegalArgumentException {
        boolean flag = false;
        final String SQL_SAVE_BOOK = "INSERT INTO book(title, author) VALUES(?,?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BOOK)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.execute();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        final String SQL_FIND_ALL_BOOKS = "SELECT * FROM book";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BOOKS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookList.add(mapToBook(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return book;
    }
}
