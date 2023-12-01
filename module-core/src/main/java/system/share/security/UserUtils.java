package system.share.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import system.core.enums.UserGradeType;
import system.core.enums.UserRoleType;

@Slf4j
@Component(value = "userUtils")
public class UserUtils {

    /**
     * 로그인 사용자의 권한을 검사한다.
     */
    public Boolean hasRoles(UserRoleType... roleTypes) {
        if (!SecurityUtils.isAuthenticated()) return false;

        return UserGradeType.hasRoleTypes(SecurityUtils.getUser().getGradeType(), roleTypes);
    }
}