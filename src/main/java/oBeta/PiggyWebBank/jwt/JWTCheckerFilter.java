package oBeta.PiggyWebBank.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oBeta.PiggyWebBank.entities.User;
import oBeta.PiggyWebBank.exceptions.UnauthorizedException;
import oBeta.PiggyWebBank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    @Autowired
    private JWT jwt;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Bearer token is missing in the call!");

        String accessToken = authHeader.substring(7);

        jwt.verifyToken(accessToken);

        String userId = jwt.getIdFromToken(accessToken);
        User currentUser = this.userService.getUserById(UUID.fromString(userId));

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Aggiorniamo il SecurityContext associandogli l'utente autenticato

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}