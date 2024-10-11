package com.demo.brokagefirmbackend.configuration;

import com.demo.brokagefirmbackend.filter.JwtRequestFilter;
import com.demo.brokagefirmbackend.service.security.CustomUserDetailsService;
import com.demo.brokagefirmbackend.service.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling(exceptionHandler -> {
                    exceptionHandler.authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                    "Access is denied due to invalid credentials or missing authentication."));
                    exceptionHandler.accessDeniedHandler((request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have permission to access the requested resource."));
                })
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/documentation",
                                "/error",
                                "/error/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        return authBuilder.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(userDetailsService, jwtService);
    }

}