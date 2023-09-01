package article.demo.controller;

import article.demo.response.ResponseDto;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    /**
     * IllegalArgumentException 발생시 이 메소드실행
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<?> illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        return ResponseDto.fail( "fail!", e.getMessage().toString());
    }
}
