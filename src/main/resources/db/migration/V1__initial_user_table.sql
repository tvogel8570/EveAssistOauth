CREATE SEQUENCE IF NOT EXISTS eve_assist_user_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE eve_assist_user
(
    id          BIGINT       NOT NULL,
    unique_user VARCHAR(30)  NOT NULL,
    email       VARCHAR(255) NOT NULL,
    screen_name VARCHAR(50)  NOT NULL,
    password    VARCHAR(255),
    CONSTRAINT pk_eveassistuser PRIMARY KEY (id)
);

ALTER TABLE eve_assist_user
    ADD CONSTRAINT uc_eveassistuser_email UNIQUE (email);

ALTER TABLE eve_assist_user
    ADD CONSTRAINT uc_eveassistuser_screen_name UNIQUE (screen_name);

ALTER TABLE eve_assist_user
    ADD CONSTRAINT uc_eveassistuser_unique_user UNIQUE (unique_user);