package com.example.extensionblocker.domain;

/**
 * 업로드 처리 결과. upload_history.status 에 문자열로 저장된다.
 * STORED   — 검증 통과 후 저장됨.
 * REJECTED — 정책/내용 검증에 걸려 거부됨 (reject_code 에 사유).
 */
public enum UploadStatus {
    STORED,
    REJECTED
}
