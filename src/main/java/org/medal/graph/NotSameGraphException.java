package org.medal.graph;

public class NotSameGraphException extends RuntimeException {

    public NotSameGraphException() {
    }

    public NotSameGraphException(String message) {
        super(message);
    }

    public NotSameGraphException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSameGraphException(Throwable cause) {
        super(cause);
    }

    public NotSameGraphException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
