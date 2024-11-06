package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        // Initialize les mocks avant chaque test et vider le contexte de sécurité
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        // Arrange: Préparer le contexte avec un JWT valide
        String jwt = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        // Act: Appeler la méthode pour simuler le filtre interne avec un JWT valide
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Vérifier les comportements attendus
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier que l'authentification a bien été configurée dans le contexte de
        // sécurité
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        assertNotNull(authentication, "Authentication should not be null for a valid JWT");
        assertEquals(userDetails, authentication.getPrincipal(),
                "The authenticated user should match the UserDetails loaded");
    }

    @Test
    public void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        // Arrange: Préparer le contexte avec un JWT invalide
        String jwt = "invalid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Act: Appeler la méthode pour simuler le filtre interne avec un JWT invalide
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Vérifier que le JWT est invalide et que la chaîne de filtre continue
        // sans authentification
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier qu'il n'y a pas d'authentification dans le contexte de sécurité
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication should be null for an invalid JWT");
    }

    @Test
    public void testDoFilterInternal_NoJwt() throws ServletException, IOException {
        // Arrange: Simuler l'absence de JWT dans l'en-tête de la requête
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act: Appeler la méthode pour simuler le filtre interne sans JWT
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert: Vérifier que la validation du JWT n'est pas effectuée
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier qu'il n'y a pas d'authentification dans le contexte de sécurité
        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication should be null when no JWT is provided");
    }
}
