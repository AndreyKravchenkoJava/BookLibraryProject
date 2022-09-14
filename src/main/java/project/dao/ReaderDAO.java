package project.dao;

import project.connector.ConnectionCreator;
import project.entity.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {

    public boolean save(Reader reader) throws IllegalArgumentException {
        boolean flag = false;
        final String SQL_SAVE_READER = "INSERT INTO reader(name) VALUES(?)";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_READER)) {

            preparedStatement.setString(1, reader.getName());
            preparedStatement.execute();

            flag = true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    public List<Reader> findAll() {
        List<Reader> readerList = new ArrayList<>();
        final String SQL_FIND_ALL_READERS = "SELECT * FROM reader";

        try (var connection = ConnectionCreator.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_READERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                readerList.add(mapToReader(resultSet));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return readerList;
    }

    private Reader mapToReader(ResultSet resultSet) {
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
