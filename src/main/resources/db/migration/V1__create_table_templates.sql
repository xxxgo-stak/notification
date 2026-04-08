CREATE TABLE templates (
    id BIGSERIAL primary key,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    channel VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL
);