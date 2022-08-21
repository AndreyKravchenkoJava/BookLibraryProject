package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDAOImpl implements DAO<Reader> {
    @Override
    public boolean save(Reader object) {
        boolean flag = false;
        final String SQL_SAVE_READER = "INSERT INTO reader(name) VALUES(?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_READER)) {

            preparedStatement.setString(1, object.getName());
            preparedStatement.execute();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<Reader> findAll() {
        List<Reader> readerList = new ArrayList<>();
        final String SQL_FIND_ALL_READERS = "SELECT * FROM reader";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_READERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                readerList.add(getReader(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return readerList;
    }

    @Override
    public Optional<Reader> findById(int id) {
        Reader reader;
        final String SQL_FIND_READER_BY_ID = "SELECT * FROM reader WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_READER_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                reader = getReader(resultSet);
                return Optional.ofNullable(reader);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Reader update(Reader object) {
        final String SQL_UPDATE_READER = "UPDATE reader SET name = (?) WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_READER)) {

            preparedStatement.setInt(2, object.getId());
            preparedStatement.setString(1, object.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return object;
    }

    @Override
    public boolean delete(int id) {
        boolean flag = false;
        final String SQL_DELETE_READER = "DELETE FROM reader WHERE id = (?)";

        try (Connection connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_READER)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    private Reader getReader(ResultSet resultSet) {
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
}
