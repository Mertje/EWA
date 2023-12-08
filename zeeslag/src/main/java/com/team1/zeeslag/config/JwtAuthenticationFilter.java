package com.team1.zeeslag.config;

import com.team1.zeeslag.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (shouldIgnoreFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null) {
            try {
                Claims claims = JwtUtil.parseToken(token);
                String subject = claims.getSubject();
                request.setAttribute("claims", subject);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            "Token not present"
        );
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    private boolean shouldIgnoreFilter(HttpServletRequest request) {
        if (request.getMethod().equals("OPTIONS")) return true;

        return request.getRequestURI().contains("/api/v1/auth");
    }
}
