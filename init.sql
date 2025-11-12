CREATE DATABASE IF NOT EXISTS moabom CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE moabom;

CREATE TABLE IF NOT EXISTS `User` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `eamil` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`user_id`)
    );

CREATE TABLE IF NOT EXISTS `OTT` (
    `ott_id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `price` INT NOT NULL,
    `logo_url` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`ott_id`)
    );

CREATE TABLE IF NOT EXISTS `Subscribe` (
    `subscribe_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `ott_id` BIGINT NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE,
    PRIMARY KEY (`subscribe_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`ott_id`) REFERENCES `OTT`(`ott_id`) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS `Program` (
    `program_id` BIGINT NOT NULL AUTO_INCREMENT,
    `crawling_id` BIGINT NOT NULL UNIQUE,
    `title` VARCHAR(100) NOT NULL,
    `genre` VARCHAR(50) NOT NULL,
    `description` TEXT NOT NULL,
    `thumbnail_url` VARCHAR(255),
    `backdrop_url` VARCHAR(255),
    `running_time` INT,
    `ranking` INT,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`program_id`),
    FULLTEXT KEY `ft_title` (`title`)
    );

CREATE TABLE IF NOT EXISTS `Program_Availability` (
    `availability_id` BIGINT NOT NULL AUTO_INCREMENT,
    `program_id` BIGINT NOT NULL,
     `ott_id` BIGINT NOT NULL,
     `url` VARCHAR(255) NOT NULL,
    `release_date` DATE,
    `expire_date` DATE,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`availability_id`),
    FOREIGN KEY (`program_id`) REFERENCES `Program`(`program_id`) ON DELETE CASCADE,
    FOREIGN KEY (`ott_id`) REFERENCES `OTT`(`ott_id`) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS `Wishlist` (
    `wishlist_id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
     `program_id` BIGINT NOT NULL,
    PRIMARY KEY (`wishlist_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`program_id`) REFERENCES `Program`(`program_id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_wish` (`user_id`, `program_id`)
    );

INSERT INTO `User` (`name`, `password`) VALUES
('wonbin@ajou.ac.kr', '1234'),
('Ajou@ajou.ac.kr', '1234');
