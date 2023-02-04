package in.solomk.dictionary.exception;

public class BadRequestException extends DictionaryException {

    public BadRequestException(String message, Object... args) {
        super(message, args);
    }

}
