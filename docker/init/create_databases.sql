CREATE DATABASE IF NOT EXISTS border_state_bot_db;
CREATE DATABASE IF NOT EXISTS border_state_db;
GRANT ALL PRIVILEGES ON border_state_bot_db.* TO 'bot_mariadb_user'@'%';
GRANT ALL PRIVILEGES ON border_state_db.* TO 'bot_mariadb_user'@'%';
FLUSH PRIVILEGES;
