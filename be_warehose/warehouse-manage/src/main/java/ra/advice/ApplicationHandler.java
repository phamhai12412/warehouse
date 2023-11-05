package ra.advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.exception.MyCustomException;
import ra.exception.MyCustomRuntimeException;
import ra.exception.UsernameAlreadyExistsException;
import javax.security.auth.login.LoginException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class ApplicationHandler {
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String>  loginFail(LoginException loginException){
        return new ResponseEntity<>(loginException.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> invalidRequest(MethodArgumentNotValidException ex){
        Map<String,String> err = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(c->{
            err.put(c.getField(),c.getDefaultMessage());
        });
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MyCustomRuntimeException.class)
    public ResponseEntity<String> handleMyCustomRuntimeException(MyCustomRuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Không xác thực", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MyCustomException.class)
    public ResponseEntity<String> handleMyCustomException(MyCustomException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
