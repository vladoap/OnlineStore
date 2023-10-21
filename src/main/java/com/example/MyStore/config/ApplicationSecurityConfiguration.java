package com.example.MyStore.config;

import com.example.MyStore.model.enums.UserRoleEnum;
import com.example.MyStore.repository.UserRepository;
import com.example.MyStore.service.impl.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/webfonts/**").permitAll()
                                .requestMatchers("/", "/users/login", "/users/register", "/users/login-error").permitAll()
                                .requestMatchers("/admin/**", "/api/admin/**", "/statistics").hasRole(UserRoleEnum.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .formLogin(fromLogin ->
                        fromLogin.
                                loginPage("/users/login")
                                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                                .defaultSuccessUrl("/home")
                                .failureForwardUrl("/users/login-error"))
                .logout(logout ->
                        logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)

                )
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository)
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .requireCsrfProtectionMatcher(request -> {

                            String httpMethod = request.getMethod();
                            return !(httpMethod.equals("GET") || httpMethod.equals("HEAD") || httpMethod.equals("TRACE") ||
                                    httpMethod.equals("OPTIONS") || httpMethod.equals("DELETE") || httpMethod.equals("POST") || httpMethod.equals("PATCH"));
                        })
                );

        return http.build();

    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

}
