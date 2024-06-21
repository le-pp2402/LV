//package com.phatpl.learnvocabulary.filters;
//
//import com.phatpl.learnvocabulary.services.JWTService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JWTService jwtService;
//    private final UserDetailsService userDetailsService;
//    @Override
//    protected void doFilterInternal(
//            @NotNull HttpServletRequest request,
//            @NotNull HttpServletResponse response,
//            @NotNull FilterChain filterChain) throws ServletException, IOException {
//            var authHeader = request.getHeader("Authorization");
//            if (authHeader == null || !authHeader.startsWith("Bearer")) {
//                filterChain.doFilter(request, response);
//            } else {
//                var token = authHeader.substring(7);
//                var username = jwtService.getUsername(token);
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                    if (jwtService.isValid(token, userDetails)) {
//                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }
//                filterChain.doFilter(request, response);
//            }
//    }
//}
