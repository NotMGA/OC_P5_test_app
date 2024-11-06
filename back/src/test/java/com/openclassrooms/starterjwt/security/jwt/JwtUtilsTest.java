package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Initialize Mockito et injecter les valeurs nécessaires
        MockitoAnnotations.openMocks(this);

        // Configurer jwtUtils avec un secret et un délai d'expiration
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000); // 1 heure
    }

    @Test
    void testGenerateJwtToken() {
        // Arrange: Préparer un utilisateur de test
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act: Générer un token JWT
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert: Vérifier que le token n'est pas null et semble être un JWT
        assertNotNull(token, "Le token JWT ne doit pas être null");
        assertTrue(token.startsWith("eyJ"), "Le token JWT doit commencer par 'eyJ' indiquant un JWT bien formé");
    }

    @Test
    void testGetUserNameFromJwtToken() {
        // Arrange: Générer un JWT valide
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Extraire le nom d'utilisateur à partir du token
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert: Vérifier que le nom d'utilisateur correspond
        assertEquals("testuser", username,
                "Le nom d'utilisateur extrait du token doit correspondre à celui initialisé");
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        // Arrange: Générer un JWT valide
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String token = jwtUtils.generateJwtToken(authentication);

        // Act: Valider le token
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert: Vérifier que le token est valide
        assertTrue(isValid, "Le token JWT doit être valide");
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        // Arrange: Définir un token invalide
        String invalidToken = "invalidToken";

        // Act: Valider le token invalide
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert: Vérifier que le token est invalide
        assertFalse(isValid, "Le token JWT doit être invalide");
    }

    @Test
    void testValidateJwtToken_MalformedJwtException() {
        // Arrange: Définir un token mal formé
        String malformedToken = "malformedToken";

        // Act: Valider le token mal formé
        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        // Assert: Vérifier que le token est invalide
        assertFalse(isValid, "Le token JWT mal formé doit être considéré comme invalide");
    }

    @Test
    void testValidateJwtToken_IllegalArgumentException() {
        // Arrange: Définir un token vide
        String emptyToken = "";

        // Act: Valider le token vide
        boolean isValid = jwtUtils.validateJwtToken(emptyToken);

        // Assert: Vérifier que le token vide est invalide
        assertFalse(isValid, "Le token JWT vide doit être considéré comme invalide");
    }

    @Test
    void testValidateJwtToken_ExpiredJwtException() {
        // Arrange: Configurer une expiration passée pour générer un token expiré
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000); // Token expiré
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        String expiredToken = jwtUtils.generateJwtToken(authentication);

        // Act: Valider le token expiré
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Assert: Vérifier que le token expiré est invalide
        assertFalse(isValid, "Le token JWT expiré doit être considéré comme invalide");
    }
}
