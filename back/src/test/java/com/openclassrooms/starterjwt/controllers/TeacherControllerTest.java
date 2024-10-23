import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données avant chaque test
        teacherRepository.deleteAll();

        // Ajouter un professeur
        teacher = Teacher.builder()
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacher = teacherRepository.save(teacher);

        teacherDto = new TeacherDto(teacher.getId(), teacher.getLastName(), teacher.getFirstName(),
                teacher.getCreatedAt(), teacher.getUpdatedAt());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testFindById_Success() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/api/teacher/" + teacher.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":" + teacher.getId() + ", \"lastName\":\"Doe\", \"firstName\":\"John\"}"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testFindById_NotFound() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/api/teacher/999")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testFindById_InvalidIdFormat() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/api/teacher/abc")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testFindAll_Success() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":" + teacher.getId() + ", \"lastName\":\"Doe\", \"firstName\":\"John\"}]"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = { "USER" })
    public void testFindAll_EmptyList() throws Exception {
        // Arrange: vider la base de données
        teacherRepository.deleteAll();

        // Act
        ResultActions result = mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}
