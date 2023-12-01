package system.share.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import system.core.entity.User;
import system.core.enums.UserGradeType;
import system.core.exception.UserException;

import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    /**
     * 로그인 사용자의 정보를 가져온다.
     */
    public static User getUser() {
        return getUserDetails().getUser();
    }

    /**
     * 로그인 사용자의 코드 값을 가져온다.
     */
    public static String getUserCode() {
        return getUserDetails().getUser().getUserCode();
    }

    /**
     * 로그인 사용자의 권한 값을 가져온다.
     */
    public static UserGradeType getUserGradeType() {
        return getUserDetails().getUser().getGradeType();
    }

    /**
     * 로그인 사용자의 정보를 가져온다.
     */
    public static SecurityUserDetails getUserDetails() {
        if (!verifySecuredUser()) throw UserException.authenticationNotFound();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (SecurityUserDetails) authentication.getPrincipal();
    }

    /**
     * 로그인 사용자의 인증 여부를 확인한다.
     */
    public static Boolean verifySecuredUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !Objects.isNull(authentication) && !StringUtils.isEmpty(authentication.getName());
    }

    /**
     * 컨텍스트에서 로그인 여부를 확인한다. (+ anonymousUser 확인)
     */
    public static Boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) return false;
        if (!authentication.isAuthenticated()) return false;

        return !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * 컨텍스트에 인증 객체를 세팅한다.
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 컨텍스트에서 인증 객체를 가져온다.
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 컨텍스트에서 인증 객체를 삭제한다.
     */
    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}