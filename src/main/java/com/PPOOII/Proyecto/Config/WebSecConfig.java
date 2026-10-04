package com.PPOOII.Proyecto.Config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.PPOOII.Proyecto.Repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class WebSecConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecConfig.class);

    @Autowired
    JWTAuthFilter jwtAuthorizationFilter;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allow-file-origin:false}") boolean allowFileOrigin) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*", "http://127.0.0.1:*"));
        if (allowFileOrigin) {
            configuration.setAllowedOrigins(List.of("null"));
        }
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept", "APIKey", "X-API-Key"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http,
                                         CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler((request, response, exception) -> {
                    logger.warn("Acceso denegado por Spring Security: {} {} - {}",
                            request.getMethod(), request.getRequestURI(), exception.getMessage());
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.name());
                    response.getWriter().write("{\"message\":\"Spring Security denegó el acceso a este recurso.\"}");
                }))
                .authorizeHttpRequests( authz -> authz
                        .requestMatchers("/auth").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                            HttpMethod.GET,
                            "/v1/vehiculos/documentos-vencidos",
                            "/v1/conductores/operables",
                            "/v1/vehiculo/placa/*",
                            "/v1/vehiculos/documentos-por-vencer",
                            "/v1/personas/total-por-tipo"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new APIKeyAuthFilter(usuarioRepository), JWTAuthFilter.class);

        return http.build();
    }

}
