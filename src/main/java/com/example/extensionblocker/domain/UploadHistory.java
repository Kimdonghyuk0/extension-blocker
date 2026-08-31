package com.example.extensionblocker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * 업로드 시도 이력 (TS-001 §1, §2). 성공/거부 모두 기록한다.
 *
 * <p>정책 테이블과 FK 로 묶지 않는다. 판정 시점의 값(원본명, 선언 타입, 실제 감지 타입,
 * 걸린 확장자 등)을 그대로 복사해 가지므로, 정책 행이 나중에 삭제돼도 "그때 왜 막혔는지" 가 남는다.
 * 도메인 규칙(판정)은 순수 함수 쪽에 있고, 이 엔티티는 결과 기록만 담당한다.
 */
@Entity
@Table(name = "upload_history")
public class UploadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 정규화 전 원본 파일명. 표시할 때는 제어문자 제거 후 textContent 로만 렌더한다(§6-3). */
    @Column(name = "original_name", nullable = false)
    private String originalName;

    /** 저장명(UUID, 확장자 없음). 거부 시 null. */
    @Column(name = "stored_name", length = 36)
    private String storedName;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    /** 클라이언트가 보낸 Content-Type. 판정에는 쓰지 않고 기록만 한다(§3-3). */
    @Column(name = "declared_type", length = 255)
    private String declaredType;

    /** 시그니처로 감지한 실제 타입. */
    @Column(name = "detected_type", length = 32)
    private String detectedType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private UploadStatus status;

    /** 거부 사유 코드(§5-1). 성공 시 null. */
    @Column(name = "reject_code", length = 32)
    private String rejectCode;

    /** 정책에 걸린 확장자. 확장자 외 사유(내용 등)면 null. */
    @Column(name = "matched_ext", length = 20)
    private String matchedExt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected UploadHistory() {
    }

    private UploadHistory(String originalName, String storedName, long sizeBytes, String declaredType,
                          String detectedType, UploadStatus status, String rejectCode, String matchedExt) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.sizeBytes = sizeBytes;
        this.declaredType = declaredType;
        this.detectedType = detectedType;
        this.status = status;
        this.rejectCode = rejectCode;
        this.matchedExt = matchedExt;
    }

    /** 검증 통과 후 저장된 파일. */
    public static UploadHistory stored(String originalName, String storedName, long sizeBytes,
                                       String declaredType, String detectedType) {
        return new UploadHistory(originalName, storedName, sizeBytes, declaredType, detectedType,
                UploadStatus.STORED, null, null);
    }

    /** 정책/내용 검증에 걸려 거부된 파일. */
    public static UploadHistory rejected(String originalName, long sizeBytes, String declaredType,
                                         String detectedType, String rejectCode, String matchedExt) {
        return new UploadHistory(originalName, null, sizeBytes, declaredType, detectedType,
                UploadStatus.REJECTED, rejectCode, matchedExt);
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getDeclaredType() {
        return declaredType;
    }

    public String getDetectedType() {
        return detectedType;
    }

    public UploadStatus getStatus() {
        return status;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public String getMatchedExt() {
        return matchedExt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
