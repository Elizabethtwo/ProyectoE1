package com.PPOOII.Proyecto.Config;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.PPOOII.Proyecto.Config.Model.Constants.*;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    	private Claims parseToken(String authorizationHeader) {
		String jwtToken = authorizationHeader.substring(TOKEN_BEARER_PREFIX.length());

		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey(SUPER_SECRET_KEY))
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
	}

	private void setAuthentication(Claims claims) {

		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
		if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Claims claims = parseToken(authorizationHeader);
			if (claims.get("authorities") == null) {
				SecurityContextHolder.clearContext();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT is missing authorities");
				return;
			}
			setAuthentication(claims);
		} catch (JwtException | IllegalArgumentException e) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
			return;
		}

		filterChain.doFilter(request, response);
	}

}
