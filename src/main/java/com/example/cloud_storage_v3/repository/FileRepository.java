package com.example.cloud_storage_v3.repository;

import com.example.cloud_storage_v3.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findFileByUserIdAndHash(Long id, String hash);


    Optional<File> findFileByUserIdAndFileName(Long aLong, String fileName);

    @Query(value = "select * from files s where s.user_id = ?1 order by s.id desc limit ?2", nativeQuery = true)
    List<File> findFilesByUserIdWithLimit(Long userId, int limit);
}
