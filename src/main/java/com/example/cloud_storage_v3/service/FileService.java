package com.example.cloud_storage_v3.service;

import com.example.cloud_storage_v3.dto.FileDTO;
import com.example.cloud_storage_v3.model.FileName;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void uploadFile(MultipartFile file, String fileName);

    FileDTO downloadFile(String fileName);

    void editFileName(String fileName, FileName name);

    void deleteFile(String fileName);

    List<FileDTO> getAllFiles(int limit);
}

