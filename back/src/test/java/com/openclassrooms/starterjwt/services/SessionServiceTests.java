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

        // Initialisation des objets User et Session
        user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(false);

        // Utilisation de new ArrayList<>() pour avoir une liste modifiable
        session = new Session()
                .setId(1L)
                .setName("Yoga Session")
                .setDate(new Date())
                .setDescription("A yoga session for beginners.")
                .setUsers(new ArrayList<>()); // Remplace Arrays.asList() par une ArrayList vide
    }

    @Test
    void testCreateSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDeleteSession() {
        Long sessionId = 1L;

        sessionService.delete(sessionId);

        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testFindAllSessions() {
        Session session2 = new Session().setId(2L).setName("Advanced Yoga Session").setDate(new Date())
                .setDescription("Advanced level yoga");
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session, session2));

        assertEquals(2, sessionService.findAll().size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateSession() {
        Long sessionId = 1L;
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(sessionId, session);

        assertNotNull(result);
        assertEquals(sessionId, result.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;
        User newUser = new User().setId(userId).setEmail("newuser@example.com").setFirstName("Jane").setLastName("Doe")
                .setPassword("pass").setAdmin(false);

        // Mocking the findById methods to return non-null objects
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));

        // Appel de la méthode participate
        sessionService.participate(sessionId, userId);

        // Vérification que l'utilisateur a été ajouté à la session
        assertTrue(session.getUsers().contains(newUser));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;
        User existingUser = new User().setId(userId).setEmail("existinguser@example.com")
                .setFirstName("Jane").setLastName("Doe").setPassword("pass").setAdmin(false);

        // Ajouter l'utilisateur à la session avant d'appeler noLongerParticipate
        session.getUsers().add(existingUser);

        // Mocking the findById methods to return non-null objects
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Appel de la méthode noLongerParticipate
        sessionService.noLongerParticipate(sessionId, userId);

        // Vérification que l'utilisateur a été retiré de la session
        assertFalse(session.getUsers().contains(existingUser));
        verify(sessionRepository, times(1)).save(session);

    }
}