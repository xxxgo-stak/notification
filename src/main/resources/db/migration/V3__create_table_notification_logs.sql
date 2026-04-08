CREATE TABLE notification_logs (
    id BIGSERIAL PRIMARY KEY,
    notification_id BIGINT REFERENCES notifications(id),
    success BOOLEAN NOT NULL,
    error_message TEXT,
    attempted_at TIMESTAMP NOT NULL
);