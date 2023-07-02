package com.example.cloud_storage_v3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    @NonNull
    @NotEmpty
    @NotBlank
//    @Size(min = 5,max = 15, message = "Должно быть минимум 5 и максимум 15 символов")
    private String login;

    @NonNull
    @NotEmpty
    @NotBlank
//    @Size(min = 5,max = 15, message = "Должно быть минимум 5 и максимум 15 символов")
    private String password;
}
