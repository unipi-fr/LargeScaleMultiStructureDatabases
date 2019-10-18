CREATE SCHEMA IF NOT EXISTS `bookstore` DEFAULT CHARACTER SET utf8 ;
USE `bookstore` ;

-- -----------------------------------------------------
-- Table `PisaFlixDB`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bookstore`.`book` ;

CREATE TABLE IF NOT EXISTS `bookstore`.`book` (
  `book_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `author` VARCHAR(45) NOT NULL,
  `price` DOUBLE NOT NULL,
  `title` VARCHAR(45) NULL,
  PRIMARY KEY (`book_id`))
ENGINE = InnoDB;