package com.sandu.filesharebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {
    private String fileId;

    private String password;

    private String fileName;

    private String downloadURL;

    private String fileType;

    private long fileSize;

    private String uploadDate;

    private String expirationDate;

    private int downloadCounter;

}
