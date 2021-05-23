package com.ega.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    EGAUserDetailsService egaUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //In memory authentication
        //auth.inMemoryAuthentication().withUser("Gopal").password("123").roles("ADMIN");
        auth.userDetailsService(egaUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("USER")
                .antMatchers("/*").permitAll();

        http.formLogin()
                //.permitAll();
                //TODO - success handler and failure handler
                //.successForwardUrl("")
                //.defaultSuccessUrl("/default")
                .successHandler(getAuthenticationSuccessHandler())
                //.failureForwardUrl("/error");
                //.failureUrl("/errorResponse").permitAll()
                .failureHandler(getAuthenticationFailureHandler());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                if (response.isCommitted()) {
                    log.debug("Response has already been committed. Unable to redirect");
                    return;
                }
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
                for (GrantedAuthority authority : roles) {
                    if (authority.getAuthority().contains("ADMIN")) {
                        response.sendRedirect(request.getContextPath() + "/admin");
                        return;
                    } else if (authority.getAuthority().contains("USER")) {
                        response.sendRedirect(request.getContextPath() + "/user");
                        return;
                    } else {
                        response.sendRedirect(request.getContextPath() + "/default");
                        return;
                    }
                }
            }
        };
    }

    public SimpleUrlAuthenticationSuccessHandler getSimpleAuthSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/default");
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler getSimpleAuthFailureHandler() {
        //return new SimpleUrlAuthenticationFailureHandler("/errorPage");
        SimpleUrlAuthenticationFailureHandler failureHandler =
                new SimpleUrlAuthenticationFailureHandler();
        failureHandler.setUseForward(false);
        failureHandler.setDefaultFailureUrl("/errorPage");
        return failureHandler;
    }

    @Bean
    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setHeader("Authentication Handler", "Authentication Failed");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String, Object> data = new HashMap<>();
                data.put("timestamp", Calendar.getInstance().getTime());
                data.put("exception", exception.getMessage());
                System.out.println("Error occured in authentication!");
                //To write on page
                // response.getOutputStream().print(new ObjectMapper().writeValueAsString(data));
                response.sendRedirect(request.getContextPath()+"/errorPage");
            }
        };
    }

}
