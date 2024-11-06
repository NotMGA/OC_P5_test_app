package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Arrange: Initialisation des objets User et Session
        user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(false);

        session = new Session()
                .setId(1L)
                .setName("Yoga Session")
                .setDate(new Date())
                .setDescription("A yoga session for beginners.")
                .setUsers(new ArrayList<>()); // Liste modifiable des utilisateurs
    }

    @Test
    void testCreateSession() {
        // Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        Session result = sessionService.create(session);

        // Assert
        assertNotNull(result, "La session créée ne doit pas être null");
        assertEquals(session.getName(), result.getName(),
                "Le nom de la session créée doit correspondre à celui fourni");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDeleteSession() {
        // Arrange
        Long sessionId = 1L;

        // Act
        sessionService.delete(sessionId);

        // Assert
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testFindAllSessions() {
        // Arrange
        Session session2 = new Session().setId(2L).setName("Advanced Yoga Session").setDate(new Date())
                .setDescription("Advanced level yoga");
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session, session2));

        // Act
        List<Session> allSessions = sessionService.findAll();

        // Assert
        assertEquals(2, allSessions.size(), "Le nombre de sessions trouvées doit être égal à 2");
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act
        Session result = sessionService.getById(1L);

        // Assert
        assertNotNull(result, "La session trouvée ne doit pas être null");
        assertEquals(session.getId(), result.getId(), "Les IDs des sessions doivent correspondre");
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateSession() {
        // Arrange
        Long sessionId = 1L;
        when(sessionRepository.save(session)).thenReturn(session);

        // Act
        Session result = sessionService.update(sessionId, session);

        // Assert
        assertNotNull(result, "La session mise à jour ne doit pas être null");
        assertEquals(sessionId, result.getId(), "L'ID de la session mise à jour doit correspondre");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        User newUser = new User().setId(userId).setEmail("newuser@example.com")
                .setFirstName("Jane").setLastName("Doe").setPassword("pass").setAdmin(false);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));

        // Act
        sessionService.participate(sessionId, userId);

        // Assert
        assertTrue(session.getUsers().contains(newUser), "L'utilisateur doit être ajouté à la session");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        User existingUser = new User().setId(userId).setEmail("existinguser@example.com")
                .setFirstName("Jane").setLastName("Doe").setPassword("pass").setAdmin(false);
        session.getUsers().add(existingUser);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act
        sessionService.noLongerParticipate(sessionId, userId);

        // Assert
        assertFalse(session.getUsers().contains(existingUser), "L'utilisateur ne doit plus être dans la session");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate_NotFoundException() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId),
                "NotFoundException devrait être levée lorsque la session n'existe pas");
    }

    @Test
    void testNoLongerParticipate_BadRequestException() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        session.setUsers(Arrays.asList(new User().setId(3L))); // Utilisateur qui ne correspond pas à `userId`
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId),
                "BadRequestException devrait être levée lorsque l'utilisateur ne participe pas à la session");
    }

    @Test
    void testParticipate_UserAlreadyParticipating_BadRequestException() {
        // Arrange
        Long sessionId = 1L;
        Long userId = 2L;
        User existingUser = new User().setId(userId).setEmail("existinguser@example.com")
                .setFirstName("Jane").setLastName("Doe").setPassword("pass").setAdmin(false);
        session.getUsers().add(existingUser);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId),
                "BadRequestException devrait être levée lorsque l'utilisateur participe déjà à la session");
    }
}
