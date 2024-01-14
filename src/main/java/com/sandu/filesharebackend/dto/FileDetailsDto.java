package com.sandu.filesharebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDetailsDto {
    private String fileId;

    private String fileName;

    private String fileType;

    private long fileSize;

    private String expirationDate;

}
