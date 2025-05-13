package lk.ijse.dep13.zeattle_tech.dto.request;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
