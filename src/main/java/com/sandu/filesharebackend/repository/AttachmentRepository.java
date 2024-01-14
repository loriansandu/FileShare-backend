package com.sandu.filesharebackend.repository;

import com.sandu.filesharebackend.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    Optional<List<Attachment>> findAttachmentsByExpirationDateBeforeAndExpired(LocalDateTime expirationDate, boolean expired);
}
