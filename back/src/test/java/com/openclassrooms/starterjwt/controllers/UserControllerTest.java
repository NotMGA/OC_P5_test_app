package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Arrange: Initialisation des objets User et UserDto pour les tests
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .build();

        userDto = new UserDto(1L, "test@example.com", "Doe", "John", false, "password123",
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void testFindById_Success() {
        // Arrange: Configuration des mocks pour retourner l'utilisateur et son DTO
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act: Appel de la méthode findById
        ResponseEntity<?> response = userController.findById("1");

        // Assert: Vérifier que la réponse est correcte
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange: Configuration pour retourner null quand l'utilisateur n'est pas
        // trouvé
        when(userService.findById(anyLong())).thenReturn(null);

        // Act: Appel de la méthode findById avec un ID inexistant
        ResponseEntity<?> response = userController.findById("1");

        // Assert: Vérifier que la réponse est Not Found (404)
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteUser_Success() {
        // Arrange: Simuler un utilisateur trouvé par l'ID
        when(userService.findById(1L)).thenReturn(user);

        // Mock du UserDetails pour simuler un utilisateur authentifié
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Créer un Authentication et l'ajouter au contexte de sécurité
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act: Appel de la méthode delete
        ResponseEntity<?> response = userController.save("1");

        // Assert: Vérifier que la suppression a réussi
        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testDeleteUser_Unauthorized() {
        // Arrange: Simuler un utilisateur trouvé par l'ID
        when(userService.findById(1L)).thenReturn(user);

        // Mock du UserDetails pour simuler un utilisateur qui n'est pas autorisé
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("other@example.com");

        // Créer un Authentication et l'ajouter au contexte de sécurité
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act: Appel de la méthode delete avec un utilisateur non autorisé
        ResponseEntity<?> response = userController.save("1");

        // Assert: Vérifier que la réponse est Unauthorized (401)
        assertEquals(401, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void testFindById_InvalidIdFormat() {
        // Act: Appel de la méthode findById avec un format d'ID invalide
        ResponseEntity<?> response = userController.findById("abc");

        // Assert: Vérifier que la réponse est Bad Request (400)
        assertEquals(400, response.getStatusCodeValue());
    }
}
