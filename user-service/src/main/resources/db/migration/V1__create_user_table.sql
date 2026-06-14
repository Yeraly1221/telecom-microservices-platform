CREATE TABLE _user (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    credential_id BIGINT NOT NULL UNIQUE
);
