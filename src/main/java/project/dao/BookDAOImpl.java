package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements DAO<Book>{

    @Override
    public boolean save(Book object) {
        boolean flag = false;
        final String SQL_SAVE_BOOK = "INSERT INTO book(title, author) VALUES(?,?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BOOK)) {

            preparedStatement.setString(1, object.getTitle());
            preparedStatement.setString(2, object.getAuthor());
            preparedStatement.execute();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        final String SQL_FIND_ALL_BOOKS = "SELECT * FROM book";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BOOKS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookList.add(getBook(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bookList;
    }

    @Override
    public Optional<Book> findById(int id) {
        Book book;
        final String SQL_FIND_BOOK_BY_ID = "SELECT * FROM book WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BOOK_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                book = getBook(resultSet);
                return Optional.ofNullable(book);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Book update(Book object) {
        final String SQL_UPDATE_BOOK = "UPDATE book SET title = (?), author = (?) WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BOOK)) {

            preparedStatement.setInt(3, object.getId());
            preparedStatement.setString(1, object.getTitle());
            preparedStatement.setString(2, object.getAuthor());
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return object;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        final String SQL_DELETE_BOOK = "DELETE FROM book WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BOOK)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    private Book getBook(ResultSet resultSet) {
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
