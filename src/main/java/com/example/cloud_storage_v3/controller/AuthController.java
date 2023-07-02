package com.example.cloud_storage_v3.controller;

import com.example.cloud_storage_v3.dto.AuthDTO;
import com.example.cloud_storage_v3.model.AuthToken;
import com.example.cloud_storage_v3.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody AuthDTO authDTO) throws AuthException {
        AuthToken token = authService.login(authDTO);
        log.info("Успешный вход в систему пользователя с логином: {}. Выдан токен: {}", authDTO.getLogin(), token.getAuthToken());
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", description = "Success logout")
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @SecurityRequirement(name = "Bearer-token-auth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@NotNull @RequestHeader(value = "auth-token") String authToken,
                                       HttpServletRequest request, HttpServletResponse response) {
        String logoutLoginUser = authService.logout(authToken, request, response);
        if (logoutLoginUser != null) {
            log.info("Успешный выход из системы пользователя с логином: {} токен: {} ", logoutLoginUser, authToken);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
