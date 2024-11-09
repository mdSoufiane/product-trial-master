package core.exceptions;


import java.io.Serial;

public class ExceptionClass extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public ExceptionClass(String message) {
        super(message);
    }
    public ExceptionClass(String message, Throwable cause) {
        super(message, cause);
    }
}
