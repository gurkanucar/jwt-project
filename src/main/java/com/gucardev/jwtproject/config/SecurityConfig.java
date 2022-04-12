package com.gucardev.jwtproject.config;

import com.gucardev.jwtproject.config.filter.CustomAuthenticationFilter;
import com.gucardev.jwtproject.config.filter.CustomAuthorizationFilter;
import com.gucardev.jwtproject.exception.CustomAccessDeniedHandler;
import com.gucardev.jwtproject.exception.CustomAuthenticationExceptionHandler;
import com.gucardev.jwtproject.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;


    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          AuthService authService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthFilter =
                new CustomAuthenticationFilter(authService);
        customAuthFilter.setAuthenticationManager(authenticationManagerBean());
        http.formLogin().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(POST, "/auth/login", "/auth/register").permitAll();
        http.authorizeRequests().antMatchers(POST, "/user").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers("/role/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.exceptionHandling().authenticationEntryPoint(authenticationExceptionHandler());
        http.addFilterAt(customAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomAuthorizationFilter(authService),
                UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();

    }

    @Bean
    public CustomAuthenticationExceptionHandler authenticationExceptionHandler() {
        return new CustomAuthenticationExceptionHandler();
    }


}
