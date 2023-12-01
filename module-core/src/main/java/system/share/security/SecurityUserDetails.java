package system.share.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import system.core.entity.User;
import java.util.Collection;
import java.util.Set;

@Getter
@AllArgsConstructor
public class SecurityUserDetails implements UserDetails {
    private final User user; // 사용자
    private final Set<? extends GrantedAuthority> authorities; // 사용자 권한 정보

    public static SecurityUserDetails create(User entity) {
        Set<? extends GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(entity.getGradeType().name()));

        return new SecurityUserDetails(entity, authorities);
    }

    public String getCode() {
        return user.getUserCode();
    }

    public String getEmail() {
        return user.getUserEmail();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isEnabled() { return true; } // 계정이 활성화되었는가?: true (y)
    @Override public boolean isAccountNonLocked() { return true; } // 계정이 잠금되지 않았는가?: true (y)
    @Override public boolean isAccountNonExpired() { return true; } // 계정이 만료되지 않았는가?: true (y)
    @Override public boolean isCredentialsNonExpired() { return true; } // 비밀번호가 만료되지 않았는가?: true (y)
}