CREATE TABLE IF NOT EXISTS user_role
(
    id          BIGSERIAL   PRIMARY KEY,
    email       TEXT        NOT NULL UNIQUE,
    password    TEXT        NOT NULL,
    name        TEXT        NULL
);

CREATE TABLE IF NOT EXISTS external_project
(
    id          BIGSERIAL   PRIMARY KEY,
    user_id     BIGINT      REFERENCES user_role (id) ON DELETE CASCADE,
    name        TEXT        NOT NULL
);
