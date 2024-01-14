package com.sandu.filesharebackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table
public class Attachment {
    @Id
    private String id;

    private String fileName;

    private String fileType;

    private String newFileName;

    private Long size;

    private LocalDateTime uploadedDate;

    private LocalDateTime expirationDate;

    private int downloadCounter;

    private String password;

    private String url;

    private boolean expired = false;



    public Attachment(String fileName, String fileType, Long size, String password, int expirationDays) {
        this.id = String.valueOf(UUID.randomUUID());
        this.fileName = fileName;
        this.fileType = fileType;
        this.password = password;
        this.size = size;
        this.uploadedDate = LocalDateTime.now();
        this.expirationDate = this.uploadedDate.plusDays(expirationDays);
    }
}
