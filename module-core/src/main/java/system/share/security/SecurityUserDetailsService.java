package system.share.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import system.core.entity.User;
import system.core.exception.UserLoginException;
import system.core.repository.query.UserQueryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserQueryRepository userQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws AuthenticationException {
        User entity = userQueryRepository
                .getByUserEmail(userEmail)
                .orElseThrow(() -> UserLoginException.userNotFound());

        return SecurityUserDetails.create(entity);
    }
}