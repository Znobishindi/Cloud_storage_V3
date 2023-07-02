package com.example.cloud_storage_v3.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.cloud_storage_v3.dto.FileDTO;
import com.example.cloud_storage_v3.exceptions.FileNotFoundException;
import com.example.cloud_storage_v3.model.FileName;
import com.example.cloud_storage_v3.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cloud", description = "File management")
@SecurityRequirement(name = "Bearer-token-auth")
@RequestMapping("/")
public class FileController {

    private final FileService fileService;

    public static final String REGEXP_NAME = "\\w";


    @Operation(summary = "Upload file to server")
    @ApiResponse(responseCode = "200", description = "Success upload")
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error upload file",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/file",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(@NotNull @RequestPart("file") MultipartFile file,
                                           @Pattern(regexp = REGEXP_NAME)
                                           @RequestParam("filename") String fileName) {
        log.info("Проверка наличия файла в запросе: {}", !file.isEmpty());
        if (file.isEmpty()) {
            throw new FileNotFoundException("Файл не прикреплен", 0);
        }
        log.info("Загрузка файла на сервер: {}", fileName);
        fileService.uploadFile(file, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Download file from cloud")
    @ApiResponse(responseCode = "200", description = "Success download file",
            content = {@Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)})
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "File not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error download file",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/file",
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> downloadFile(@Pattern(regexp = REGEXP_NAME)
                                               @RequestParam("filename") String fileName) {
        log.info("Скачивание файла: {}", fileName);
        FileDTO fileDto = fileService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getFileByte());
    }

    @Operation(summary = "Edit file name")
    @ApiResponse(responseCode = "200", description = "Success edited file name")
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "File not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error edit file name",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/file")
    public ResponseEntity<Void> editFileName(@Pattern(regexp = REGEXP_NAME)
                                             @RequestParam("filename") String fileName,
                                             @Valid @RequestBody FileName name) {
        log.info("Изменение файла с именем: {} на новое: {}", fileName, name.getName());
        fileService.editFileName(fileName, name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete file from cloud")
    @ApiResponse(responseCode = "200", description = "Success deleted file")
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "File not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error delete file",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@Pattern(regexp = REGEXP_NAME)
                                           @RequestParam("filename") String fileName) {
        log.info("Удаление файла: {}", fileName);
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get list all files")
    @ApiResponse(responseCode = "200", description = "Success get list",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FileDTO.class))})
    @ApiResponse(responseCode = "400", description = "Error input data",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "403", description = "Unauthorized error",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "404", description = "File not found",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @ApiResponse(responseCode = "500", description = "Error getting file list",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/list")
    public ResponseEntity<List<FileDTO>> getAllFiles(@Min(1) @RequestParam int limit) {
        log.info("Получение списка всех файлов. Лимит: {}", limit);
        return new ResponseEntity<>(fileService.getAllFiles(limit), HttpStatus.OK);
    }
}