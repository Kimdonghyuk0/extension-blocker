package com.example.extensionblocker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * A single blocked-extension policy row.
 *
 * <p>One table holds both FIXED and CUSTOM extensions. The {@code extension} value is stored
 * normalized (lowercase, no dot) and is UNIQUE, so a custom entry can never collide with a
 * fixed one. FIXED rows always exist (seeded) and toggle via {@code blocked}; CUSTOM rows are
 * inserted/deleted and are considered blocked whenever present.
 */
@Entity
@Table(name = "blocked_extension")
public class BlockedExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "extension", nullable = false, unique = true, length = 20)
    private String extension;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private ExtensionType extensionType;

    /** For FIXED rows: whether the box is checked. CUSTOM rows are always true. */
    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected BlockedExtension() {
    }

    public BlockedExtension(String extension, ExtensionType extensionType, boolean blocked) {
        this.extension = extension;
        this.extensionType = extensionType;
        this.blocked = blocked;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getExtension() {
        return extension;
    }

    public ExtensionType getExtensionType() {
        return extensionType;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
