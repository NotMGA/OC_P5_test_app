import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
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

    private String jwtSecret = "testSecret";
    private int jwtExpirationMs = 3600000; // 1 heure en millisecondes

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Réglage des valeurs pour jwtSecret et jwtExpirationMs
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void testGenerateJwtToken() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // Un JWT commence souvent par eyJ
    }

    @Test
    void testGetUserNameFromJwtToken() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        // Arrange
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_MalformedJwtException() {
        // Arrange
        String malformedToken = "malformedToken";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_IllegalArgumentException() {
        // Arrange
        String emptyToken = "";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_ExpiredJwtException() {
        // Arrange
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000); // Set expiration time to the past
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", false, "password");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String expiredToken = jwtUtils.generateJwtToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Assert
        assertFalse(isValid);
    }
}
