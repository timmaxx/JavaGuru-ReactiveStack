CREATE TABLE ALBUM (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id     VARCHAR(100) NOT NULL,
                       title VARCHAR(255)  NOT NULL
);