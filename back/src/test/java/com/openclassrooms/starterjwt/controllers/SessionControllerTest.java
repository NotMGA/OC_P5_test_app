import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionService sessionService;

    private Session session;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        // Nettoyage de la base de données avant chaque test
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        // Ajout d'un professeur
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

        // Ajout d'une session avec un professeur
        session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A yoga session to relax.");
        session.setDate(new Date());
        session.setTeacher(teacher);
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testFindById_Success() throws Exception {
        // Arrange: sauvegarder une session
        session = sessionRepository.save(session);

        // Act
        ResultActions result = mockMvc.perform(get("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testFindById_NotFound() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/api/session/999")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testCreate_Success() throws Exception {
        ResultActions result = mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"name\": \"New Yoga Session\", \"description\": \"A relaxing yoga session\", \"date\": \"2024-10-22T10:00:00\", \"teacher_id\": 1 }"));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testUpdate_Success() throws Exception {
        // Arrange: sauvegarder une session
        session = sessionRepository.save(session);

        // Act
        ResultActions result = mockMvc.perform(put("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"name\": \"Updated Yoga Session\", \"description\": \"Updated description\", \"date\": \"2024-10-22T10:00:00\", \"teacher_id\": "
                                + teacher.getId() + " }"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Yoga Session"))
                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testDelete_Success() throws Exception {
        // Arrange: sauvegarder une session
        session = sessionRepository.save(session);

        // Act
        ResultActions result = mockMvc.perform(delete("/api/session/" + session.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
    public void testParticipate_Success() throws Exception {
        // Arrange: sauvegarder une session et un utilisateur
        session = sessionRepository.save(session);
        User user = new User("participant@example.com", "Doe", "Jane", "password123", false);
        user = userRepository.save(user);

        // Act
        ResultActions result = mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk());
    }

}
