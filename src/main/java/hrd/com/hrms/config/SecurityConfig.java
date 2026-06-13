package hrd.com.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import hrd.com.hrms.security.JwtAuthenticationFilter;
import hrd.com.hrms.security.JwtAuthEntryPoint;
import hrd.com.hrms.security.JwtAccessDeniedHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(org.springframework.security.config.Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntryPoint)   // 401 with JSON envelope
                        .accessDeniedHandler(jwtAccessDeniedHandler)   // 403 with JSON envelope
                )
                .authorizeHttpRequests(auth -> auth
                        // 0. Always allow CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1. Specific rules MUST go first
                        .requestMatchers(HttpMethod.GET, "/api/auth/users-list").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-hr").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/auth/user-status/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole("ADMIN")

                        // 2. More generic permits/wildcards go second
                        .requestMatchers(
                                "/api/v1/auth/**", // Standardized auth endpoint path
                                "/api/auth/**",
                                "/uploads/**",     // Served attendance selfies / documents (img tags)
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/"
                        ).permitAll()

                        // 3. Catch-all fallback goes last
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}