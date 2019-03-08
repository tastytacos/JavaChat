package manager;

public class NoSuchXMLValueException extends Throwable {
    public NoSuchXMLValueException(String s) {
    }

    public NoSuchXMLValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchXMLValueException(Throwable cause) {
        super(cause);
    }

    public NoSuchXMLValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoSuchXMLValueException() {
    }
}
