package com.example.cloud_storage_v3.controller;

import com.example.cloud_storage_v3.dto.UserDTO;
import com.example.cloud_storage_v3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer-token-auth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Creating a user")
    @ApiResponse(responseCode = "200", description = "Success User created",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDTO.class))})
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error creating user",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto) {
        log.info("Создание нового пользователя с логином: {}", userDto.getLogin());
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.OK);
    }


    @Operation(summary = "Update user by ID")
    @ApiResponse(responseCode = "200", description = "Success User updated",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDTO.class))})
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "User not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error update user",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDto,
                                              @NotNull @PathVariable Long id) {
        log.info("Обновление пользователя с ID: {}. Новые данные: {}", id, userDto);
        return new ResponseEntity<>(userService.updateUser(userDto, id),
                HttpStatus.OK);


    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "Success User found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserDTO.class))})
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "User not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error getting user",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@NotNull @PathVariable Long id) {
        log.info("Получение пользователя по ID: {}", id);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @Operation(summary = "Delete user by ID")
    @ApiResponse(responseCode = "200", description = "Success deleted user")
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "User not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error delete user",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@NotNull @PathVariable Long id) {
        log.info("Удаление пользователя по ID: {}", id);
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
