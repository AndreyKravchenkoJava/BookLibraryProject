package project.exception;

public class ValidatorServiceException extends RuntimeException {

    public ValidatorServiceException(String errorMessage) {
        super(errorMessage);
    }

    public ValidatorServiceException(Exception e) {
        super(e);
    }
}
