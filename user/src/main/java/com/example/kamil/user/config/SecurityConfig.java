package com.example.kamil.user.config;

import com.example.kamil.user.exception.AppAuthenticationEntryPoint;
import com.example.kamil.user.filter.AuthorizationFilter;
import com.example.kamil.user.model.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
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
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AppAuthenticationEntryPoint();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http , AuthorizationFilter authorizationFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( (exception)->
                        exception.authenticationEntryPoint(authenticationEntryPoint())
                )
                .authorizeHttpRequests(request -> {

                    // Swagger UI -> permit all swagger sub-path , for ex: "/swagger-ui/" ,"/swagger-ui/index.html" ,"/swagger-ui/api-docs" etc.
                    request.requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll();
                    request.requestMatchers("/h2-console/**").permitAll(); // permits access to all URLs starting with /h2-console/ without authentication.

                    // todo: Secure http requests !

                    //   Auth Controller
                    request.requestMatchers("/v1/auth/logout").authenticated(); // todo: not implemented yet !
                    request.requestMatchers("/v1/auth/**").anonymous();//accessible to only unauthenticated users

                    // User Controller
                    request
                            .requestMatchers(HttpMethod.PUT,"/v1/users/{email}").authenticated()
                            .requestMatchers(HttpMethod.POST,"/v1/users/{email}").hasRole(Role.ROLE_ADMIN.getValue())
//                            .requestMatchers("/v1/users/{email}/deactivateUser").authenticated()// if user has role admin it can deactivate any user except other admins , i do inside service method
//                            .requestMatchers("/v1/users/{email}/activateUser").authenticated()
//                            .requestMatchers("/v1/users/{email}/getAllVendorRequests").authenticated()
                            .requestMatchers("/v1/users/{email}/**").authenticated()//FOR deactivateUser,activateUser,getAllVendorRequests
                            .requestMatchers(HttpMethod.GET,"/v1/users/getAllVendorRequests").hasRole(Role.ROLE_ADMIN.getValue())
                            .requestMatchers(HttpMethod.GET,"/v1/users/getAllUsers").hasRole(Role.ROLE_ADMIN.getValue())
                            .requestMatchers(HttpMethod.GET,"/v1/users/getActiveUsers").hasRole(Role.ROLE_ADMIN.getValue());
//                            .requestMatchers("/v1/users/**").permitAll();

                    // User Details Controller
                    request
                            .requestMatchers(HttpMethod.GET,"/v1/userDetails/{email}").authenticated()
                            .requestMatchers(HttpMethod.PUT,"/v1/userDetails/{email}").authenticated()
                            .requestMatchers(HttpMethod.PATCH,"/v1/userDetails/{email}/deleteVendorRole").hasRole(Role.ROLE_ADMIN.getValue())
                            .requestMatchers(HttpMethod.PATCH,"/v1/userDetails/{email}/addVendorRole").hasRole(Role.ROLE_ADMIN.getValue())
                            .requestMatchers(HttpMethod.PATCH,"/v1/userDetails/{email}/addAdminRole").hasRole(Role.ROLE_SUPER_ADMIN.getValue())
                            .requestMatchers(HttpMethod.PATCH,"/v1/userDetails/{email}/deleteAdminRole").hasRole(Role.ROLE_SUPER_ADMIN.getValue())
                            .requestMatchers("/v1/userDetails/**").permitAll();// todo: secure these later

                    // Message Controller
                    request.requestMatchers("/v1/message/send").permitAll();


                    // Test Controller
                    request
                            .requestMatchers("/v1/test/user").hasRole(Role.ROLE_USER.getValue())
                            .requestMatchers("/v1/test/testAdmin").hasAnyRole(Role.ROLE_ADMIN.getValue());
                    // WebSocketController
                    request.anyRequest().permitAll();//todo: fix this

                })
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())// it solves 403 instead of 401 problem
                //.formLogin(Customizer.withDefaults())

                .build();
    }


}
