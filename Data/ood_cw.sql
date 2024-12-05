-- MySQL dump 10.13  Distrib 9.0.1, for macos14.7 (arm64)
--
-- Host: localhost    Database: OOD_CW
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `email` varchar(255) NOT NULL,
  `article_id` varchar(255) NOT NULL,
  `interaction_type` enum('like','dislike','view','skip') NOT NULL DEFAULT 'view',
  `interaction_count` int DEFAULT '0',
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`email`,`article_id`,`interaction_type`),
  CONSTRAINT `history_fk` FOREIGN KEY (`email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES ('chani@gmail.com','67163953a2b65ee8e43a3156','like',3,'2024-12-04 10:48:53'),('chani@gmail.com','67163953a2b65ee8e43a3156','dislike',1,'2024-12-04 10:48:59'),('chani@gmail.com','67163953a2b65ee8e43a3156','view',1,'2024-12-04 10:48:38'),('chani@gmail.com','671639b6a2b65ee8e43a3171','dislike',14,'2024-12-04 10:50:18'),('chirasthi.20232005@iit.ac.lk','671639f9a2b65ee8e43a318a','like',1,'2024-12-05 01:29:58'),('chirasthi.20232005@iit.ac.lk','671639f9a2b65ee8e43a318a','view',1,'2024-12-05 01:29:52'),('root@root','67163959a2b65ee8e43a3158','view',2,'2024-11-13 21:53:27'),('root@root','67163971a2b65ee8e43a315d','like',1,'2024-10-30 11:52:28'),('root@root','67163971a2b65ee8e43a315d','view',2,'2024-10-30 11:50:37'),('wella','67163953a2b65ee8e43a3156','like',1,'2024-10-30 12:38:57'),('wella','67163953a2b65ee8e43a3156','view',3,'2024-10-30 12:38:53'),('wella','67163953a2b65ee8e43a3156','skip',7,'2024-10-30 12:53:21'),('wella','67163956a2b65ee8e43a3157','view',3,'2024-10-31 22:01:20'),('wella','67163956a2b65ee8e43a3157','skip',7,'2024-10-30 12:53:24'),('wella','67163968a2b65ee8e43a315a','skip',1,'2024-10-30 13:13:39'),('wella','671639a1a2b65ee8e43a316e','like',2,'2024-11-29 22:34:00'),('wella','671639a1a2b65ee8e43a316e','view',3,'2024-11-28 23:06:39'),('wella','671639a1a2b65ee8e43a316e','skip',1,'2024-12-05 10:13:06'),('wella','671639c7a2b65ee8e43a3176','skip',1,'2024-12-05 10:13:10'),('wella','671639f9a2b65ee8e43a318a','view',1,'2024-12-05 10:13:29'),('wella','671639f9a2b65ee8e43a318a','skip',2,'2024-12-05 10:13:08');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences`
--

DROP TABLE IF EXISTS `preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences` (
  `email` varchar(255) NOT NULL,
  `user_pref` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `email_pref` FOREIGN KEY (`email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences`
--

LOCK TABLES `preferences` WRITE;
/*!40000 ALTER TABLE `preferences` DISABLE KEYS */;
INSERT INTO `preferences` VALUES ('chani@gmail.com','AI,model,models,outcome'),('chanidu3434@gmail.com',''),('chirasthi.20232005@iit.ac.lk','AI,safety,applications,camera,images,information,privacy,business,robotaxi,startup,year,WordPress'),('root@root','business,world,entertainment'),('thennakoonchirasthi@gmail.com','Technology,Health,Finance,Sports,Entertainment,Education'),('wella','ai,mislead,models,outcome,researchers,sabotage,safety,task');
/*!40000 ALTER TABLE `preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(32) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `admin` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('CC EE','chani@gmail.com','1234','2024-12-04 05:16:20',0),('Chanidu Edirisinghe','chanidu3434@gmail.com','123456','2024-12-04 05:15:12',0),('Chirasthi Thennakoon','chirasthi.20232005@iit.ac.lk','1234','2024-12-04 19:59:37',0),('wella','lalala','1234','2024-10-19 17:11:10',0),('0','root@root','12345678','2024-10-13 06:55:24',1),('1','test@gmail.com','test1234','2024-10-13 07:19:17',0),('chira','thennakoonchirasthi@gmail.com','1234','2024-11-02 19:14:08',0),('wella','wella','1234','2024-10-19 17:13:28',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-05 11:05:40
