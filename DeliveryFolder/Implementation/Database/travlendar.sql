-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Creato il: Gen 06, 2018 alle 11:14
-- Versione del server: 5.7.19
-- Versione PHP: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `travlendar`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `activity`
--

DROP TABLE IF EXISTS `activity`;
CREATE TABLE IF NOT EXISTS `activity` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER` int(10) UNSIGNED NOT NULL,
  `LABEL` varchar(100) NOT NULL,
  `NOTES` varchar(100) DEFAULT NULL,
  `LOCATION_TEXT` varchar(100) DEFAULT NULL,
  `LOCATION_TAG_ID` int(10) UNSIGNED DEFAULT NULL,
  `START_PLACE_TEXT` varchar(100) DEFAULT NULL,
  `START_PLACE_TAG_ID` int(10) UNSIGNED DEFAULT NULL,
  `FLEXIBLE` tinyint(1) NOT NULL,
  `DURATION` int(11) DEFAULT NULL COMMENT 'minutes',
  `START_DAY_TIME` bigint(20) NOT NULL,
  `END_DAY_TIME` bigint(20) NOT NULL,
  `STATUS` int(10) UNSIGNED NOT NULL,
  `ESTIMATED_TRAVEL_TIME` int(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT 'minutes',
  PRIMARY KEY (`KEY_ID`),
  KEY `LOCATION_TAG_ID` (`LOCATION_TAG_ID`),
  KEY `START_PLACE_TAG_ID` (`START_PLACE_TAG_ID`),
  KEY `STATUS` (`STATUS`),
  KEY `USER` (`USER`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `activity`
--

INSERT INTO `activity` (`KEY_ID`, `USER`, `LABEL`, `NOTES`, `LOCATION_TEXT`, `LOCATION_TAG_ID`, `START_PLACE_TEXT`, `START_PLACE_TAG_ID`, `FLEXIBLE`, `DURATION`, `START_DAY_TIME`, `END_DAY_TIME`, `STATUS`, `ESTIMATED_TRAVEL_TIME`) VALUES
(109, 12, 'a', 'a', 'break', NULL, 'break', NULL, 1, 30, 1531980000000, 1531987200000, 1, 0),
(110, 12, 'a', 'a', 'break', NULL, 'break', NULL, 1, 30, 1531987200000, 1531994400000, 1, 0),
(111, 12, 'a', 'a', 'break', NULL, 'break', NULL, 1, 60, 1532001600000, 1532008800000, 1, 0),
(113, 12, 'b', 'b', 'break', NULL, 'break', NULL, 1, 15, 1532023200000, 1532026800000, 1, 0),
(114, 12, 'b', 'b', 'break', NULL, 'break', NULL, 1, 30, 1532030400000, 1532034000000, 1, 0),
(115, 12, 'b', 'b', 'break', NULL, 'break', NULL, 1, 15, 1532034000000, 1532037600000, 1, 0),
(116, 12, 'b', 'b', 'break', NULL, 'break', NULL, 1, 15, 1531962000000, 1531965600000, 1, 0),
(117, 12, 'test', 'test', 'via caduti di marcinelle 2 milano', NULL, 'via stefanardo da vimercate 8 milano ', NULL, 0, 0, 1513454400000, 1513458000000, 1, 24),
(118, 12, 'lunch', '', 'via roma 2, castrocielo', NULL, 'via stazione 5, castrocielo', NULL, 0, 30, 1515067200718, 1515070800718, 1, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `activity_status`
--

DROP TABLE IF EXISTS `activity_status`;
CREATE TABLE IF NOT EXISTS `activity_status` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ACTIVITY_STATUS_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `ACTIVITY_STATUS` (`ACTIVITY_STATUS_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `activity_status`
--

INSERT INTO `activity_status` (`KEY_ID`, `ACTIVITY_STATUS_NAME`) VALUES
(1, 'NOT_STARTED'),
(2, 'ON_GOING'),
(3, 'TERMINATED');

-- --------------------------------------------------------

--
-- Struttura della tabella `boolean_preferences`
--

DROP TABLE IF EXISTS `boolean_preferences`;
CREATE TABLE IF NOT EXISTS `boolean_preferences` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) UNSIGNED NOT NULL,
  `PERSONAL_CAR` tinyint(1) NOT NULL,
  `CAR_SHARING` tinyint(1) NOT NULL,
  `PERSONAL_BIKE` tinyint(1) NOT NULL,
  `BIKE_SHARING` tinyint(1) NOT NULL,
  `PUBLIC_TRANSPORT` tinyint(1) NOT NULL,
  `UBER_TAXI` tinyint(1) NOT NULL,
  `MODALITY` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`),
  KEY `MODALITY` (`MODALITY`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `boolean_preferences`
--

INSERT INTO `boolean_preferences` (`KEY_ID`, `USER_ID`, `PERSONAL_CAR`, `CAR_SHARING`, `PERSONAL_BIKE`, `BIKE_SHARING`, `PUBLIC_TRANSPORT`, `UBER_TAXI`, `MODALITY`) VALUES
(1, 1, 1, 1, 1, 1, 1, 1, 4),
(8, 2, 1, 1, 1, 1, 1, 1, 3),
(9, 3, 1, 1, 0, 0, 0, 0, 1),
(10, 4, 0, 1, 0, 1, 0, 0, 1),
(11, 6, 0, 1, 1, 0, 0, 0, 1),
(12, 7, 1, 1, 1, 0, 1, 1, 1),
(14, 11, 0, 1, 0, 1, 1, 1, 1),
(15, 12, 1, 0, 1, 0, 1, 0, 3),
(16, 13, 0, 1, 0, 1, 1, 1, 1),
(17, 14, 0, 1, 0, 1, 1, 1, 1),
(18, 15, 0, 1, 0, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `favpositions`
--

DROP TABLE IF EXISTS `favpositions`;
CREATE TABLE IF NOT EXISTS `favpositions` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) UNSIGNED NOT NULL,
  `LATITUDE` double NOT NULL,
  `LONGITUDE` double NOT NULL,
  `TAG` text NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  KEY `USER_ID` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `favpositions`
--

INSERT INTO `favpositions` (`KEY_ID`, `USER_ID`, `LATITUDE`, `LONGITUDE`, `TAG`) VALUES
(10, 1, 45.4800783, 9.2472984, 'testtrequattro'),
(11, 1, 52.4519832, 16.7280532, 'giorgio'),
(12, 1, 45.5061231, 9.2257091, 'gorla'),
(13, 1, 45.4590968, 9.093464899999999, 'homeSweetHome'),
(14, 12, 48.3705449, 10.89779, 'a'),
(15, 12, 41.6763577, -71.53721809999999, 'Tag');

-- --------------------------------------------------------

--
-- Struttura della tabella `modality`
--

DROP TABLE IF EXISTS `modality`;
CREATE TABLE IF NOT EXISTS `modality` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `MODALITY_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `MODALITY_NAME` (`MODALITY_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `modality`
--

INSERT INTO `modality` (`KEY_ID`, `MODALITY_NAME`) VALUES
(3, 'MINIMIZE_COST'),
(2, 'MINIMIZE_FOOTPRINT'),
(4, 'MINIMIZE_TIME'),
(1, 'STANDARD');

-- --------------------------------------------------------

--
-- Struttura della tabella `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) UNSIGNED DEFAULT NULL,
  `ACTIVITY_ID` int(10) UNSIGNED DEFAULT NULL,
  `TEXT` varchar(300) NOT NULL,
  `TIMESTAMP` bigint(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  KEY `USER_ID` (`USER_ID`,`ACTIVITY_ID`),
  KEY `ACTIVITY_ID` (`ACTIVITY_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `notification`
--

INSERT INTO `notification` (`KEY_ID`, `USER_ID`, `ACTIVITY_ID`, `TEXT`, `TIMESTAMP`) VALUES
(145, NULL, NULL, 'Rain', 1515236506397);

-- --------------------------------------------------------

--
-- Struttura della tabella `ranged_preferences`
--

DROP TABLE IF EXISTS `ranged_preferences`;
CREATE TABLE IF NOT EXISTS `ranged_preferences` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) UNSIGNED NOT NULL,
  `PREF_TYPE` int(10) UNSIGNED NOT NULL,
  `VALUE` int(10) UNSIGNED NOT NULL COMMENT 'minutes',
  PRIMARY KEY (`KEY_ID`),
  KEY `PREF_TYPE` (`PREF_TYPE`),
  KEY `USER_ID` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `ranged_preferences`
--

INSERT INTO `ranged_preferences` (`KEY_ID`, `USER_ID`, `PREF_TYPE`, `VALUE`) VALUES
(3, 2, 1, 6),
(13, 1, 4, 4),
(14, 1, 1, 4),
(15, 1, 5, 4),
(16, 1, 3, 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `ranged_preference_type`
--

DROP TABLE IF EXISTS `ranged_preference_type`;
CREATE TABLE IF NOT EXISTS `ranged_preference_type` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `TYPE_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `TYPE_NAME` (`TYPE_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `ranged_preference_type`
--

INSERT INTO `ranged_preference_type` (`KEY_ID`, `TYPE_NAME`) VALUES
(3, 'BIKING_TIME_LIMIT'),
(4, 'CAR_TIME_LIMIT'),
(2, 'COST_LIMIT'),
(5, 'PUBLIC_TRANSPORT_TIME_LIMIT'),
(1, 'WALKING_TIME_LIMIT');

-- --------------------------------------------------------

--
-- Struttura della tabella `reg_status`
--

DROP TABLE IF EXISTS `reg_status`;
CREATE TABLE IF NOT EXISTS `reg_status` (
  `KEY_ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `STATUS_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `STATUS_NAME` (`STATUS_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `reg_status`
--

INSERT INTO `reg_status` (`KEY_ID`, `STATUS_NAME`) VALUES
(3, 'EMAIL_NOT_CONFIRMED'),
(1, 'REGULAR'),
(2, 'SUSPENDED');

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `USERNAME` text NOT NULL,
  `PASSWORD` text NOT NULL,
  `REGISTRATION_STATUS` int(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REGISTRATION_STATUS` (`REGISTRATION_STATUS`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`ID`, `USERNAME`, `PASSWORD`, `REGISTRATION_STATUS`) VALUES
(1, 'cane', 'cane', 1),
(2, 'GIOVANNI', 'DONDONI', 1),
(3, 'LUCA', 'DONDONDONDONDONDONDONDONDONDONDONDONDODNODNODNODNDONDONDODNODNDONDONDODNODNDONDONI', 1),
(4, 'MATTIA', 'CULO', 1),
(6, 'xfgf', 'xdx', 3),
(7, 'cande', 's', 1),
(11, 'bigdog', 'bigdog', 1),
(12, 'a', 'a', 1),
(13, 'b', 'b', 1),
(14, 'barbagianni', 'torta', 1),
(15, 'culo', 'torta', 1);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `activity`
--
ALTER TABLE `activity`
  ADD CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`LOCATION_TAG_ID`) REFERENCES `favpositions` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `activity_ibfk_2` FOREIGN KEY (`START_PLACE_TAG_ID`) REFERENCES `favpositions` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `activity_ibfk_3` FOREIGN KEY (`STATUS`) REFERENCES `activity_status` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `activity_ibfk_4` FOREIGN KEY (`USER`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `boolean_preferences`
--
ALTER TABLE `boolean_preferences`
  ADD CONSTRAINT `boolean_preferences_ibfk_1` FOREIGN KEY (`MODALITY`) REFERENCES `modality` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `boolean_preferences_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `favpositions`
--
ALTER TABLE `favpositions`
  ADD CONSTRAINT `favpositions_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`ACTIVITY_ID`) REFERENCES `activity` (`KEY_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `ranged_preferences`
--
ALTER TABLE `ranged_preferences`
  ADD CONSTRAINT `ranged_preferences_ibfk_1` FOREIGN KEY (`PREF_TYPE`) REFERENCES `ranged_preference_type` (`KEY_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ranged_preferences_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`REGISTRATION_STATUS`) REFERENCES `reg_status` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
