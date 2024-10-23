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
        teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void testToString() {
        String expected = "Teacher(id=1, lastName=Doe, firstName=John, createdAt=" + teacher1.getCreatedAt()
                + ", updatedAt=" + teacher1.getUpdatedAt() + ")";
        assertEquals(expected, teacher1.toString());
    }

    @Test
    public void testHashCode() {
        Teacher anotherTeacher1 = new Teacher(1L, "Doe", "John", teacher1.getCreatedAt(), teacher1.getUpdatedAt());
        assertEquals(teacher1.hashCode(), anotherTeacher1.hashCode());

        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    public void testEquals() {
        Teacher anotherTeacher1 = new Teacher(1L, "Doe", "John", teacher1.getCreatedAt(), teacher1.getUpdatedAt());

        assertTrue(teacher1.equals(anotherTeacher1)); // Objects should be equal
        assertFalse(teacher1.equals(teacher2)); // Different teachers
        assertFalse(teacher1.equals(null)); // Null case
        assertFalse(teacher1.equals(new Object())); // Different type
    }

    @Test
    public void testSetCreatedAt() {
        LocalDateTime newCreatedAt = LocalDateTime.now().minusDays(1);
        teacher1.setCreatedAt(newCreatedAt);
        assertEquals(newCreatedAt, teacher1.getCreatedAt());
    }

    @Test
    public void testSetUpdatedAt() {
        LocalDateTime newUpdatedAt = LocalDateTime.now().minusDays(1);
        teacher1.setUpdatedAt(newUpdatedAt);
        assertEquals(newUpdatedAt, teacher1.getUpdatedAt());
    }

    @Test
    public void testBuilder() {
        Teacher teacher = Teacher.builder()
                .id(3L)
                .lastName("Brown")
                .firstName("Charlie")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertEquals(3L, teacher.getId());
        assertEquals("Brown", teacher.getLastName());
        assertEquals("Charlie", teacher.getFirstName());
    }
}
