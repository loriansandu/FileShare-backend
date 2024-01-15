package com.sandu.filesharebackend.controller;

import com.sandu.filesharebackend.dto.FileDetailsDto;
import com.sandu.filesharebackend.dto.PasswordDto;
import com.sandu.filesharebackend.dto.ResponseDto;
import com.sandu.filesharebackend.model.Attachment;
import com.sandu.filesharebackend.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseDto uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("password") String password, @RequestParam("expirationDays") int expirationDays ) throws Exception {
        Attachment attachment = attachmentService.uploadAttachment(file, password, expirationDays);


        return new ResponseDto(attachment.getId(),
                attachment.getPassword(),
                attachment.getFileName(),
                attachment.getUrl(),
                attachment.getFileType(),
                attachment.getSize(),
                attachment.getUploadedDate().toString(),
                attachment.getExpirationDate().toString(),
                attachment.getDownloadCounter()
        );
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Object[] response = attachmentService.downloadAttachment(fileId);
        Attachment attachment = (Attachment) response[0];
        Resource resource = (Resource) response[1];
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name",  attachment.getFileName());
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;filename=" + attachment.getFileName());
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .headers(httpHeaders)
                .body((resource));
    }

    @PostMapping("/{fileId}")
    public FileDetailsDto getFileDetails(@PathVariable String fileId, @RequestBody PasswordDto passwordDto) throws Exception {
        Attachment attachment = attachmentService.getFileDetails(fileId, passwordDto.getPassword());
        return new FileDetailsDto(attachment.getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getSize(),
                attachment.getExpirationDate().toString());

    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws Exception {
        String id = attachmentService.deleteAttachment(fileId);
        return ResponseEntity.ok("{\"message\": \"" + id + "\"}");
    }

    @GetMapping("/is-locked")
    public ResponseEntity<String> checkFileLocked(@RequestParam String fileId) throws Exception {
        boolean isLocked = attachmentService.checkFileLocked(fileId);
        return ResponseEntity.ok("{\"message\": \"" + isLocked + "\"}");
    }
}
