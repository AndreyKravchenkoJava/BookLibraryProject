package project.exception;

public class LibraryServiceException extends RuntimeException {

    public LibraryServiceException(String errorMessage) {
        super(errorMessage);
    }

    public LibraryServiceException(Exception e) {
        super(e);
    }
}
