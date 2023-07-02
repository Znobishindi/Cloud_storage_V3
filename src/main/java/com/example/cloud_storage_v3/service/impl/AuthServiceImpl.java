package com.example.cloud_storage_v3.service.impl;

import com.example.cloud_storage_v3.dto.AuthDTO;
import com.example.cloud_storage_v3.entity.User;
import com.example.cloud_storage_v3.exceptions.InvalidInputData;
import com.example.cloud_storage_v3.exceptions.UserNotFoundException;
import com.example.cloud_storage_v3.model.AuthToken;
import com.example.cloud_storage_v3.repository.UserRepository;
import com.example.cloud_storage_v3.security.JwtProvider;
import com.example.cloud_storage_v3.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthToken login(@NonNull AuthDTO authDTO) {
        log.info("Поиск пользователя в базе данных по логину: {}", authDTO.getLogin());
        User userFromDatabase = findUserInStorage(authDTO.getLogin());
        if (isEquals(authDTO, userFromDatabase)) {
            String accessToken = jwtProvider.generateAccessToken(userFromDatabase);
            return new AuthToken(accessToken);
        } else {
            throw new InvalidInputData("Неверный пароль", 0);
        }
    }

    private User findUserInStorage(String login) {
        return userRepository.findByLogin(login).orElseThrow(() ->
                new UserNotFoundException("Пользователь с таким логином не найден", 0));
    }

    private boolean isEquals(AuthDTO authDTO, User userFromDatabase) {
        return passwordEncoder.matches(authDTO.getPassword(), userFromDatabase.getPassword());
    }

    @Override
    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currUser = findUserInStorage(auth.getName());
        SecurityContextLogoutHandler securityContextLogoutHandler =
                new SecurityContextLogoutHandler();
        if (currUser != null) {
            securityContextLogoutHandler.logout(request, response, auth);
            return currUser.getLogin();
        }
        return null;
    }
}
