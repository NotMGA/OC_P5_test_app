package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    public void setUp() {
        // Arrange: Initialiser les objets Teacher avec des valeurs prédéfinies
        teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void testToString() {
        // Act: Appeler la méthode toString sur teacher1
        String toStringOutput = teacher1.toString();

        // Assert: Vérifier que la sortie contient les valeurs attendues
        assertTrue(toStringOutput.contains("id=1"), "La sortie de toString devrait contenir 'id=1'");
        assertTrue(toStringOutput.contains("lastName=Doe"), "La sortie de toString devrait contenir 'lastName=Doe'");
        assertTrue(toStringOutput.contains("firstName=John"),
                "La sortie de toString devrait contenir 'firstName=John'");
        assertTrue(toStringOutput.contains("createdAt="), "La sortie de toString devrait contenir 'createdAt='");
        assertTrue(toStringOutput.contains("updatedAt="), "La sortie de toString devrait contenir 'updatedAt='");
    }

    @Test
    public void testHashCode_SameId() {
        // Arrange: Créer un autre objet Teacher avec le même ID que teacher1
        Teacher anotherTeacher1 = new Teacher(1L, "Doe", "John", teacher1.getCreatedAt(), teacher1.getUpdatedAt());

        // Assert: Vérifier que les hashcodes sont identiques
        assertEquals(teacher1.hashCode(), anotherTeacher1.hashCode(),
                "Les hashCodes devraient être identiques pour les enseignants ayant le même ID");
    }

    @Test
    public void testHashCode_DifferentId() {
        // Assert: Vérifier que les hashcodes sont différents pour des IDs différents
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode(),
                "Les hashCodes devraient être différents pour les enseignants ayant des IDs différents");
    }

    @Test
    public void testEquals_SameId() {
        // Arrange: Créer un autre objet Teacher avec le même ID que teacher1
        Teacher anotherTeacher1 = new Teacher(1L, "Doe", "John", teacher1.getCreatedAt(), teacher1.getUpdatedAt());

        // Assert: Vérifier que les objets sont égaux
        assertTrue(teacher1.equals(anotherTeacher1), "Les enseignants ayant le même ID devraient être égaux");
    }

    @Test
    public void testEquals_DifferentId() {
        // Assert: Vérifier que les objets ne sont pas égaux car leurs IDs sont
        // différents
        assertFalse(teacher1.equals(teacher2), "Les enseignants ayant des IDs différents ne devraient pas être égaux");
    }

    @Test
    public void testEquals_NullAndDifferentClass() {
        // Assert: Vérifier que teacher1 n'est pas égal à null ou à un objet d'un autre
        // type
        assertFalse(teacher1.equals(null), "Un enseignant ne devrait pas être égal à null");
        assertFalse(teacher1.equals(new Object()), "Un enseignant ne devrait pas être égal à un objet d'un autre type");
    }

    @Test
    public void testSetCreatedAt() {
        // Arrange: Définir une nouvelle valeur pour createdAt
        LocalDateTime newCreatedAt = LocalDateTime.now().minusDays(1);

        // Act: Mettre à jour la date de création
        teacher1.setCreatedAt(newCreatedAt);

        // Assert: Vérifier que la date de création est correctement mise à jour
        assertEquals(newCreatedAt, teacher1.getCreatedAt(),
                "La date de création devrait être mise à jour avec la nouvelle valeur");
    }

    @Test
    public void testSetUpdatedAt() {
        // Arrange: Définir une nouvelle valeur pour updatedAt
        LocalDateTime newUpdatedAt = LocalDateTime.now().minusDays(1);

        // Act: Mettre à jour la date de mise à jour
        teacher1.setUpdatedAt(newUpdatedAt);

        // Assert: Vérifier que la date de mise à jour est correctement mise à jour
        assertEquals(newUpdatedAt, teacher1.getUpdatedAt(),
                "La date de mise à jour devrait être mise à jour avec la nouvelle valeur");
    }
}
