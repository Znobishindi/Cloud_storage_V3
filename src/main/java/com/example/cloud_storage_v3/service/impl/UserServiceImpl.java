package com.example.cloud_storage_v3.service.impl;

import com.example.cloud_storage_v3.dto.UserDTO;
import com.example.cloud_storage_v3.entity.Role;
import com.example.cloud_storage_v3.entity.User;
import com.example.cloud_storage_v3.exceptions.UserNotFoundException;
import com.example.cloud_storage_v3.repository.UserRepository;
import com.example.cloud_storage_v3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User newUser = userDTO.toUser();
        if (isThereAUser(userDTO.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setLogin(userDTO.getLogin());
        newUser.setCreated(new Date());
        newUser.setUpdated(new Date());
        newUser.setRole(Role.ROLE_USER);
        return UserDTO.fromUser(userRepository.save(newUser));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ExpressionException("Пользователя с таким id" + id + " нет в базе данных"));
        UserDTO foundedUser = UserDTO.fromUser(user);
        return foundedUser;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long id) {
        User user = userDTO.toUser();
        User updateUser = userRepository.findById(id).map(foundedUser -> {
            foundedUser.setLogin(user.getLogin());
            foundedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            foundedUser.setUpdated(new Date());
            return foundedUser;
        }).orElseThrow(() -> new RuntimeException("Пользователя с таким id" + id + " нет в базе данных"));
        return UserDTO.fromUser(userRepository.save(updateUser));
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Пользователя с таким id " + id + " нет в базе данных", 0));
        userRepository.delete(user);
    }

    @Override
    public User findUserByLogin(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new ExpressionException("Пользователя с таким логином нет в базе данных"));
        return user;
    }

    private boolean isThereAUser(String login) {
        if (userRepository.existsUserByLogin(login)) {
            return true;
        }
        return false;
    }
}
