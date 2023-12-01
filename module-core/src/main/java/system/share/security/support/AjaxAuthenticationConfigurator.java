package system.share.security.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import system.share.security.filter.AjaxLoginProcessingFilter;

@Slf4j
public class AjaxAuthenticationConfigurator<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, AjaxAuthenticationConfigurator<H>, AjaxLoginProcessingFilter> {
    private AuthenticationManager loginManager;
    private AuthenticationSuccessHandler loginSuccessHandler;
    private AuthenticationFailureHandler loginFailureHandler;

    public AjaxAuthenticationConfigurator() {
        super(new AjaxLoginProcessingFilter(), null);
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(H http) {
        http.addFilterBefore(loginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String url) {
        return new AntPathRequestMatcher(url, HttpMethod.POST.name());
    }

    public AjaxAuthenticationConfigurator<H> loginPage(String page) {
        return super.loginPage(page);
    }

    public AjaxAuthenticationConfigurator<H> loginManager(AuthenticationManager manager) {
        this.loginManager = manager;

        return this;
    }

    public AjaxAuthenticationConfigurator<H> loginSuccessHandler(AuthenticationSuccessHandler handler) {
        this.loginSuccessHandler = handler;

        return this;
    }

    public AjaxAuthenticationConfigurator<H> loginFailureHandler(AuthenticationFailureHandler handler) {
        this.loginFailureHandler = handler;

        return this;
    }

    private AjaxLoginProcessingFilter loginProcessingFilter() {
        AjaxLoginProcessingFilter filter = getAuthenticationFilter();

        filter.setAuthenticationManager(loginManager);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);

        return filter;
    }
}