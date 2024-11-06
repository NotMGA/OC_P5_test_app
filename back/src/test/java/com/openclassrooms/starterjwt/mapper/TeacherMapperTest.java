package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherMapperTest {

    // Utilisez l'instance réelle du mapper générée par MapStruct
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    public void testTeacherDtoToTeacher() {
        // **Arrange**: Initialiser un objet TeacherDto avec des données de test
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Smith");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // **Act**: Mapper l'objet TeacherDto en Teacher
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // **Assert**: Vérifier que l'objet Teacher est correctement mappé
        assertNotNull(teacher, "L'objet Teacher ne doit pas être nul");
        assertEquals(teacherDto.getId(), teacher.getId(), "Les IDs doivent correspondre");
        assertEquals(teacherDto.getLastName(), teacher.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt(),
                "Les dates de mise à jour doivent correspondre");
    }

    @Test
    public void testTeacherDtoListToTeacherList() {
        // **Arrange**: Initialiser une liste de TeacherDto avec des données de test
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Smith");
        teacherDto1.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setLastName("Doe");
        teacherDto2.setFirstName("Jane");

        List<TeacherDto> teacherDtoList = Arrays.asList(teacherDto1, teacherDto2);

        // **Act**: Mapper la liste de TeacherDto en liste de Teacher
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // **Assert**: Vérifier que la liste est correctement mappée
        assertNotNull(teacherList, "La liste de Teacher ne doit pas être nulle");
        assertEquals(2, teacherList.size(), "La taille de la liste de Teacher doit être de 2");
        assertEquals(teacherDto1.getId(), teacherList.get(0).getId(),
                "Le premier ID doit correspondre à celui de teacherDto1");
        assertEquals(teacherDto2.getId(), teacherList.get(1).getId(),
                "Le second ID doit correspondre à celui de teacherDto2");
    }

    @Test
    public void testTeacherToTeacherDto() {
        // **Arrange**: Initialiser un objet Teacher avec des données de test
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("Smith")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // **Act**: Mapper l'objet Teacher en TeacherDto
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // **Assert**: Vérifier que l'objet TeacherDto est correctement mappé
        assertNotNull(teacherDto, "L'objet TeacherDto ne doit pas être nul");
        assertEquals(teacher.getId(), teacherDto.getId(), "Les IDs doivent correspondre");
        assertEquals(teacher.getLastName(), teacherDto.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt(),
                "Les dates de mise à jour doivent correspondre");
    }

    @Test
    public void testTeacherListToTeacherDtoList() {
        // **Arrange**: Initialiser une liste de Teacher avec des données de test
        Teacher teacher1 = Teacher.builder().id(1L).lastName("Smith").firstName("John").build();
        Teacher teacher2 = Teacher.builder().id(2L).lastName("Doe").firstName("Jane").build();

        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        // **Act**: Mapper la liste de Teacher en liste de TeacherDto
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // **Assert**: Vérifier que la liste est correctement mappée
        assertNotNull(teacherDtoList, "La liste de TeacherDto ne doit pas être nulle");
        assertEquals(2, teacherDtoList.size(), "La taille de la liste de TeacherDto doit être de 2");
        assertEquals(teacher1.getId(), teacherDtoList.get(0).getId(),
                "Le premier ID doit correspondre à celui de teacher1");
        assertEquals(teacher2.getId(), teacherDtoList.get(1).getId(),
                "Le second ID doit correspondre à celui de teacher2");
    }
}
