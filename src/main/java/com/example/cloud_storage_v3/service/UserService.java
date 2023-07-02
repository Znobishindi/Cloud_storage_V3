package com.example.cloud_storage_v3.service;

import com.example.cloud_storage_v3.dto.UserDTO;
import com.example.cloud_storage_v3.entity.User;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    UserDTO updateUser(UserDTO userDTO, Long id);

    void deleteUserById(Long id);

    User findUserByLogin(String login);
}
