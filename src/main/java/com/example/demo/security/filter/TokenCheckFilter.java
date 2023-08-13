package com.example.demo.security.filter;

import com.example.demo.security.exception.AccessTokenException;
import com.example.demo.security.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    private static String getJwtToken(HttpServletRequest request) {
        String headerStr = request.getHeader("Authorization");
        if(headerStr == null) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }
        String tokenType = headerStr.substring(0, 6);
        if(!"Bearer". equals(tokenType)) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }
        String tokenStr = headerStr.substring(7);
        return tokenStr;
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
        String tokenStr = getJwtToken(request);
        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;
        } catch (MalformedJwtException malformedJwtException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if(jwtUtil.isTokenCheckFilterExcludeUris(uri)) {
            filterChain.doFilter(request, response);
            return ;
        }
        try {
            Map<String, Object> tokenPayload = validateAccessToken(request);
            String username = (String) tokenPayload.get("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException e) {
            e.sendResponseError(response);
        }
    }
}
