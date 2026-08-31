package com.example.extensionblocker.repository;

import com.example.extensionblocker.domain.BlockedExtension;
import com.example.extensionblocker.domain.ExtensionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockedExtensionRepository extends JpaRepository<BlockedExtension, Long> {

    Optional<BlockedExtension> findByExtension(String extension);

    boolean existsByExtension(String extension);

    List<BlockedExtension> findByExtensionType(ExtensionType extensionType);

    long countByExtensionType(ExtensionType extensionType);
}
