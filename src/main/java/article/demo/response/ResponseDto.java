package article.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private Error error;
    public static <T> ResponseDto<T> success(String message,T data) {
        return new ResponseDto<>(true, message, data,null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(false, message, null, new Error(code));
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
    }

}
