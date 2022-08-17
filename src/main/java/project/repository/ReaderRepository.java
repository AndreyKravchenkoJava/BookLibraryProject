package project.repository;

import project.entity.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderRepository {
    private List<Reader> readerList = new ArrayList<>();

    Reader reader1 = new Reader("Andrey Kravchenko");
    Reader reader2 = new Reader("Danil Kolyagin");
    Reader reader3 = new Reader("Aleksandra Kosharnaya");

    public ReaderRepository() {
        readerList.add(reader1);
        readerList.add(reader2);
        readerList.add(reader3);
    }

    public void save(Reader reader) {
        readerList.add(reader);
    }

    public Reader findReaderById(int readerId) {
        Reader reader = readerList.stream().filter(r -> r.getId() == readerId).collect(Collectors.toList()).get(0);

        return reader;
    }

    public List<Reader> findAll() {
        return readerList;
    }
}