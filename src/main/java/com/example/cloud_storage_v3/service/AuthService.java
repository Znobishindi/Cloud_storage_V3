package com.example.cloud_storage_v3.service;

import com.example.cloud_storage_v3.dto.AuthDTO;
import com.example.cloud_storage_v3.model.AuthToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

public interface AuthService {

    public AuthToken login(@NonNull AuthDTO authDTO);

    public String logout(String authToken, HttpServletRequest request,
                         HttpServletResponse response);


}
