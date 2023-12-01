package system.share.base.response;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {
    private String code; // 응답 코드
    private String message; // 응답 메시지
    private T payload; // 응답 데이터

    protected static Result<?> create(ResultType type) {
        return Result.builder()
                .code(type.getCode())
                .message(type.getMessage())
                .payload(null)
                .build();
    }

    protected static <T> Result<?> create(ResultType type, T payload) {
        return Result.builder()
                .code(type.getCode())
                .message(type.getMessage())
                .payload(payload)
                .build();
    }

    /**
     * 응답 객체를 생성한다.
     */
    public static ResponseEntity<?> of(HttpStatus status, ResultType type) {
        Result<?> response = Result.create(type, null);

        return new ResponseEntity<>(response, status);
    }

    /**
     * 응답 객체를 생성한다.
     */
    public static <T> ResponseEntity<?> of(HttpStatus status, ResultType type, T payload) {
        Result<?> response = Result.create(type, payload);

        return new ResponseEntity<>(response, status);
    }

    /**
     * 성공 응답 객체를 생성한다.
     */
    public static ResponseEntity<?> ofSuccess() {
        Result<?> response = Result.create(ResultType.SUCCESS, null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 성공 응답 객체를 생성한다.
     */
    public static <T> ResponseEntity<?> ofSuccess(T payload) {
        Result<?> response = Result.create(ResultType.SUCCESS, payload);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}