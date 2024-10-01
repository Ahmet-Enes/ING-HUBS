CREATE TABLE IF NOT EXISTS customer (
    id int AUTO_INCREMENT PRIMARY KEY,
    username varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    role varchar(100) NOT NULL
);

INSERT INTO customer (id, username, password, role)
VALUES (0, 'admin', '$2a$12$mdvn8HbcQuWyTWVdcvZTweJkdB26B15lYV48/58uQTm1RBEXb2lUy', 'ROLE_ADMIN');