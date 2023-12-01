package system.share.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import system.share.security.handler.*;
import system.share.security.support.AjaxAuthenticationProvider;
import system.share.security.support.AjaxAuthenticationConfigurator;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final SecurityUserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .anyRequest().permitAll();
        http.exceptionHandling()
            .authenticationEntryPoint(unauthenticatedRequestHandler())
            .accessDeniedHandler(unauthorizedRequestHandler());

        ajaxLoginConfiguration(http);
        ajaxLogoutConfiguration(http);
    }

    @Override
    public void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(ajaxAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler ajaxLoginSuccessHandler() {
        return new AjaxLoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxLoginFailureHandler() {
        return new AjaxLoginFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationEntryPoint unauthenticatedRequestHandler() {
        return new UnauthenticatedRequestHandler();
    }

    @Bean
    public AccessDeniedHandler unauthorizedRequestHandler() {
        return new UnauthorizedRequestHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private void ajaxLoginConfiguration(HttpSecurity http) throws Exception {
        http.apply(new AjaxAuthenticationConfigurator<>())
            .loginProcessingUrl("/api/users/login")
            .loginManager(authenticationManagerBean())
            .loginSuccessHandler(ajaxLoginSuccessHandler())
            .loginFailureHandler(ajaxLoginFailureHandler());

    }

    private void ajaxLogoutConfiguration(HttpSecurity http) throws Exception {
        http.logout()
            .logoutUrl("/api/users/logout")
            .logoutSuccessUrl("/user/login")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler())
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");
    }
}