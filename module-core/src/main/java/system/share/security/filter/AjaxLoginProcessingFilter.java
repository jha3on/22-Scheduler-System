package system.share.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import system.api.payload.request.UserLoginRequest;
import system.share.base.utility.MapperUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/users/login"));
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException, IOException {
        // Request Body: IOUtils.toString(request.getReader()) (org.apache.commons.io.IOUtils)

        UserLoginRequest payload = MapperUtils.deserialize(request.getReader(), UserLoginRequest.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(payload.getUserEmail(), payload.getUserPassword());

        return getAuthenticationManager().authenticate(token);
    }
}