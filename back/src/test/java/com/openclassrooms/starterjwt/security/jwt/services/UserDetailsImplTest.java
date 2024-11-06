package com.openclassrooms.starterjwt.security.jwt.services;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

        @Test
        public void testEquals_SameInstance() {
                // Créer une instance de UserDetailsImpl
                UserDetailsImpl user = UserDetailsImpl.builder()
                                .id(1L)
                                .username("user@example.com")
                                .firstName("John")
                                .lastName("Doe")
                                .admin(false)
                                .password("password")
                                .build();

                // Vérifie que l'objet est égal à lui-même (this == o)
                assertTrue(user.equals(user), "L'instance devrait être égale à elle-même");
        }

        @Test
        public void testEquals_NullComparison() {
                // Créer une instance de UserDetailsImpl
                UserDetailsImpl user = UserDetailsImpl.builder()
                                .id(1L)
                                .username("user@example.com")
                                .firstName("John")
                                .lastName("Doe")
                                .admin(false)
                                .password("password")
                                .build();

                // Vérifie que la comparaison avec null retourne false
                assertFalse(user.equals(null), "L'instance ne devrait pas être égale à null");
        }

        @Test
        public void testEquals_DifferentClass() {
                // Créer une instance de UserDetailsImpl
                UserDetailsImpl user = UserDetailsImpl.builder()
                                .id(1L)
                                .username("user@example.com")
                                .firstName("John")
                                .lastName("Doe")
                                .admin(false)
                                .password("password")
                                .build();

                // Comparer avec un objet d'une classe différente
                String differentClassObject = "NotAUserDetailsImpl";

                // Vérifie que la comparaison avec un objet d'une autre classe retourne false
                assertFalse(user.equals(differentClassObject),
                                "L'instance ne devrait pas être égale à un objet d'une classe différente");
        }

        @Test
        public void testEquals_DifferentId() {
                // Créer deux instances de UserDetailsImpl avec des IDs différents
                UserDetailsImpl user1 = UserDetailsImpl.builder()
                                .id(1L)
                                .username("user1@example.com")
                                .firstName("John")
                                .lastName("Doe")
                                .admin(false)
                                .password("password1")
                                .build();

                UserDetailsImpl user2 = UserDetailsImpl.builder()
                                .id(2L)
                                .username("user2@example.com")
                                .firstName("Jane")
                                .lastName("Smith")
                                .admin(false)
                                .password("password2")
                                .build();

                // Vérifie que deux instances avec des IDs différents ne sont pas égales
                assertFalse(user1.equals(user2), "Deux instances avec des IDs différents ne devraient pas être égales");
        }

        @Test
        public void testAdminProperty() {
                // Créer une instance avec admin = true (administrateur)
                UserDetailsImpl adminUser = UserDetailsImpl.builder()
                                .id(1L)
                                .username("admin@example.com")
                                .firstName("Admin")
                                .lastName("User")
                                .admin(true)
                                .password("password")
                                .build();

                // Créer une instance avec admin = false (utilisateur non-administrateur)
                UserDetailsImpl regularUser = UserDetailsImpl.builder()
                                .id(2L)
                                .username("user@example.com")
                                .firstName("Regular")
                                .lastName("User")
                                .admin(false)
                                .password("password")
                                .build();

                // Vérifie que la propriété admin est true pour l'utilisateur administrateur
                assertTrue(adminUser.getAdmin(),
                                "La propriété admin devrait être true pour un utilisateur administrateur");

                // Vérifie que la propriété admin est false pour l'utilisateur
                // non-administrateur
                assertFalse(regularUser.getAdmin(),
                                "La propriété admin devrait être false pour un utilisateur non-administrateur");
        }

}
