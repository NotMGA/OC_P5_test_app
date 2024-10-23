package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'un enseignant pour les tests
        teacher = new Teacher()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe");
    }

    @Test
    void testFindAllTeachers() {
        Teacher teacher2 = new Teacher().setId(2L).setFirstName("Jane").setLastName("Smith");

        // Simulation du comportement de findAll
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher, teacher2));

        // Appel de la méthode findAll
        List<Teacher> teachers = teacherService.findAll();

        // Vérifie que deux enseignants ont été trouvés
        assertEquals(2, teachers.size());
        assertTrue(teachers.contains(teacher));
        assertTrue(teachers.contains(teacher2));

        // Vérification que findAll a été appelée une fois
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testFindById_TeacherExists() {
        Long teacherId = 1L;

        // Simulation de la récupération d'un enseignant existant
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // Appel de la méthode findById
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Vérification que l'enseignant est bien celui attendu
        assertNotNull(foundTeacher);
        assertEquals(teacherId, foundTeacher.getId());
        assertEquals("John", foundTeacher.getFirstName());

        // Vérification que findById a été appelée une fois avec le bon ID
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void testFindById_TeacherNotFound() {
        Long teacherId = 1L;

        // Simulation du cas où l'enseignant n'existe pas
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // Appel de la méthode findById
        Teacher foundTeacher = teacherService.findById(teacherId);

        // Vérification que la méthode renvoie null lorsque l'enseignant n'est pas
        // trouvé
        assertNull(foundTeacher);

        // Vérification que findById a été appelée une fois avec le bon ID
        verify(teacherRepository, times(1)).findById(teacherId);
    }

}
