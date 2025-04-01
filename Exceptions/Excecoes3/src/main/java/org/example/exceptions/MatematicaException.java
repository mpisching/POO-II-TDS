package org.example.exceptions;

public class MatematicaException extends Exception {
    public MatematicaException(String message) {
        super(message);
    }

    public MatematicaException(Exception cause) {
        super(cause);
    }

    public MatematicaException(String message, Exception cause ) {
        super(message, cause);
    }
}
