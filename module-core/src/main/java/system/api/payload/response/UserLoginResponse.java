package system.api.payload.response;

import lombok.*;
import system.core.entity.User;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {
    private String userCode; // 사용자 코드
    private String userName; // 사용자 이름
    private String userEmail; // 사용자 이메일

    public static UserLoginResponse create(User entity) {
        return UserLoginResponse.builder()
                .userCode(entity.getUserCode())
                .userName(entity.getUserName())
                .userEmail(entity.getUserEmail())
                .build();
    }
}