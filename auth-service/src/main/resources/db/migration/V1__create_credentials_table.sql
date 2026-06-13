CREATE TABLE credentials (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN,
    role VARCHAR(255) NOT NULL
);
