-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: 12 nov, 2017 at 11:24 AM
-- Versione MySQL: 5.5.8
-- Versione PHP: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `travlendar`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `activity`
--

CREATE TABLE IF NOT EXISTS `activity` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER` int(10) unsigned NOT NULL,
  `LABEL` varchar(100) NOT NULL,
  `NOTES` varchar(100) DEFAULT NULL,
  `LOCATION_TEXT` varchar(100) DEFAULT NULL,
  `LOCATION_TAG_ID` int(10) unsigned DEFAULT NULL,
  `START_PLACE_TEXT` varchar(100) DEFAULT NULL,
  `START_PLACE_TAG_ID` int(10) unsigned DEFAULT NULL,
  `FLEXIBLE` tinyint(1) NOT NULL,
  `DURATION` int(11) DEFAULT NULL COMMENT 'minutes',
  `START_DAY_TIME` bigint(20) NOT NULL,
  `END_DAY_TIME` bigint(20) NOT NULL,
  `STATUS` int(10) unsigned NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  KEY `LOCATION_TAG_ID` (`LOCATION_TAG_ID`),
  KEY `START_PLACE_TAG_ID` (`START_PLACE_TAG_ID`),
  KEY `STATUS` (`STATUS`),
  KEY `USER` (`USER`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dump dei dati per la tabella `activity`
--

INSERT INTO `activity` (`KEY_ID`, `USER`, `LABEL`, `NOTES`, `LOCATION_TEXT`, `LOCATION_TAG_ID`, `START_PLACE_TEXT`, `START_PLACE_TAG_ID`, `FLEXIBLE`, `DURATION`, `START_DAY_TIME`, `END_DAY_TIME`, `STATUS`) VALUES
(2, 1, 'aaa', 'bbb', 'politecnico di milano', NULL, 'via caduti di marcinelle 2 milano', NULL, 0, NULL, 1510269834746, 1510277035746, 1),
(3, 1, 'br', 'br1', NULL, 1, NULL, 2, 0, NULL, 1510269834746, 1510277035746, 1),
(4, 1, 'br', 'br1', NULL, 1, NULL, 2, 0, NULL, 1510269834746, 1510277035746, 1),
(6, 1, 'prova', 'note di prova', NULL, 1, 'via caduti di marcinelle 2 milano', NULL, 0, 0, 1510306035081, 1510306045881, 2),
(7, 1, 'prova', 'note di prova', NULL, 1, 'via caduti di marcinelle 2 milano', NULL, 0, 0, 1510306035315, 1510306046115, 2),
(8, 1, 'prova', 'note di prova111', 'via stefanardo da vimercate 95 milano', NULL, 'via caduti di marcinelle 2 milano', NULL, 0, 0, 1510485804167, 1510485814967, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `activity_status`
--

CREATE TABLE IF NOT EXISTS `activity_status` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ACTIVITY_STATUS_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `ACTIVITY_STATUS` (`ACTIVITY_STATUS_NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

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

CREATE TABLE IF NOT EXISTS `boolean_preferences` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `PERSONAL_CAR` tinyint(1) NOT NULL,
  `CAR_SHARING` tinyint(1) NOT NULL,
  `PERSONAL_BIKE` tinyint(1) NOT NULL,
  `BIKE_SHARING` tinyint(1) NOT NULL,
  `PUBLIC_TRANSPORT` tinyint(1) NOT NULL,
  `UBER_TAXI` tinyint(1) NOT NULL,
  `MODALITY` int(10) unsigned NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `USER_ID` (`USER_ID`),
  KEY `MODALITY` (`MODALITY`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dump dei dati per la tabella `boolean_preferences`
--

INSERT INTO `boolean_preferences` (`KEY_ID`, `USER_ID`, `PERSONAL_CAR`, `CAR_SHARING`, `PERSONAL_BIKE`, `BIKE_SHARING`, `PUBLIC_TRANSPORT`, `UBER_TAXI`, `MODALITY`) VALUES
(1, 1, 1, 1, 1, 1, 1, 1, 4),
(8, 2, 1, 1, 1, 1, 1, 1, 4),
(9, 3, 1, 1, 0, 0, 0, 0, 1),
(10, 4, 0, 1, 0, 1, 0, 0, 1),
(11, 6, 0, 1, 1, 0, 0, 0, 1),
(12, 7, 1, 1, 1, 0, 1, 1, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `favpositions`
--

CREATE TABLE IF NOT EXISTS `favpositions` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `LATITUDE` double NOT NULL,
  `LONGITUDE` double NOT NULL,
  `TAG` text NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  KEY `USER_ID` (`USER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dump dei dati per la tabella `favpositions`
--

INSERT INTO `favpositions` (`KEY_ID`, `USER_ID`, `LATITUDE`, `LONGITUDE`, `TAG`) VALUES
(1, 1, 40.4781071, -100.2272764, 'CANION'),
(2, 2, 40.4781071, -112.2272764, 'GIUSTO CANYON');

-- --------------------------------------------------------

--
-- Struttura della tabella `modality`
--

CREATE TABLE IF NOT EXISTS `modality` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `MODALITY_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `MODALITY_NAME` (`MODALITY_NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

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
-- Struttura della tabella `ranged_preferences`
--

CREATE TABLE IF NOT EXISTS `ranged_preferences` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USER_ID` int(10) unsigned NOT NULL,
  `PREF_TYPE` int(10) unsigned NOT NULL,
  `VALUE` int(10) unsigned NOT NULL COMMENT 'minutes',
  PRIMARY KEY (`KEY_ID`),
  KEY `PREF_TYPE` (`PREF_TYPE`),
  KEY `USER_ID` (`USER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dump dei dati per la tabella `ranged_preferences`
--

INSERT INTO `ranged_preferences` (`KEY_ID`, `USER_ID`, `PREF_TYPE`, `VALUE`) VALUES
(2, 1, 4, 6),
(3, 2, 1, 6),
(4, 1, 1, 5);

-- --------------------------------------------------------

--
-- Struttura della tabella `ranged_preference_type`
--

CREATE TABLE IF NOT EXISTS `ranged_preference_type` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE_NAME` varchar(30) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `TYPE_NAME` (`TYPE_NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

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

CREATE TABLE IF NOT EXISTS `reg_status` (
  `KEY_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `STATUS_NAME` varchar(20) NOT NULL,
  PRIMARY KEY (`KEY_ID`),
  UNIQUE KEY `STATUS_NAME` (`STATUS_NAME`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

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

CREATE TABLE IF NOT EXISTS `user` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERNAME` text NOT NULL,
  `PASSWORD` text NOT NULL,
  `REGISTRATION_STATUS` int(10) unsigned NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `REGISTRATION_STATUS` (`REGISTRATION_STATUS`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dump dei dati per la tabella `user`
--

INSERT INTO `user` (`ID`, `USERNAME`, `PASSWORD`, `REGISTRATION_STATUS`) VALUES
(1, 'cane', 'cane', 1),
(2, 'GIOVANNI', 'DONDONI', 1),
(3, 'LUCA', 'DONDONDONDONDONDONDONDONDONDONDONDONDODNODNODNODNDONDONDODNODNDONDONDODNODNDONDONI', 1),
(4, 'MATTIA', 'CULO', 1),
(6, 'xfgf', 'xdx', 3),
(7, 'cande', 's', 1);

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
  ADD CONSTRAINT `boolean_preferences_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `boolean_preferences_ibfk_1` FOREIGN KEY (`MODALITY`) REFERENCES `modality` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limiti per la tabella `favpositions`
--
ALTER TABLE `favpositions`
  ADD CONSTRAINT `favpositions_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `ranged_preferences`
--
ALTER TABLE `ranged_preferences`
  ADD CONSTRAINT `ranged_preferences_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ranged_preferences_ibfk_1` FOREIGN KEY (`PREF_TYPE`) REFERENCES `ranged_preference_type` (`KEY_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`REGISTRATION_STATUS`) REFERENCES `reg_status` (`KEY_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
