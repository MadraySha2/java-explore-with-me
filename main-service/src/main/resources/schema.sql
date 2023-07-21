CREATE TABLE IF NOT EXISTS compilation_event(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id       BIGINT,
    compilation_id BIGINT,
    CONSTRAINT fk_event_compilation_to_event FOREIGN KEY (event_id) REFERENCES event (id) ON UPDATE CASCADE,
    CONSTRAINT fk_event_compilation_to_compilation FOREIGN KEY (compilation_id) REFERENCES compilation (id) ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name  VARCHAR(250),
    email VARCHAR(254) UNIQUE
);
CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT,
    requester_id BIGINT,
    create_date  TIMESTAMP,
    status       VARCHAR(20),
    CONSTRAINT fk_requests_to_event FOREIGN KEY (event_id) REFERENCES event (id),
    CONSTRAINT fk_requests_to_user FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    annotation VARCHAR(2000),
    confirmed_requests BIGINT,
    created_on TIMESTAMP,
    description VARCHAR(7000),
    event_date TIMESTAMP,
    lat NUMERIC,
    lon NUMERIC,
    paid BOOLEAN,
    participant_limit BIGINT,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(255),
    title VARCHAR(255),
    "views" BIGINT,
    category_id BIGINT,
    user_id BIGINT,
    CONSTRAINT fk_event_to_user FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_event_to_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    pinned BOOLEAN,
    title  VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name VARCHAR(50) UNIQUE
);