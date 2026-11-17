CREATE DATABASE IF NOT EXISTS tetris CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'tetris'@'localhost' IDENTIFIED BY 'change-me';
GRANT ALL PRIVILEGES ON tetris.* TO 'tetris'@'localhost';
FLUSH PRIVILEGES;
