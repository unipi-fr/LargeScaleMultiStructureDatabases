-- -----------------------------------------------------
-- PisaFlixDB
-- -----------------------------------------------------

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema PisaFlixDB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `PisaFlixDB` ;

-- -----------------------------------------------------
-- Schema PisaFlixDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `PisaFlixDB` DEFAULT CHARACTER SET utf8 ;
USE `PisaFlixDB` ;

-- -----------------------------------------------------
-- Table `PisaFlixDB`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`User` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`User` (
  `idUser` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NULL,
  `secondName` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `privilegeLevel` INT NOT NULL,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Film`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Film` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Film` (
  `idFilm` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `publicationDate` DATE NOT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`idFilm`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Cinema`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Cinema` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Cinema` (
  `idCinema` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idCinema`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Film_Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Film_Comment` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Film_Comment` (
  `timestamp` TIMESTAMP NOT NULL,
  `idUser` INT UNSIGNED NOT NULL,
  `idFilm` INT UNSIGNED NOT NULL,
  `text` TEXT NOT NULL,
  PRIMARY KEY (`timestamp`, `idFilm`, `idUser`),
  INDEX `fk_Film_Comment_User_idx` (`idUser` ASC),
  INDEX `fk_Film_Comment_Film1_idx` (`idFilm` ASC),
  CONSTRAINT `fk_Film_Comment_User`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlixDB`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Film_Comment_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlixDB`.`Film` (`idFilm`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Cinema_Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Cinema_Comment` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Cinema_Comment` (
  `timestamp` TIMESTAMP NOT NULL,
  `idUser` INT UNSIGNED NOT NULL,
  `idCinema` INT UNSIGNED NOT NULL,
  `text` TEXT NOT NULL,
  PRIMARY KEY (`timestamp`, `idUser`, `idCinema`),
  INDEX `fk_Cinema_Comment_User1_idx` (`idUser` ASC),
  INDEX `fk_Cinema_Comment_Cinema1_idx` (`idCinema` ASC),
  CONSTRAINT `fk_Cinema_Comment_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlixDB`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cinema_Comment_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlixDB`.`Cinema` (`idCinema`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Cinema_Rating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Cinema_Rating` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Cinema_Rating` (
  `idUser` INT UNSIGNED NOT NULL,
  `idCinema` INT UNSIGNED NOT NULL,
  `rate` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`idUser`, `idCinema`),
  INDEX `fk_Cinema_Rating_Cinema1_idx` (`idCinema` ASC),
  CONSTRAINT `fk_Cinema_Rating_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlixDB`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cinema_Rating_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlixDB`.`Cinema` (`idCinema`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Film_Rating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Film_Rating` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Film_Rating` (
  `idUser` INT UNSIGNED NOT NULL,
  `idFilm` INT UNSIGNED NOT NULL,
  `rate` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`idUser`, `idFilm`),
  INDEX `fk_Film_Rating_Film1_idx` (`idFilm` ASC),
  CONSTRAINT `fk_Film_Rating_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `PisaFlixDB`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Film_Rating_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlixDB`.`Film` (`idFilm`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `PisaFlixDB`.`Projection`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PisaFlixDB`.`Projection` ;

CREATE TABLE IF NOT EXISTS `PisaFlixDB`.`Projection` (
  `date` DATE NOT NULL,
  `room` INT UNSIGNED NOT NULL,
  `idFilm` INT UNSIGNED NOT NULL,
  `idCinema` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`date`, `room`, `idFilm`, `idCinema`),
  INDEX `fk_Projection_Film1_idx` (`idFilm` ASC),
  INDEX `fk_Projection_Cinema1_idx` (`idCinema` ASC),
  CONSTRAINT `fk_Projection_Film1`
    FOREIGN KEY (`idFilm`)
    REFERENCES `PisaFlixDB`.`Film` (`idFilm`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Projection_Cinema1`
    FOREIGN KEY (`idCinema`)
    REFERENCES `PisaFlixDB`.`Cinema` (`idCinema`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
