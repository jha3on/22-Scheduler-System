package system.share.security.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import system.share.security.SecurityUserDetailsService;
import system.core.exception.UserLoginException;
import system.share.security.SecurityUserDetails;

@Slf4j
@RequiredArgsConstructor
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    private final SecurityUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = String.valueOf(authentication.getPrincipal());
        String userPassword = String.valueOf(authentication.getCredentials());
        SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsService.loadUserByUsername(userEmail);

        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
            throw UserLoginException.userLoginMismatch();
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}