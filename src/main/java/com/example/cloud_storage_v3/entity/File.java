package com.example.cloud_storage_v3.entity;

import com.example.cloud_storage_v3.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
@Builder

public class File extends BaseEntity {


    public File(String hash, String fileName, String type, Long size, byte[] fileByte, User user){
        this.hash = hash;
        this.fileName = fileName;
        this.type = type;
        this.size = size;
        this.fileByte = fileByte;
        super.setUpdated(new Date());
        super.setCreated(new Date());
        this.user = user;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hash;

    private String fileName;

    private String type;

    private Long size;

    private byte[] fileByte;


    @Override
    public void setCreated(Date created) {
        super.setCreated(created);
    }

    @Override
    public void setUpdated(Date updated) {
        super.setUpdated(updated);
    }



    @ManyToOne(fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "user_id",
            nullable = false)
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        File file = (File) o;
        return id != null && Objects.equals(id, file.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
