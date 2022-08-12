package project.service;

import project.entity.Reader;
import project.repository.ReaderRepository;

public class ReaderService {
    ReaderRepository readerRepository = new ReaderRepository();

    Reader reader1 = new Reader(1, "Andrey Kravchenko");
    Reader reader2 = new Reader(2, "Danil Kolyagin");
    Reader reader3 = new Reader(3, "Aleksandra Kosharnaya");

    public void addReaders() {
        readerRepository.readerList.add(reader1);
        readerRepository.readerList.add(reader2);
        readerRepository.readerList.add(reader3);
    }

    public void showReaders() {
        System.out.println(readerRepository.readerList);
    }
}
