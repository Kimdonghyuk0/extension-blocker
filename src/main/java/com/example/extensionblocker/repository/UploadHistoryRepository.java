package com.example.extensionblocker.repository;

import com.example.extensionblocker.domain.UploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory, Long> {
}
