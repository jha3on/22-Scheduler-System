package system.api.payload.request;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginRequest {
    private String userEmail; // 사용자 이메일
    private String userPassword; // 사용자 비밀번호
}