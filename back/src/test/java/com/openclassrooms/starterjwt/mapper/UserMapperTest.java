package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Initialisation de l'instance UserMapper
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testUserDtoToUser() {
        // **Arrange**: Créer un UserDto avec des valeurs fictives
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password123");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        // **Act**: Mapper UserDto vers User
        User user = userMapper.toEntity(userDto);

        // **Assert**: Vérifier que User a les mêmes valeurs que UserDto
        assertNotNull(user, "L'objet User ne doit pas être nul");
        assertEquals(userDto.getId(), user.getId(), "Les IDs doivent correspondre");
        assertEquals(userDto.getEmail(), user.getEmail(), "Les adresses email doivent correspondre");
        assertEquals(userDto.getFirstName(), user.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(userDto.getLastName(), user.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(userDto.getPassword(), user.getPassword(), "Les mots de passe doivent correspondre");
        assertEquals(userDto.isAdmin(), user.isAdmin(), "Le statut admin doit correspondre");
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt(), "Les dates de mise à jour doivent correspondre");
    }

    @Test
    void testUserToUserDto() {
        // **Arrange**: Créer un User avec des valeurs fictives
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // **Act**: Mapper User vers UserDto
        UserDto userDto = userMapper.toDto(user);

        // **Assert**: Vérifier que UserDto a les mêmes valeurs que User
        assertNotNull(userDto, "L'objet UserDto ne doit pas être nul");
        assertEquals(user.getId(), userDto.getId(), "Les IDs doivent correspondre");
        assertEquals(user.getEmail(), userDto.getEmail(), "Les adresses email doivent correspondre");
        assertEquals(user.getFirstName(), userDto.getFirstName(), "Les prénoms doivent correspondre");
        assertEquals(user.getLastName(), userDto.getLastName(), "Les noms de famille doivent correspondre");
        assertEquals(user.getPassword(), userDto.getPassword(), "Les mots de passe doivent correspondre");
        assertEquals(user.isAdmin(), userDto.isAdmin(), "Le statut admin doit correspondre");
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt(), "Les dates de création doivent correspondre");
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt(), "Les dates de mise à jour doivent correspondre");
    }

    @Test
    void testUserDtoListToUserList() {
        // **Arrange**: Créer une liste de UserDto avec des valeurs fictives
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("test1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("password1");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("test2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Doe");
        userDto2.setPassword("password2");

        List<UserDto> userDtoList = Arrays.asList(userDto1, userDto2);

        // **Act**: Mapper la liste de UserDto vers une liste de User
        List<User> userList = userMapper.toEntity(userDtoList);

        // **Assert**: Vérifier que la liste est correctement mappée
        assertNotNull(userList, "La liste de User ne doit pas être nulle");
        assertEquals(2, userList.size(), "La taille de la liste de User doit être de 2");
        assertEquals(userDto1.getId(), userList.get(0).getId(), "Le premier ID doit correspondre à celui de userDto1");
        assertEquals(userDto2.getId(), userList.get(1).getId(), "Le second ID doit correspondre à celui de userDto2");
    }

    @Test
    void testUserListToUserDtoList() {
        // **Arrange**: Créer une liste de User avec des valeurs fictives
        User user1 = User.builder()
                .id(1L)
                .email("test1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password1")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("test2@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("password2")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        // **Act**: Mapper la liste de User vers une liste de UserDto
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // **Assert**: Vérifier que la liste est correctement mappée
        assertNotNull(userDtoList, "La liste de UserDto ne doit pas être nulle");
        assertEquals(2, userDtoList.size(), "La taille de la liste de UserDto doit être de 2");
        assertEquals(user1.getId(), userDtoList.get(0).getId(), "Le premier ID doit correspondre à celui de user1");
        assertEquals(user2.getId(), userDtoList.get(1).getId(), "Le second ID doit correspondre à celui de user2");
    }
}
