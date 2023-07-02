package com.example.cloud_storage_v3.dto;

import com.example.cloud_storage_v3.entity.File;
import com.example.cloud_storage_v3.entity.Role;
import com.example.cloud_storage_v3.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO{
    private Long id;

    private String login;

    private String password;

    private Date created;

    private Date updated;

    private Role role;


    public User toUser() {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        userDTO.setCreated(user.getCreated());
        userDTO.setUpdated(user.getUpdated());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

}