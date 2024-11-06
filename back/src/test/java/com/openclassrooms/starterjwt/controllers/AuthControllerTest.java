package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        public void setUp() {
                // Nettoyage de la base de données avant chaque test
                userRepository.deleteAll();
        }

        @Test
        public void testAuthenticateUser_Success() throws Exception {
                // Arrange : création de l'utilisateur dans la base de données
                User user = new User("test@example.com", "Doe", "John", passwordEncoder.encode("password123"), false);
                userRepository.save(user);

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("user@example.com");
                loginRequest.setPassword("password123");

                // Act : envoi de la requête POST pour s'authentifier
                ResultActions result = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"test@example.com\", \"password\": \"password123\" }"));

                // Assert : vérification du code de statut et du contenu de la réponse
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andExpect(jsonPath("$.id").value(user.getId()))
                                .andExpect(jsonPath("$.username").value("test@example.com"))
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.lastName").value("Doe"))
                                .andExpect(jsonPath("$.admin").value(false));
        }

        @Test
        public void testAuthenticateUser_Failure() throws Exception {
                // Arrange : création d'une requête de connexion avec un mot de passe incorrect
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("user@example.com");
                loginRequest.setPassword("password123");

                // Act : envoi de la requête POST pour s'authentifier
                ResultActions result = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"wrong@example.com\", \"password\": \"wrongpassword\" }"));

                // Assert : vérification que l'authentification a échoué
                result.andExpect(status().isUnauthorized());
        }

        @Test
        public void testRegisterUser_Success() throws Exception {
                // Arrange : création de la requête d'inscription
                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail("newuser@example.com");
                signupRequest.setPassword("password123");
                signupRequest.setFirstName("Jane");
                signupRequest.setLastName("Doe");

                // Act : envoi de la requête POST pour s'enregistrer
                ResultActions result = mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"newuser@example.com\", \"password\": \"password123\", \"firstName\": \"Jane\", \"lastName\": \"Doe\" }"));

                // Assert : vérification que l'utilisateur est bien enregistré
                result.andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User registered successfully!"));
        }

        @Test
        public void testMessageResponse_SetMessage() {
                // Arrange: Création d'une instance de MessageResponse
                MessageResponse messageResponse = new MessageResponse("Initial message");

                // Act : Mise à jour du message
                String newMessage = "Updated message";
                messageResponse.setMessage(newMessage);

                // Assert : Vérification que le message a bien été mis à jour
                assertEquals(newMessage, messageResponse.getMessage(),
                                "Le message devrait être mis à jour avec setMessage");
        }

        @Test
        public void testRegisterUser_EmailAlreadyTaken() throws Exception {
                // Arrange : création d'un utilisateur dans la base de données
                User existingUser = new User("existinguser@example.com", "Doe", "John",
                                passwordEncoder.encode("password123"), false);
                userRepository.save(existingUser);

                // Act : envoi de la requête POST pour s'enregistrer avec un email existant
                ResultActions result = mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"existinguser@example.com\", \"password\": \"password123\", \"firstName\": \"Jane\", \"lastName\": \"Doe\" }"));

                // Assert : vérification que l'enregistrement échoue en raison de l'email

                result.andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

                // Vérification explicite de l'initialisation du message dans MessageResponse
                MessageResponse expectedResponse = new MessageResponse("Error: Email is already taken!");
                assertEquals("Error: Email is already taken!", expectedResponse.getMessage(),
                                "Le message de MessageResponse devrait correspondre au message attendu");
        }

}
