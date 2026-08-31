-- TS-001 §2-1. 정책 + 업로드 이력 스키마.
-- 두 테이블 사이 FK 없음: upload_history 는 판정 시점 정책을 값으로 복사해 가진다
-- (정책 행이 삭제돼도 "그때 왜 막혔는지" 가 남아야 하므로).

CREATE TABLE blocked_extension (
    id         BIGSERIAL   PRIMARY KEY,
    extension  VARCHAR(20) NOT NULL,
    type       VARCHAR(10) NOT NULL,
    blocked    BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_blocked_extension_extension UNIQUE (extension),
    CONSTRAINT ck_blocked_extension_type      CHECK (type IN ('FIXED', 'CUSTOM')),
    CONSTRAINT ck_blocked_extension_format    CHECK (extension ~ '^[a-z0-9]{1,20}$')
);

-- 고정 확장자 7종은 seed. 기본 unCheck(= blocked FALSE)
INSERT INTO blocked_extension (extension, type, blocked) VALUES
  ('bat','FIXED',FALSE), ('cmd','FIXED',FALSE), ('com','FIXED',FALSE),
  ('cpl','FIXED',FALSE), ('exe','FIXED',FALSE), ('scr','FIXED',FALSE),
  ('js' ,'FIXED',FALSE);

CREATE TABLE upload_history (
    id            BIGSERIAL   PRIMARY KEY,
    original_name TEXT        NOT NULL,
    stored_name   VARCHAR(36),
    size_bytes    BIGINT      NOT NULL,
    declared_type VARCHAR(255),
    detected_type VARCHAR(32),
    status        VARCHAR(16) NOT NULL,
    reject_code   VARCHAR(32),
    matched_ext   VARCHAR(20),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
