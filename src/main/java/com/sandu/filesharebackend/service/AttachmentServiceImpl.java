package com.sandu.filesharebackend.service;

import com.sandu.filesharebackend.exception.*;
import com.sandu.filesharebackend.model.Attachment;
import com.sandu.filesharebackend.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements  AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Value("${frontend.url}")
    private String frontEndURL;

    @Value("${file.max-size}")
    private long FILE_MAXSIZE;
    public static final String DIRECTORY = System.getProperty("user.home");

    @Override
    public Attachment uploadAttachment(MultipartFile file, String password, int expirationDays) {
        if (file.getSize() > (FILE_MAXSIZE * 1024 * 1024))
            throw new InvalidFileSizeException("File size exceeds limit ");
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new InvalidFileNameException("Filename contains invalid path sequence " + fileName);
            }
            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getSize(), password, expirationDays);
            String downloadURl = frontEndURL + "/downloads/" + attachment.getId();
            String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";

            attachment.setNewFileName(attachment.getId() + extension);
            attachment.setUrl(downloadURl);
            attachmentRepository.save(attachment);

            Path fileStorage = get(DIRECTORY, attachment.getId() + extension).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            return attachment;

        } catch (IOException exception) {
            throw new FileNotSavedException("File could not be saved");
        }
    }

    @Override
    public Object[] downloadAttachment(String id) throws Exception {
        Object[] result = new Object[2];
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));
        result[0] = attachment;
        var today = LocalDateTime.now();
        if (!attachment.getExpirationDate().isAfter(today))
            throw new FileExpiredException("File is expired with id: " + attachment.getId());
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(attachment.getNewFileName());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(attachment.getNewFileName() + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        result[1] = resource;
        attachment.setDownloadCounter(attachment.getDownloadCounter() + 1);
        attachmentRepository.save(attachment);
        return result;
    }

    @Override
    public String deleteAttachment(String id) throws FileNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(attachment.getNewFileName());
        attachmentRepository.delete(attachment);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new FileNotDeletedException("File could not be deleted with id: " + id);
        }
        return id;
    }

    @Override
    public Attachment getFileDetails(String id, String password) throws FileNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));
        var today = LocalDateTime.now();
        if (!attachment.getExpirationDate().isAfter(today))
            throw new FileExpiredException("File is expired with id: " + attachment.getId());
        if (!attachment.getPassword().equals(password)) {
            throw new WrongPasswordException("Wrong password for file with id: " + id);
        }
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(attachment.getNewFileName());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(attachment.getNewFileName() + " was not found on the server");
        }
        return attachment;
    }

    @Override
    public boolean checkFileLocked(String id) throws FileNotFoundException {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found with id: " + id));
        System.out.println(attachment.getPassword());
        return !attachment.getPassword().isEmpty();
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
//    @Scheduled(cron = "0 * * * * ?") // Run every minute
    public void processExpiredAttachments() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Attachment> expiredAttachments = attachmentRepository.findAttachmentsByExpirationDateBeforeAndExpired(currentDate, false).get();

        for (Attachment attachment : expiredAttachments) {
            attachment.setExpired(true);
            attachment.setUrl(null);
            attachmentRepository.save(attachment);
            Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(attachment.getNewFileName());
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new FileNotDeletedException("File could not be deleted with id: " + attachment.getId());
            }
        }
    }


}
