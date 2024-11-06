package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testEqualsAndHashCode() {
        // **Arrange**: Créer deux sessions avec des IDs différents
        Session session1 = new Session();
        session1.setId(1L).setName("Yoga Session");

        Session session2 = new Session();
        session2.setId(2L).setName("Different Session");

        // **Act & Assert**: Vérifier que les sessions ne sont pas égales
        assertNotEquals(session1, session2, "Les sessions avec des IDs différents ne doivent pas être égales");

        // **Arrange**: Modifier l'ID de session2 pour qu'il soit égal à celui de
        // session1
        session2.setId(1L);

        // **Assert**: Vérifier que les sessions sont égales maintenant que les IDs sont
        // les mêmes
        assertEquals(session1, session2, "Les sessions avec les mêmes IDs doivent être égales");
    }

    @Test
    public void testSetCreatedAtAndUpdatedAt() {
        // **Arrange**: Créer une nouvelle session
        Session session = new Session();

        // **Act**: Définir une date de création
        LocalDateTime createdAt = LocalDateTime.now();
        session.setCreatedAt(createdAt);

        // **Assert**: Vérifier que la date de création est correctement définie
        assertEquals(createdAt, session.getCreatedAt(), "La date de création doit correspondre à celle définie");

        // **Act**: Définir une date de mise à jour différente
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);
        session.setUpdatedAt(updatedAt);

        // **Assert**: Vérifier que la date de mise à jour est correctement définie
        assertEquals(updatedAt, session.getUpdatedAt(), "La date de mise à jour doit correspondre à celle définie");
    }

    @Test
    public void testCanEqual() {
        // **Arrange**: Créer deux sessions différentes
        Session session1 = new Session();
        Session session2 = new Session();

        // **Act & Assert**: Vérifier que session1 peut être comparée à session2
        assertTrue(session1.canEqual(session2), "session1 doit pouvoir être égale à session2");
    }
}
