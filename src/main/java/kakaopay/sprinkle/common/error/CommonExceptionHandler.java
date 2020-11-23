package kakaopay.sprinkle.common.error;

import kakaopay.sprinkle.common.constant.Code;
import kakaopay.sprinkle.common.error.exception.EmptyInfoException;
import kakaopay.sprinkle.common.error.exception.ExpiredTokenException;
import kakaopay.sprinkle.common.error.exception.NotSprinklerInquiryException;
import kakaopay.sprinkle.common.error.exception.ReceiveFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({ReceiveFailedException.class, NotSprinklerInquiryException.class, ExpiredTokenException.class})
    protected ResponseEntity<ErrorResponse> badRequestException(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(Code.ERROR0001.getCode())
                        .message(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({EmptyInfoException.class})
    protected ResponseEntity<ErrorResponse> notFoundException(Exception e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(Code.ERROR0004.getCode())
                        .message(e.getMessage())
                        .build()
                );
    }

}
