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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class APIKeyAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(APIKeyAuthFilter.class);
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
        if (usuario == null) {
            sendForbidden(request, response, "El usuario del token no existe en la base de datos.");
            return;
        }
        if (usuario.getPersona() == null || !"A".equals(usuario.getPersona().getTipoPersona())) {
            sendForbidden(request, response, "El usuario del token no pertenece a una persona administrativa (tipo A).");
            return;
        }
        if (usuario.getApikey() == null || usuario.getApikey().isBlank()) {
            sendForbidden(request, response, "El usuario administrativo no tiene una API key configurada.");
            return;
        }
        if (apiKey == null || apiKey.isBlank()) {
            sendForbidden(request, response, "La solicitud no incluyó el header APIKey ni X-API-Key.");
            return;
        }
        if (!MessageDigest.isEqual(apiKey.getBytes(StandardCharsets.UTF_8),
                usuario.getApikey().getBytes(StandardCharsets.UTF_8))) {
            sendForbidden(request, response, "La API key enviada no coincide con la del usuario del token.");
            return;
        }

        logger.info("API key validada para {} {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private void sendForbidden(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        logger.warn("Solicitud rechazada por APIKeyAuthFilter: {} {} - {}",
                request.getMethod(), request.getRequestURI(), message);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
