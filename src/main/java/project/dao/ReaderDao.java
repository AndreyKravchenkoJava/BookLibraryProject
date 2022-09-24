package project.dao;

import project.entity.Reader;

import java.util.List;

public interface ReaderDao {
    Reader save(Reader reader);

    List<Reader> findAll();
}
