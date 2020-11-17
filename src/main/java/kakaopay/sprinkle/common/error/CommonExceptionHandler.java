package kakaopay.sprinkle.common.error;

import kakaopay.sprinkle.common.constant.Code;
import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({EmptyInfoException.class})
    protected ResponseEntity<ErrorResponse> missRequiredValueException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .code(Code.ERROR0001.getCode())
                                .message(e.getMessage())
                                .build()
                );
    }

}
