package in.solomk.dictionary.service.exception;

public class AlreadyExistingException extends DictionaryException {

    public AlreadyExistingException(String message, Object... args) {
        super(message, args);
    }
}
