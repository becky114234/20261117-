-- 建立使用者資料表。
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS scores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    cleared_lines INT NOT NULL,
    level INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    -- 每筆分數都必須屬於一位使用者。
    CONSTRAINT fk_scores_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 建立完整的遊戲對局紀錄資料表。
CREATE TABLE IF NOT EXISTS game_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    cleared_lines INT NOT NULL,
    level INT NOT NULL,
    result VARCHAR(20) NOT NULL,
    played_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_game_records_user FOREIGN KEY (user_id) REFERENCES users(id)
);
