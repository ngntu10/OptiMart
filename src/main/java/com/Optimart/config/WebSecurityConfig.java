    package com.Optimart.config;

    import com.Optimart.filters.JwtTokenFilter;
    import lombok.RequiredArgsConstructor;
    import org.jetbrains.annotations.NotNull;
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
        @Value("${server.servlet.context-path}")
        private String apiPrefix;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(requests -> {
                        requests
                                .requestMatchers(POST,
                                        "/auth/register",
                                        "/auth/login"
                                )
                                .permitAll()
                                .anyRequest().authenticated();
                        //.anyRequest().permitAll();
                    });
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
