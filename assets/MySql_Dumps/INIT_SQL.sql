-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema walfi
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema walfi
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `walfi` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `walfi` ;

-- -----------------------------------------------------
-- Table `walfi`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`user` (
  `user_id` VARCHAR(16) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `password` VARCHAR(100) NOT NULL,
  `name` VARCHAR(16) NULL DEFAULT NULL,
  `birth_date` DATETIME NULL DEFAULT NULL,
  `phone_number` VARCHAR(16) NULL DEFAULT NULL,
  `대표계좌` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `대표계좌_UNIQUE` (`대표계좌` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`account` (
  `계좌번호` CHAR(12) NOT NULL,
  `구분` VARCHAR(16) NULL DEFAULT NULL,
  `상품명` CHAR(4) NULL DEFAULT NULL,
  `잔액(통화별)` BIGINT NULL DEFAULT NULL,
  `평가금액(통화별)` BIGINT NULL DEFAULT NULL,
  `신규일` DATE NULL DEFAULT NULL,
  `만기일` DATE NULL DEFAULT NULL,
  `관리점명` VARCHAR(255) NULL DEFAULT NULL,
  `금리(수익률)` DECIMAL(2,2) NULL DEFAULT NULL,
  `통화` VARCHAR(4) NULL DEFAULT NULL,
  `과세` CHAR(4) NULL DEFAULT NULL,
  `잔액(원화)` BIGINT NULL DEFAULT NULL,
  `평가금액(원화)` BIGINT NULL DEFAULT NULL,
  `자동해지여부` TINYINT NULL DEFAULT NULL,
  `대표계좌` VARCHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`계좌번호`),
  INDEX `fk_account_user2_idx` (`대표계좌` ASC) INVISIBLE,
  CONSTRAINT `fk_account_user2`
    FOREIGN KEY (`대표계좌`)
    REFERENCES `walfi`.`user` (`대표계좌`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`user_game_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`user_game_info` (
  `point` INT NULL DEFAULT '0',
  `status` VARCHAR(255) NULL DEFAULT '도전자',
  `user_id` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_user_game_info_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_game_info_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `walfi`.`user` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`branch`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`branch` (
  `branch_idx` BIGINT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(100) NULL DEFAULT NULL,
  `branch_name` VARCHAR(16) NULL DEFAULT NULL,
  `branch_type` VARCHAR(16) NULL DEFAULT NULL,
  `branch_phone_number` VARCHAR(16) NULL DEFAULT NULL,
  `latitude` DOUBLE NULL DEFAULT NULL,
  `longitude` DOUBLE NULL DEFAULT NULL,
  `manager_level` VARCHAR(10) NULL DEFAULT '6',
  `manager_exp` INT NULL DEFAULT '0',
  `manager_hp` INT NULL DEFAULT '50',
  `manager_atk` INT NULL DEFAULT '2',
  `manager_def` INT NULL DEFAULT '2',
  `start_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_idx`),
  INDEX `fk_branch_user_game_info1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_branch_user_game_info1`
    FOREIGN KEY (`user_id`)
    REFERENCES `walfi`.`user_game_info` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 5001
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`battle_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`battle_history` (
  `battle_idx` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  `occupy_time` BIGINT NULL DEFAULT NULL,
  `branch_idx` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`battle_idx`),
  INDEX `FK6qrmbe72qh6g647qlgxy9dg1k` (`branch_idx` ASC) VISIBLE,
  CONSTRAINT `FK6qrmbe72qh6g647qlgxy9dg1k`
    FOREIGN KEY (`branch_idx`)
    REFERENCES `walfi`.`branch` (`branch_idx`))
ENGINE = InnoDB
AUTO_INCREMENT = 41
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`game_character`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`game_character` (
  `character_idx` BIGINT NOT NULL AUTO_INCREMENT,
  `character_type` VARCHAR(10) NULL DEFAULT NULL,
  `color` VARCHAR(10) NULL DEFAULT 'BASIC',
  `level` VARCHAR(10) NULL DEFAULT '1',
  `exp` INT NULL DEFAULT '0',
  `hp` INT NULL DEFAULT '50',
  `atk` INT NULL DEFAULT '1',
  `def` INT NULL DEFAULT '1',
  `is_main` TINYINT NULL DEFAULT '0',
  `created_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`character_idx`),
  INDEX `fk_game_character_user_game_info1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_game_character_user_game_info1`
    FOREIGN KEY (`user_id`)
    REFERENCES `walfi`.`user_game_info` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`global_account_transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`global_account_transaction` (
  `글로벌계좌거래번호` BIGINT NOT NULL AUTO_INCREMENT,
  `상대계좌번호` CHAR(12) NULL DEFAULT NULL,
  `통화코드` CHAR(3) NULL DEFAULT NULL,
  `외화금액` BIGINT NULL DEFAULT NULL,
  `이체종류` CHAR(2) NULL DEFAULT NULL,
  `거래후잔액` BIGINT NULL DEFAULT NULL,
  `계좌번호` CHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`글로벌계좌거래번호`),
  INDEX `FK9224g1ucffa2qnrdnafos5ls7` (`계좌번호` ASC) VISIBLE,
  CONSTRAINT `FK9224g1ucffa2qnrdnafos5ls7`
    FOREIGN KEY (`계좌번호`)
    REFERENCES `walfi`.`account` (`계좌번호`))
ENGINE = InnoDB
AUTO_INCREMENT = 400000
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`global_transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`global_transaction` (
  `글로벌거래번호` BIGINT NOT NULL AUTO_INCREMENT,
  `거래일자` DATE NULL DEFAULT NULL,
  `거래시각` TIME NULL DEFAULT NULL,
  `거래종류` VARCHAR(16) NULL DEFAULT NULL,
  `적요` VARCHAR(16) NULL DEFAULT NULL,
  `지급금액` FLOAT NULL DEFAULT NULL,
  `입금금액` FLOAT NULL DEFAULT NULL,
  `계좌잔액` FLOAT NULL DEFAULT NULL,
  `정정취소구분` VARCHAR(3) NULL DEFAULT NULL,
  `거래원화금액` BIGINT NULL DEFAULT NULL,
  `거래환율` FLOAT NULL DEFAULT NULL,
  `입금의뢰인명` VARCHAR(16) NULL DEFAULT NULL,
  `계좌번호` VARCHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`글로벌거래번호`),
  INDEX `FKh7npy4lr3fh13sihkk2ml9w99` (`계좌번호` ASC) VISIBLE,
  CONSTRAINT `FKh7npy4lr3fh13sihkk2ml9w99`
    FOREIGN KEY (`계좌번호`)
    REFERENCES `walfi`.`account` (`계좌번호`))
ENGINE = InnoDB
AUTO_INCREMENT = 300000
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`krw_account_transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`krw_account_transaction` (
  `원화거래번호` BIGINT NOT NULL AUTO_INCREMENT,
  `상대계좌번호` CHAR(12) NULL DEFAULT NULL,
  `입금은행코드` CHAR(3) NULL DEFAULT NULL,
  `이체종류` CHAR(2) NULL DEFAULT NULL,
  `이체금액` BIGINT NULL DEFAULT NULL,
  `입금계좌통장메모` VARCHAR(16) NULL DEFAULT NULL,
  `출금계좌통장메모` VARCHAR(16) NULL DEFAULT NULL,
  `거래후잔액` BIGINT NULL DEFAULT NULL,
  `계좌번호` CHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`원화거래번호`),
  INDEX `FKji4a06519y0vcok6ng89pk3gw` (`계좌번호` ASC) VISIBLE,
  CONSTRAINT `FKji4a06519y0vcok6ng89pk3gw`
    FOREIGN KEY (`계좌번호`)
    REFERENCES `walfi`.`account` (`계좌번호`))
ENGINE = InnoDB
AUTO_INCREMENT = 200000
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`krw_transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`krw_transaction` (
  `원화거래번호` BIGINT NOT NULL AUTO_INCREMENT,
  `거래일자` DATE NULL DEFAULT NULL,
  `거래시각` TIME NULL DEFAULT NULL,
  `적요` VARCHAR(16) NULL DEFAULT NULL,
  `출금금액` BIGINT NULL DEFAULT NULL,
  `입금금액` BIGINT NULL DEFAULT NULL,
  `내용` VARCHAR(30) NULL DEFAULT NULL,
  `잔액` BIGINT NULL DEFAULT NULL,
  `입지구분` TINYINT NULL DEFAULT NULL,
  `거래점명` VARCHAR(16) NULL DEFAULT NULL,
  `계좌번호` CHAR(12) NULL DEFAULT NULL,
  PRIMARY KEY (`원화거래번호`),
  INDEX `FKcr708aagpubq47g31oqgwslhq` (`계좌번호` ASC) VISIBLE,
  CONSTRAINT `FKcr708aagpubq47g31oqgwslhq`
    FOREIGN KEY (`계좌번호`)
    REFERENCES `walfi`.`account` (`계좌번호`))
ENGINE = InnoDB
AUTO_INCREMENT = 100000
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `walfi`.`quest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `walfi`.`quest` (
  `quest_idx` BIGINT NOT NULL AUTO_INCREMENT,
  `quest_type` VARCHAR(10) NULL DEFAULT NULL,
  `quest_title` VARCHAR(50) NULL DEFAULT NULL,
  `quest_content` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`quest_idx`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
