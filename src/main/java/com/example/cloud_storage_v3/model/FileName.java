package com.example.cloud_storage_v3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileName {
    @JsonProperty("filename")
    @NotNull(message = "Имя файла не ложно быть Null")
    @NotEmpty(message = "Имя файла не должно быть пустым")
    @NotBlank(message = "Имя файла не должно состоять из пробелов")
    private String name;
}
