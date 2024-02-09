package com.example.kamil.user.config;

import com.example.kamil.user.filter.AuthorizationFilter;
import com.example.kamil.user.model.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    // it includes userDetailsService,and passwordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService
            , PasswordEncoder passwordEncoder)
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http , AuthorizationFilter authorizationFilter) throws Exception {

        return http
                .authorizeHttpRequests(request -> {

                    // Swagger UI -> permit all swagger sub-path , for ex: "/swagger-ui/" ,"/swagger-ui/index.html" ,"/swagger-ui/api-docs" etc.
                    request.requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll();

                    // todo: Secure http requests !

                    //   Auth URLs
                    request.requestMatchers("/v1/auth/logout").authenticated();
                    // user role
                    request.requestMatchers("/v1/auth/test/user").hasRole(Role.ROLE_USER.getValue());
                    request.requestMatchers("/v1/auth/test/testAdmin").hasRole(Role.ROLE_ADMIN.getValue());
                    request.requestMatchers("/v1/auth/**").anonymous();//accessible to only unauthenticated users

                })
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


}
