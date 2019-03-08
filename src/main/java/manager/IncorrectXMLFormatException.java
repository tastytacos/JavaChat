package manager;

public class IncorrectXMLFormatException extends Exception {
    public IncorrectXMLFormatException() {
    }

    public IncorrectXMLFormatException(String message) {
        super(message);
    }

    public IncorrectXMLFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectXMLFormatException(Throwable cause) {
        super(cause);
    }

    public IncorrectXMLFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
