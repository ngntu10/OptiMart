    package com.Optimart.configuration.Security;

    import com.Optimart.filters.JwtTokenFilter;
    import com.Optimart.services.RefreshToken.RefreshTokenService;
    import com.Optimart.utils.LocalizationUtils;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import org.springframework.web.servlet.config.annotation.EnableWebMvc;

    import java.util.Arrays;
    import java.util.List;

    import static org.springframework.http.HttpMethod.*;

    @Configuration
    @EnableMethodSecurity
    @EnableWebSecurity
    @EnableWebMvc
    @RequiredArgsConstructor
    public class WebSecurityConfig {
        private final JwtTokenFilter jwtTokenFilter;
        private final RefreshTokenService refreshTokenService;
        private final LocalizationUtils localizationUtils;
        @Value("${api.prefix}")
        private String apiPrefix;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(requests -> {
                        requests
                                .requestMatchers(
                                        // Swagger
                                        "/v3/api-docs/**",
                                        "/swagger-ui/index.html",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger/**",
                                        "/api-docs/**",
                                        "/swagger-resources/**",
                                        "/swagger-resources/",
                                        "/configuration/ui",
                                        "/configuration/security",

                                        //Auth
                                        String.format("%s/auth/register", apiPrefix),
                                        String.format("%s/auth/login", apiPrefix),
                                        String.format("%s/auth/refreshtoken", apiPrefix),
                                        String.format("%s/auth/me", apiPrefix),
                                        String.format("%s/auth/avatar", apiPrefix),
                                        String.format("%s/auth/change-password", apiPrefix),
                                        String.format("%s/auth/update-info", apiPrefix),

                                        //Role
                                        String.format("%s/roles", apiPrefix),
                                        String.format("%s/**", apiPrefix),

                                        // Users
                                        String.format("%s/users", apiPrefix)
                                        )
                                .permitAll()
                                .anyRequest().authenticated();
                        //.anyRequest().permitAll();
                    });
            http
                    .logout(logout -> logout
                            .logoutUrl(String.format("%s/auth/logout", apiPrefix))
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .permitAll()
                            .logoutSuccessHandler(new CustomLogoutSuccessHandler(localizationUtils, refreshTokenService))
                    );

            http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                @Override
                public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("*"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                    configuration.setExposedHeaders(List.of("x-auth-token"));
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);
                    httpSecurityCorsConfigurer.configurationSource(source);
                }
            });
            return http.build();
        }
    }
