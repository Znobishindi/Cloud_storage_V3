package com.example.cloud_storage_v3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO {
    @JsonProperty("filename")
    private String fileName;
    private Long size;
    private String hash;
    private byte[] fileByte;
    private String type;
}