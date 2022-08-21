package project.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    boolean save(T object);
    List<T> findAll();
    Optional<T> findById(int id);
    T update(T object);
    boolean delete(int id);
}
