package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

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

        private Session session;
        private Teacher teacher;

        @BeforeEach
        public void setUp() {
                // Arrange: Nettoyage de la base de données avant chaque test
                sessionRepository.deleteAll();
                teacherRepository.deleteAll();
                userRepository.deleteAll();

                // Arrange: Créer et sauvegarder un professeur
                teacher = new Teacher();
                teacher.setFirstName("John");
                teacher.setLastName("Doe");
                teacher = teacherRepository.save(teacher);

                // Arrange: Créer une session non encore sauvegardée
                session = new Session();
                session.setName("Yoga Session");
                session.setDescription("A yoga session to relax.");
                session.setDate(new Date());
                session.setTeacher(teacher);
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testFindById_Success() throws Exception {
                // Arrange: Sauvegarder une session
                session = sessionRepository.save(session);

                // Act & Assert: Effectuer une requête GET pour trouver la session par ID et
                // vérifier le résultat
                mockMvc.perform(get("/api/session/" + session.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.name").value("Yoga Session"))
                                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testFindById_NotFound() throws Exception {
                // Act & Assert: Effectuer une requête GET pour un ID inexistant et vérifier que
                // le statut est Not Found
                mockMvc.perform(get("/api/session/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testCreate_Success() throws Exception {
                // Act & Assert: Effectuer une requête POST pour créer une nouvelle session et
                // vérifier la réponse
                mockMvc.perform(post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{ \"name\": \"New Yoga Session\", \"description\": \"A relaxing yoga session\", \"date\": \"2024-10-22T10:00:00\", \"teacher_id\": "
                                                                + teacher.getId() + " }"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testUpdate_Success() throws Exception {
                // Arrange: Sauvegarder une session existante
                session = sessionRepository.save(session);

                // Act & Assert: Effectuer une requête PUT pour mettre à jour la session et
                // vérifier le résultat
                mockMvc.perform(put("/api/session/" + session.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                                "{ \"name\": \"Updated Yoga Session\", \"description\": \"Updated description\", \"date\": \"2024-10-22T10:00:00\", \"teacher_id\": "
                                                                + teacher.getId() + " }"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.name").value("Updated Yoga Session"))
                                .andExpect(jsonPath("$.teacher_id").value(teacher.getId()));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testDelete_Success() throws Exception {
                // Arrange: Sauvegarder une session existante
                session = sessionRepository.save(session);

                // Act & Assert: Effectuer une requête DELETE pour supprimer la session et
                // vérifier la réponse
                mockMvc.perform(delete("/api/session/" + session.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" }) // Simule un utilisateur authentifié
        public void testParticipate_Success() throws Exception {
                // Arrange: Sauvegarder une session et un utilisateur
                session = sessionRepository.save(session);
                User user = new User("participant@example.com", "Doe", "Jane", "password123", false);
                user = userRepository.save(user);

                // Act & Assert: Effectuer une requête POST pour que l'utilisateur participe à
                // la session et vérifier la réponse
                mockMvc.perform(post("/api/session/" + session.getId() + "/participate/" + user.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }
}
