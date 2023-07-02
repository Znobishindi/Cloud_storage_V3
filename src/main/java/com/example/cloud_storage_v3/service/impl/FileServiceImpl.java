package com.example.cloud_storage_v3.service.impl;

import com.example.cloud_storage_v3.dto.FileDTO;
import com.example.cloud_storage_v3.entity.File;
import com.example.cloud_storage_v3.exceptions.FileNotFoundException;
import com.example.cloud_storage_v3.exceptions.InvalidInputData;
import com.example.cloud_storage_v3.model.FileName;
import com.example.cloud_storage_v3.repository.FileRepository;
import com.example.cloud_storage_v3.security.JwtProvider;
import com.example.cloud_storage_v3.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public void uploadFile(MultipartFile file, String fileName) {
        Long userId = jwtProvider.getAuthorizedUser().getId();
        log.info("Поиск файла в БД по названию {} и ID {}", fileName, userId);
        findFileNameInStorage(fileName, userId);
        String hash = null;
        byte[] fileBytes = null;
        try {
            hash = generateChecksum(file);
            fileBytes = file.getBytes();
            log.info("Сгенерирован хеш файла :{}", hash);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        log.info("Поиск файла в БД у пользователя с ID :{} и хешем :{}", userId, hash);
        fileRepository.findFileByUserIdAndHash(userId, hash).ifPresent(
                s -> {
                    throw new InvalidInputData("Этот файл уже существует. " +
                            "Пожалуйста выберите другой файл. Пользователь с ID", userId);
                });

        File createdFile = getBuild(file, fileName, userId, hash, fileBytes);
        log.info("Создание и сохранение файла в БД: {}", createdFile);

        fileRepository.save(createdFile);
    }

    @Transactional
    @Override
    public FileDTO downloadFile(String fileName) {
        Long userId = jwtProvider.getAuthorizedUser().getId();
        log.info("Поиск файла по его имени {} у пользователя с ID {}", fileName, userId);
        File fileFound = getFileFromStorage(fileName, userId);
        log.info("Получение файла: {} из хранилища для пользователя с Id: {}", fileName, userId);
        return FileDTO.builder()
                .fileName(fileFound.getFileName())
                .type(fileFound.getType())
                .fileByte(fileFound.getFileByte())
                .build();
    }

    @Override
    public void editFileName(String fileName, FileName name) {
        Long userId = jwtProvider.getAuthorizedUser().getId();
        log.info("Поиск файла в хранилище для редактирования" +
                " по имени файла: {} у пользователя с ID : {}", fileName, userId);
        File fileFoundForUpdate = getFileFromStorage(fileName, userId);
        log.info("Проверка на совпадение нового имени файла " +
                " {} у пользователя с ID {}", name.getName(), userId);
        findFileNameInStorage(name.getName(), userId);
        log.info("Редактирование файла в хранилище" +
                "с названием {} у пользователя с ID {}", fileFoundForUpdate, userId);
        fileFoundForUpdate.setFileName(name.getName());
        fileFoundForUpdate.setUpdated(new Date());
        log.info("сохранение файла в хранилище " +
                "с новым именем {} у пользовавтеля с ID {}", fileFoundForUpdate, userId);
        fileRepository.save(fileFoundForUpdate);
    }

    @Override
    public void deleteFile(String fileName) {
        Long userId = jwtProvider.getAuthorizedUser().getId();
        log.info("Поиск файла в БД для удаления" +
                " по имени: {} у пользователя с ID {}", fileName, userId);
        File fileFromStorage = getFileFromStorage(fileName, userId);
        log.info("Удаление файла из БД " +
                " по имени: {} у пользователя с ID {}", fileFromStorage, userId);
        fileRepository.deleteById(fileFromStorage.getId());
    }

    @Override
    public List<FileDTO> getAllFiles(int limit) {
        Long userId = jwtProvider.getAuthorizedUser().getId();
        log.info("Поиск всех файлов в БД" +
                "у пользователя с ID {} лимит: {}", userId, limit);
        List<File> filesByUserIdWithLimit =
                fileRepository.findFilesByUserIdWithLimit(userId, limit);
        log.info("Найдены все файлы в хранилище " +
                "у пользователя с ID {} лимит: {} | List<File>: {}", userId, limit, filesByUserIdWithLimit);
        return filesByUserIdWithLimit.stream()
                .map(file -> FileDTO.builder()
                        .fileName(file.getFileName())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());
    }

    private void findFileNameInStorage(String fileName, Long userId) {
        fileRepository.findFileByUserIdAndFileName(userId, fileName).ifPresent(s -> {
            throw new InvalidInputData("Файл с таким названием: {" + fileName + "} " +
                    "был найден. Id пользователя:{}. Смените имя файла и попробуйте снова", userId);
        });
        log.info("Файл с таким именем {} у пользователя с ID {} не нейден",
                fileName, userId);
    }

    private File getFileFromStorage(String fileName, Long userId) {
        return fileRepository.findFileByUserIdAndFileName(userId, fileName)
                .orElseThrow(() -> new FileNotFoundException(
                        "Файл с таким именем: " + fileName + " не найден у пользователя с ID: ", userId));
    }

    private File getBuild(MultipartFile file, String fileName, Long userId, String hash, byte[] fileBytes) {
        log.info("Получение файла: {} хеш: {} , ID пользователя: {}", fileName, hash, userId);
        File buildFile = new File(hash, fileName, file.getContentType(), file.getSize(), fileBytes, jwtProvider.getAuthorizedUser());
        return buildFile;
    }

    private String generateChecksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        try (InputStream fis = file.getInputStream()) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        log.info("Успешно сгенерирована контрольная сумма файла с именем: {}", file.getName());
        return result.toString();
    }
}
