SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema PisaFlix
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `PisaFlix` ;
CREATE SCHEMA IF NOT EXISTS `PisaFlix` DEFAULT CHARACTER SET utf8 ;
USE `PisaFlix` ;

-- -----------------------------------------------------
-- Table `PisaFlix`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`User` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`User` (
  `idUser` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `firstName` VARCHAR(45) NULL DEFAULT NULL,
  `lastName` VARCHAR(45) NULL DEFAULT NULL,
  `privilegeLevel` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Film`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Film` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Film` (
  `idFilm` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `publicationDate` DATE NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`idFilm`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Cinema`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Cinema` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Cinema` (
  `idCinema` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idCinema`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Comment` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Comment` (
  `idComment` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `timestamp` TIMESTAMP NOT NULL,
  `text` TEXT NOT NULL,
  `idUser` INT UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`idComment`),
  INDEX `fk_Comment_User1_idx` (`idUser` ASC),
  CONSTRAINT `fk_Comment_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlix`.`User` (`idUser`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Projection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Projection` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Projection` (
  `idProjection` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `dateTime` DATETIME NOT NULL,
  `room` INT UNSIGNED NOT NULL,
  `idFilm` INT UNSIGNED NULL DEFAULT NULL,
  `idCinema` INT UNSIGNED NOT NULL,
  INDEX `fk_Projection_Film1_idx` (`idFilm` ASC),
  INDEX `fk_Projection_Cinema1_idx` (`idCinema` ASC),
  PRIMARY KEY (`idProjection`),
  CONSTRAINT `fk_Projection_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlix`.`Film` (`idFilm`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Projection_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlix`.`Cinema` (`idCinema`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Film_has_Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Film_has_Comment` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Film_has_Comment` (
  `idFilm` INT UNSIGNED NOT NULL,
  `idComment` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idFilm`, `idComment`),
  INDEX `fk_Film_has_Comment_Film1_idx` (`idFilm` ASC),
  INDEX `fk_Film_has_Comment_Comment1_idx` (`idComment` ASC),
  CONSTRAINT `fk_Film_has_Comment_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlix`.`Film` (`idFilm`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Film_has_Comment_Comment1`
    FOREIGN KEY (`idComment`)
    REFERENCES `PisaFlix`.`Comment` (`idComment`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Cinema_has_Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Cinema_has_Comment` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Cinema_has_Comment` (
  `idCinema` INT UNSIGNED NOT NULL,
  `idComment` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idCinema`, `idComment`),
  INDEX `fk_Cinema_has_Comment_Cinema1_idx` (`idCinema` ASC),
  INDEX `fk_Cinema_has_Comment_Comment1_idx` (`idComment` ASC),
  CONSTRAINT `fk_Cinema_has_Comment_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlix`.`Cinema` (`idCinema`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Cinema_has_Comment_Comment1`
    FOREIGN KEY (`idComment`)
    REFERENCES `PisaFlix`.`Comment` (`idComment`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Favorite_Cinema`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Favorite_Cinema` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Favorite_Cinema` (
  `idCinema` INT UNSIGNED NOT NULL,
  `idUser` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idCinema`, `idUser`),
  INDEX `fk_Favorite_Cinema_User1_idx` (`idUser` ASC),
  CONSTRAINT `fk_Favorite_Cinema_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlix`.`Cinema` (`idCinema`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Favorite_Cinema_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlix`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlix`.`Favorite_Film`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlix`.`Favorite_Film` ;

CREATE TABLE IF NOT EXISTS `PisaFlix`.`Favorite_Film` (
  `idFilm` INT UNSIGNED NOT NULL,
  `idUser` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idFilm`, `idUser`),
  INDEX `fk_Film_has_User_User1_idx` (`idUser` ASC),
  INDEX `fk_Film_has_User_Film1_idx` (`idFilm` ASC),
  CONSTRAINT `fk_Film_has_User_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlix`.`Film` (`idFilm`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Film_has_User_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlix`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
