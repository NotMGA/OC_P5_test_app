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
        user1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false, LocalDateTime.now(),
                LocalDateTime.now());
        user2 = new User(2L, "test2@test.com", "testlm2", "testfn2", "test123test2", false, LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    public void testToString() {
        String expected = "User(id=1, email=test@test.com, lastName=testlm, firstName=testfn, password=test123test, admin=false, createdAt="
                + user1.getCreatedAt()
                + ", updatedAt=" + user1.getUpdatedAt() + ")";
        assertEquals(expected, user1.toString());
    }

    @Test
    public void testHashCode() {
        User anotheruser1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false,
                user1.getCreatedAt(), user1.getUpdatedAt());
        assertEquals(user1.hashCode(), anotheruser1.hashCode());

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEquals() {
        User anotheruser1 = new User(1L, "test@test.com", "testlm", "testfn", "test123test", false,
                user1.getCreatedAt(), user1.getUpdatedAt());

        assertTrue(user1.equals(anotheruser1)); // Objects should be equal
        assertFalse(user1.equals(user2)); // Different teachers
        assertFalse(user1.equals(null)); // Null case
        assertFalse(user1.equals(new Object())); // Different type
    }

}
