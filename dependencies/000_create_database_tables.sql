CREATE DATABASE IF NOT EXISTS HEARTSTONE_DB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE HEARTSTONE_DB;


CREATE TABLE IF NOT EXISTS USUARIS (
    USERNAME VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    PASSWORD VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    IS_ADMIN BOOLEAN,
    PRIMARY KEY (USERNAME)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS CARTES (
    NAME VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    CLASS INT,
    MANA_COST INT,
    ATTACK INT,
    HEALTH INT,
    TEXT VARCHAR(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    IMAGE_URL VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    TYPE INT,
    RARITY INT,
    PRIMARY KEY (NAME, IMAGE_URL),
    UNIQUE (IMAGE_URL)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS FOTOS_CARTES (
    IMAGE_URL VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    IMAGE MEDIUMBLOB,
    PRIMARY KEY (IMAGE_URL),
    FOREIGN KEY (IMAGE_URL) REFERENCES CARTES(IMAGE_URL)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_general_ci;
