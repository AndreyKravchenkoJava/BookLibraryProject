package project.repository;

import project.entity.Reader;

import java.util.ArrayList;
import java.util.List;

public class ReaderRepository {
    public List<Reader> readerList = new ArrayList<>();

    Reader reader1 = new Reader("Andrey Kravchenko");
    Reader reader2 = new Reader("Danil Kolyagin");
    Reader reader3 = new Reader("Aleksandra Kosharnaya");

    public ReaderRepository() {
        readerList.add(reader1);
        readerList.add(reader2);
        readerList.add(reader3);
    }
}