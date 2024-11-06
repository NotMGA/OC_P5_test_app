package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur pour les tests
        user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(false);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;

        // Act Appel de la méthode delete
        userService.delete(userId);

        // Assert Vérifie que la méthode deleteById est bien appelée avec le bon
        // argument
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testFindById_UserExists() {
        // Arrange
        Long userId = 1L;

        // Mock pour simuler qu'un utilisateur est trouvé
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // ACT Appel de la méthode findById
        User foundUser = userService.findById(userId);

        // Assert Vérifie que l'utilisateur trouvé est bien celui attendu
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("test@example.com", foundUser.getEmail());

        // Vérifie que la méthode findById est bien appelée avec le bon argument
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserNotFound() {
        // Arragne
        Long userId = 1L;

        // Mock pour simuler qu'aucun utilisateur n'est trouvé
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act Appel de la méthode findById
        User foundUser = userService.findById(userId);

        // Assert Vérifie que la méthode renvoie null si l'utilisateur n'existe pas
        assertNull(foundUser);

        // Vérifie que la méthode findById est bien appelée avec le bon argument
        verify(userRepository, times(1)).findById(userId);
    }
}
