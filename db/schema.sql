CREATE DATABASE IF NOT EXISTS juegos_retro_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE juegos_retro_db;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  password_salt VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS scores (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  game_key VARCHAR(20) NOT NULL,
  score INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_scores_game (game_key),
  INDEX idx_scores_user (user_id),
  CONSTRAINT fk_scores_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;
