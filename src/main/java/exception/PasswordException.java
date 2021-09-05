package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class PasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PasswordException(String message){
        super(message);}

}
