package project.dao;

import project.entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderDao {
    Reader save(Reader reader);

    Optional<Reader> findById(int readerId);

    List<Reader> findAll();
}
