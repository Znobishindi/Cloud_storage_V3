package com.example.cloud_storage_v3.service.impl;

import com.example.cloud_storage_v3.dto.UserDTO;
import com.example.cloud_storage_v3.entity.Role;
import com.example.cloud_storage_v3.entity.User;
import com.example.cloud_storage_v3.exceptions.UserNotFoundException;
import com.example.cloud_storage_v3.repository.UserRepository;
import com.example.cloud_storage_v3.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(UserDTO.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private static User user;
    @MockBean
    public UserDTO userDTO;

    @BeforeEach
    public void setStartingData() {
        user = User.builder()
                .login("test@test.ru")
                .password("1234567")
                .role(Role.ROLE_USER)
                .build();
        user.setId(1L);
        userDTO = UserDTO.fromUser(user);
    }



    @Test
    void getUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        userService.getUserById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }



    @Test
    void deleteUserByIdTest() {
       Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        userService.deleteUserById(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void findUserByLogin() {
        Mockito.when(userRepository
                .findByLogin("test@test.ru")).thenReturn(Optional.ofNullable(user));

        userService.findUserByLogin("test@test.ru");

        Mockito.verify(userRepository,
                Mockito.times(1)).findByLogin("test@test.ru");
    }


    @Test
    public void deleteUserByIdAndUserNotFoundExceptionTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(1L);
        });

        String expectedMessage = "Пользователя с таким id 1 нет в базе данных";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}