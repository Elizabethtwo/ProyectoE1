package com.PPOOII.Proyecto.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.PPOOII.Proyecto.Repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class WebSecConfig {

    @Autowired
    JWTAuthFilter jwtAuthorizationFilter;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
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
