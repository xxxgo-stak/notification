CREATE TABLE notifications (
    id BIGSERIAL primary key,
    template_id BIGINT REFERENCES templates(id),
    recipient VARCHAR(255) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempts INT DEFAULT 0,
    payload JSONB,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP
);