package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TeacherService teacherService;

    private Teacher teacher;
    private User user1, user2;

    @BeforeEach
    public void setUp() {
        // **Arrange**: Initialisation des objets Teacher et User
        teacher = Teacher.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        user1 = new User()
                .setId(1L)
                .setFirstName("User1")
                .setLastName("Test");

        user2 = new User()
                .setId(2L)
                .setFirstName("User2")
                .setLastName("Test");

        // **Arrange**: Configuration des mocks pour retourner les objets précédemment
        // créés
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);
    }

    @Test
    public void testSessionListToSessionDtoList() {
        // **Arrange**: Création de deux objets Session à mapper en DTO
        Session session1 = Session.builder()
                .id(1L)
                .name("Session 1")
                .teacher(teacher)
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Session 2")
                .teacher(teacher)
                .build();

        List<Session> sessionList = Arrays.asList(session1, session2);

        // **Act**: Mapper les objets Session en objets SessionDto
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        // **Assert**: Vérifier que la liste de DTO n'est pas nulle, et contient les
        // informations correctes
        assertNotNull(sessionDtoList, "La liste de SessionDto ne doit pas être nulle");
        assertEquals(2, sessionDtoList.size(), "La taille de la liste de SessionDto doit être 2");
        assertEquals(session1.getId(), sessionDtoList.get(0).getId(),
                "Le premier ID doit correspondre à celui de session1");
        assertEquals(session2.getId(), sessionDtoList.get(1).getId(),
                "Le second ID doit correspondre à celui de session2");
    }

    @Test
    public void testSessionDtoListToSessionList() {
        // **Arrange**: Création de deux objets SessionDto à mapper en entités Session
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");
        sessionDto1.setTeacher_id(1L);

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");
        sessionDto2.setTeacher_id(1L);

        List<SessionDto> sessionDtoList = Arrays.asList(sessionDto1, sessionDto2);

        // **Act**: Mapper les objets SessionDto en objets Session
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // **Assert**: Vérifier que la liste de Session n'est pas nulle et contient les
        // informations correctes
        assertNotNull(sessionList, "La liste de Session ne doit pas être nulle");
        assertEquals(2, sessionList.size(), "La taille de la liste de Session doit être 2");
        assertEquals(sessionDto1.getId(), sessionList.get(0).getId(),
                "Le premier ID doit correspondre à celui de sessionDto1");
        assertEquals(sessionDto2.getId(), sessionList.get(1).getId(),
                "Le second ID doit correspondre à celui de sessionDto2");
    }
}
