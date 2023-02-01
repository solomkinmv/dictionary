package in.solomk.dictionary.service.exception;

public abstract class DictionaryException extends RuntimeException {

    public DictionaryException(String message, Object... args) {
        super(message.formatted(args));
    }

    public DictionaryException(Throwable cause, String message, Object... args) {
        super(message.formatted(args), cause);
    }
}
