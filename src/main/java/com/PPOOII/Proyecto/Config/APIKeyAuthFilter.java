package com.PPOOII.Proyecto.Config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class APIKeyAuthFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    public APIKeyAuthFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || "/auth".equals(request.getServletPath())) {
            return true;
        }
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return false;
        }

        String path = request.getServletPath();
        return "/v1/vehiculos/documentos-vencidos".equals(path)
                || "/v1/conductores/operables".equals(path)
                || path.startsWith("/v1/vehiculo/placa/")
                || "/v1/vehiculos/documentos-por-vencer".equals(path)
                || "/v1/personas/total-por-tipo".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof String login)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Se requiere un token válido");
            return;
        }

        String apiKey = request.getHeader("APIKey");
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = request.getHeader("X-API-Key");
        }
        Usuario usuario = usuarioRepository.findById_Login(login).orElse(null);
        if (usuario == null || usuario.getPersona() == null
                || !"A".equals(usuario.getPersona().getTipoPersona())
                || usuario.getApikey() == null
                || apiKey == null
                || !MessageDigest.isEqual(apiKey.getBytes(StandardCharsets.UTF_8),
                        usuario.getApikey().getBytes(StandardCharsets.UTF_8))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Se requiere un APIKey válido de un usuario administrativo");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
