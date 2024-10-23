import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        // Nettoyage de la base de données avant chaque test
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Arrange
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"email\": \"newuser@example.com\", \"password\": \"password123\", \"firstName\": \"John\", \"lastName\": \"Doe\" }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().json("{ \"message\": \"User registered successfully!\" }"));
    }

    @Test
    public void testRegisterUser_EmailAlreadyTaken() throws Exception {
        // Arrange: créer un utilisateur dans la base de données
        User existingUser = new User("existinguser@example.com", "Doe", "John", passwordEncoder.encode("password123"),
                false);
        userRepository.save(existingUser);

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"email\": \"existinguser@example.com\", \"password\": \"password123\", \"firstName\": \"John\", \"lastName\": \"Doe\" }"));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().json("{ \"message\": \"Error: Email is already taken!\" }"));
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // Arrange: créer un utilisateur dans la base de données
        User existingUser = new User("test@example.com", "Doe", "John", passwordEncoder.encode("password123"), false);
        userRepository.save(existingUser);

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"test@example.com\", \"password\": \"password123\" }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
