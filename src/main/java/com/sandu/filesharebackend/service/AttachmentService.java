package com.sandu.filesharebackend.service;

import com.sandu.filesharebackend.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface AttachmentService {
    Attachment uploadAttachment(MultipartFile file, String password, int expirationDays) throws Exception;
    Object[] downloadAttachment(String id) throws Exception;
    String deleteAttachment(String fileId) throws FileNotFoundException;

    Attachment getFileDetails(String fileId, String password) throws FileNotFoundException;

    boolean checkFileLocked(String fileId) throws FileNotFoundException;

}
