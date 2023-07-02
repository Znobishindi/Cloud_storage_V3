package com.example.cloud_storage_v3.service.impl;

import com.example.cloud_storage_v3.dto.FileDTO;
import com.example.cloud_storage_v3.entity.File;
import com.example.cloud_storage_v3.entity.Role;
import com.example.cloud_storage_v3.entity.User;
import com.example.cloud_storage_v3.model.FileName;
import com.example.cloud_storage_v3.repository.FileRepository;
import com.example.cloud_storage_v3.security.JwtProvider;
import com.example.cloud_storage_v3.service.FileService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FileServiceImplTest {
    private static final String FILE_NAME = "TEST.txt";

    @Autowired
    private FileService fileService;
    @MockBean
    private FileRepository fileRepository;
    @MockBean
    private JwtProvider jwtProvider;

    private User user;
    private File fileFound;

    @BeforeEach
    public void setStartingData() {
        user = User.builder().login("test@test.ru")
                .password("1234567")
                .role(Role.ROLE_USER)
                .build();
        user.setId(1L);

        fileFound = File.builder()
                .id(5L)
                .fileName(FILE_NAME)
                .hash("sfw435tdmj78ss45453mhs566jf44ggw")
                .type(MediaType.TEXT_PLAIN_VALUE)
                .size(19L)
                .fileByte("Cloud_storage_v3".getBytes())
                .user(user)
                .build();
        fileFound.setCreated(new Date());
    }

    @SneakyThrows
    @Test
    void uploadFileTest() {
        String hash = "sfw435tdmj78ss45453mhs566jf44ggw";
        MultipartFile multipartFile =
                new MockMultipartFile("file", FILE_NAME,
                        MediaType.TEXT_PLAIN_VALUE, "Cloud_storage_v3".getBytes());
        Mockito.when(jwtProvider.getAuthorizedUser()).thenReturn(user);
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, FILE_NAME))
                .thenReturn(Optional.empty());
        Mockito.when(fileRepository.findFileByUserIdAndHash(1L, hash))
                .thenReturn(Optional.empty());
        File createdFile = new File(5L, hash, FILE_NAME, multipartFile.getContentType(), multipartFile.getSize(), multipartFile.getBytes(), user);

        fileService.uploadFile(multipartFile, FILE_NAME);

        Assertions.assertEquals(16L, createdFile.getSize());
    }

    @Test
    void downloadFileTest() {
        Mockito.when(jwtProvider.getAuthorizedUser()).thenReturn(user);
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, FILE_NAME))
                .thenReturn(Optional.ofNullable(fileFound));

        FileDTO downloadFile = fileService.downloadFile(FILE_NAME);

        Assertions.assertEquals(FILE_NAME, downloadFile.getFileName());
    }

    @Test
    void editFileNameTest() {
        FileName newName = new FileName("Name.txt");
        Mockito.when(jwtProvider.getAuthorizedUser()).thenReturn(user);
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, FILE_NAME))
                .thenReturn(Optional.ofNullable(fileFound));

        fileService.editFileName(FILE_NAME, newName);

        Mockito.verify(fileRepository,
                Mockito.times(1)).save(fileFound);
    }

    @Test
    void deleteFileTest() {
        Mockito.when(jwtProvider.getAuthorizedUser()).thenReturn(user);
        Mockito.when(fileRepository.findFileByUserIdAndFileName(1L, FILE_NAME))
                .thenReturn(Optional.ofNullable(fileFound));

        fileService.deleteFile(FILE_NAME);

        Mockito.verify(fileRepository,
                Mockito.times(1)).deleteById(fileFound.getId());
    }

    @Test
    void getAllFiles() {
        int limit = 4;
        List<File> listFile = List.of(
                File.builder().size(1143L).fileName("new_file1.txt").build(),
                File.builder().size(12372L).fileName("new_file2.txt").build(),
                File.builder().size(2433L).fileName("new_file3.txt").build(),
                File.builder().size(14513L).fileName("new_file4.txt").build());

        Mockito.when(jwtProvider.getAuthorizedUser()).thenReturn(user);
        Mockito.when(fileRepository.findFilesByUserIdWithLimit(user.getId(), limit))
                .thenReturn(listFile);

        List<FileDTO> files = fileService.getAllFiles(limit);

        Assertions.assertEquals("new_file1.txt", files.get(0).getFileName());
    }
}