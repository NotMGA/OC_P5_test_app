package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class TeacherControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private TeacherRepository teacherRepository;

        @Autowired
        private SessionRepository sessionRepository;

        private Teacher teacher;

        @BeforeEach
        void setUp() {
                // Nettoyer les tables dans un ordre correct pour éviter les violations de
                // contraintes de clé étrangère
                sessionRepository.deleteAll();
                teacherRepository.deleteAll();

                // Ajouter un professeur pour les tests
                teacher = Teacher.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                teacher = teacherRepository.save(teacher);
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        public void testFindById_Success() throws Exception {
                // Effectuer une requête GET pour trouver le professeur par ID
                ResultActions result = mockMvc.perform(get("/api/teacher/" + teacher.getId())
                                .contentType(MediaType.APPLICATION_JSON));

                // Vérifier le statut et le contenu de la réponse
                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(
                                                "{\"id\":" + teacher.getId()
                                                                + ", \"lastName\":\"Doe\", \"firstName\":\"John\"}"));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        public void testFindById_NotFound() throws Exception {
                // Effectuer une requête GET pour un ID inexistant
                ResultActions result = mockMvc.perform(get("/api/teacher/999")
                                .contentType(MediaType.APPLICATION_JSON));

                // Vérifier que le statut est Not Found (404)
                result.andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        public void testFindById_InvalidIdFormat() throws Exception {
                // Effectuer une requête GET avec un format d'ID invalide
                ResultActions result = mockMvc.perform(get("/api/teacher/abc")
                                .contentType(MediaType.APPLICATION_JSON));

                // Vérifier que le statut est Bad Request (400)
                result.andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        public void testFindAll_Success() throws Exception {
                // Effectuer une requête GET pour obtenir tous les enseignants
                ResultActions result = mockMvc.perform(get("/api/teacher")
                                .contentType(MediaType.APPLICATION_JSON));

                // Vérifier que la liste des enseignants est correctement renvoyée
                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json(
                                                "[{\"id\":" + teacher.getId()
                                                                + ", \"lastName\":\"Doe\", \"firstName\":\"John\"}]"));
        }

        @Test
        @WithMockUser(username = "testUser", roles = { "USER" })
        public void testFindAll_EmptyList() throws Exception {
                // Supprimer les enseignants pour obtenir une liste vide
                sessionRepository.deleteAll(); // Supprimer d'abord les sessions liées
                teacherRepository.deleteAll(); // Puis supprimer les enseignants

                // Effectuer une requête GET pour obtenir la liste vide des enseignants
                ResultActions result = mockMvc.perform(get("/api/teacher")
                                .contentType(MediaType.APPLICATION_JSON));

                // Vérifier que la réponse est une liste vide
                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().json("[]"));
        }
}
