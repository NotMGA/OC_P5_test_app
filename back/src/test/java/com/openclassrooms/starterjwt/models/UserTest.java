package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        // Arrange: Initialiser les objets User avec des valeurs prédéfinies
        user1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false, LocalDateTime.now(),
                LocalDateTime.now());
        user2 = new User(2L, "test2@test.com", "testlm2", "testfn2", "test123test2", false, LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    public void testToString() {
        // Arrange: Préparer la sortie attendue
        String expected = "User(id=1, email=test@test.com, lastName=testlm, firstName=testfn, password=test123test, admin=false, createdAt="
                + user1.getCreatedAt() + ", updatedAt=" + user1.getUpdatedAt() + ")";

        // Act: Obtenir la sortie réelle de la méthode toString
        String actual = user1.toString();

        // Assert: Comparer la sortie réelle avec la sortie attendue
        assertEquals(expected, actual, "La méthode toString doit retourner la représentation correcte de l'objet User");
    }

    @Test
    public void testHashCode() {
        // Arrange: Créer un autre objet User ayant le même ID et les mêmes attributs
        // que user1
        User anotherUser1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false,
                user1.getCreatedAt(), user1.getUpdatedAt());

        // Assert: Vérifier que les hashCodes sont identiques pour les objets ayant les
        // mêmes attributs
        assertEquals(user1.hashCode(), anotherUser1.hashCode(),
                "Les hashCodes doivent être identiques pour les utilisateurs ayant les mêmes attributs");

        // Assert: Vérifier que les hashCodes sont différents pour des utilisateurs
        // ayant des IDs différents
        assertNotEquals(user1.hashCode(), user2.hashCode(),
                "Les hashCodes doivent être différents pour des utilisateurs ayant des IDs différents");
    }

    @Test
    public void testEquals() {
        // Arrange: Créer un autre objet User ayant le même ID et les mêmes attributs
        // que user1
        User anotherUser1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false,
                user1.getCreatedAt(), user1.getUpdatedAt());

        // Act & Assert: Vérifier que user1 est égal à un autre User avec les mêmes
        // attributs
        assertTrue(user1.equals(anotherUser1), "Les utilisateurs avec les mêmes ID et attributs devraient être égaux");

        // Act & Assert: Vérifier que user1 n'est pas égal à user2 (IDs différents)
        assertFalse(user1.equals(user2), "Les utilisateurs avec des IDs différents ne devraient pas être égaux");

        // Act & Assert: Vérifier que user1 n'est pas égal à null
        assertFalse(user1.equals(null), "Un utilisateur ne devrait pas être égal à null");

        // Act & Assert: Vérifier que user1 n'est pas égal à un objet d'un autre type
        assertFalse(user1.equals(new Object()), "Un utilisateur ne devrait pas être égal à un objet d'un autre type");
    }
}
