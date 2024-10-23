package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    @Test
    public void testEqualsAndHashCode() {
        // Arrange: créer deux sessions avec des ids différents
        Session session1 = new Session();
        session1.setId(1L).setName("Yoga Session");

        Session session2 = new Session();
        session2.setId(2L).setName("Different Session");

        // Act & Assert: vérifier que les sessions ne sont pas égales
        assertNotEquals(session1, session2);

        // Si vous voulez tester le cas où les ids sont égaux
        session2.setId(1L); // même id que session1
        assertEquals(session1, session2); // Devraient être égales selon equals()
    }

    @Test
    public void testSetCreatedAtAndUpdatedAt() {
        Session session = new Session();

        LocalDateTime createdAt = LocalDateTime.now();
        session.setCreatedAt(createdAt);
        assertEquals(createdAt, session.getCreatedAt());

        LocalDateTime updatedAt = LocalDateTime.now().plusDays(1);
        session.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, session.getUpdatedAt());
    }

    @Test
    public void testCanEqual() {
        Session session1 = new Session();
        Session session2 = new Session();

        assertTrue(session1.canEqual(session2));
    }
}
