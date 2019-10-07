CREATE DATABASE  IF NOT EXISTS `moviedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `moviedb`;
-- MySQL dump 10.13  Distrib 8.0.17, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: moviedb
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Film`
--

DROP TABLE IF EXISTS `Film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Film` (
  `idFilm` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `publishDate` date DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idFilm`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Film`
--

LOCK TABLES `Film` WRITE;
/*!40000 ALTER TABLE `Film` DISABLE KEYS */;
INSERT INTO `Film` VALUES (1,'Joker','0002-10-19','Drama'),(2,'Avengers','2025-04-19','Action'),(3,'Wanted','2007-12-17','Horror'),(4,'Albakiara','2008-10-24','Drama');
/*!40000 ALTER TABLE `Film` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rating`
--

DROP TABLE IF EXISTS `Rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Rating` (
  `username` varchar(45) NOT NULL,
  `idFilm` int(11) NOT NULL,
  `rate` int(11) DEFAULT NULL,
  KEY `fk_Rating_User_idx` (`username`),
  KEY `fk_Film_idx` (`idFilm`),
  CONSTRAINT `fk_Film_Rated` FOREIGN KEY (`idFilm`) REFERENCES `film` (`idFilm`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Rating_User` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rating`
--

LOCK TABLES `Rating` WRITE;
/*!40000 ALTER TABLE `Rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `username` varchar(45) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `secondName` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `accountLevel` int(11) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES ('admin','admin','admin','admin@admin.ad','admin',1),('andrè','Andrea','Tubak','andrea@boh.com','1234',0),('DaEliminare','boh','boh^2','tizio@caio.sempronio','inutile',0),('fraronk','Francesco','Ronchieri','francesco@hotmail.mo','56789',0),('mazzorca','Alessandro','Madonna','mazzorca96@gmail.com','pass1',0),('stefanoP','Stefano','Petrocchi','stefanoP@gmeil.com','tizio1',0);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'moviedb'
--

--
-- Dumping routines for database 'moviedb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-07 16:04:26
