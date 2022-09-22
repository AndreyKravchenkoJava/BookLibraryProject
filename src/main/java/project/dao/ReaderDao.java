package project.dao;

import project.entity.Reader;

import java.util.List;

public interface ReaderDao {
    boolean save(Reader reader);

    List<Reader> findAll();
}
