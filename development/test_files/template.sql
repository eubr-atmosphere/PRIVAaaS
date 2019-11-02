SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ALLOW_INVALID_DATES';

DROP DATABASE IF EXISTS `PRIVaaS`;
CREATE SCHEMA IF NOT EXISTS `PRIVaaS` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `PRIVaaS` ;


-- -----------------------------------------------------
-- Table 
-- -----------------------------------------------------

DROP TABLE IF EXISTS `PRIVaaS`.`ADULTO` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`ADULTO` (
    `id`		INT		NOT NULL AUTO_INCREMENT,	
    `age` 		INT		NOT NULL,
    `workclass`		VARCHAR(45)	NOT NULL,
    `fnlwgt`		INT		NOT NULL,
    `education`		VARCHAR(45)	NOT NULL,
    `education2`	INT		NOT NULL,
    `marital-status`	VARCHAR(45)	NOT NULL,
    `occupation`	VARCHAR(45)	NOT NULL,
    `relationship`	VARCHAR(45)	NOT NULL,
    `race`		VARCHAR(45)	NOT NULL,
    `sex`		VARCHAR(45)	NOT NULL,
    `capital-gain`	INT		NOT NULL,
    `capital-loss`	INT		NOT NULL,
    `hours-per-week`	INT		NOT NULL,
    `native-country`	VARCHAR(45)	NOT NULL,
    `Class`		VARCHAR(45)	NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`CMC` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`CMC` (
    `id`                		INT	NOT NULL AUTO_INCREMENT,
    `Wifes age`				INT     NOT NULL,
    `Wifes education`			INT     NOT NULL,
    `Husbandseducation`			INT     NOT NULL,
    `Number of children ever born`	INT     NOT NULL,
    `Wifes religion`			INT     NOT NULL,
    `Wifes now working?`		INT     NOT NULL,
    `Husbands occupation`		INT     NOT NULL,
    `Standard-of-living index`		INT     NOT NULL,
    `Media exposure`			INT     NOT NULL,
    `Contraceptive method used`		INT     NOT NULL,
     PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`DERMAOLOGY` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`DERMAOLOGY` (
    `id`	INT	NOT NULL AUTO_INCREMENT,
    `1`		INT     NOT NULL,
    `2`		INT     NOT NULL,
    `3`		INT     NOT NULL,
    `4`		INT     NOT NULL,
    `5`		INT     NOT NULL,
    `6`		INT     NOT NULL,
    `7`		INT     NOT NULL,
    `8`		INT     NOT NULL,
    `9`		INT     NOT NULL,
    `10`	INT     NOT NULL,
    `11`	INT     NOT NULL,
    `12`	INT     NOT NULL,
    `13`	INT     NOT NULL,
    `14`	INT     NOT NULL,
    `15`	INT     NOT NULL,
    `16`	INT     NOT NULL,
    `17`	INT     NOT NULL,
    `18`	INT     NOT NULL,
    `19`	INT     NOT NULL,
    `20`	INT     NOT NULL,
    `21`	INT     NOT NULL,
    `22`	INT     NOT NULL,
    `23`	INT     NOT NULL,
    `24`	INT     NOT NULL,
    `25`	INT     NOT NULL,
    `26`	INT     NOT NULL,
    `27`	INT     NOT NULL,
    `28`	INT     NOT NULL,
    `29`	INT     NOT NULL,
    `30`	INT     NOT NULL,
    `31`	INT     NOT NULL,
    `32`	INT     NOT NULL,
    `33`	INT     NOT NULL,
    `34`	INT     NOT NULL,
    `35`	INT     NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`HEPATITIS` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`HEPATITIS` (
    `id`	INT	NOT NULL AUTO_INCREMENT,
    `Class`		INT     	NOT NULL,
    `AGE`		INT     	NOT NULL,
    `SEX`		INT     	NOT NULL,
    `STEROID`		VARCHAR(45)     NOT NULL,
    `ANTIVIRALS`	INT     	NOT NULL,
    `FATIGUE`		INT     	NOT NULL,
    `MALAISE`		INT     	NOT NULL,
    `ANOREXIA`		INT     	NOT NULL,
    `LIVER BIG`		INT     	NOT NULL,
    `LIVER FIRM`	INT     	NOT NULL,	
    `SPLEEN PALPABLE`	INT     	NOT NULL,
    `SPIDERS`		INT     	NOT NULL,
    `ASCITES:`		INT     	NOT NULL,
    `VARICES`		INT		NOT NULL,
    `BILIRUBIN`		VARCHAR(45)     NOT NULL,
    `ALK PHOSPHATE:`	VARCHAR(45)     NOT NULL,
    `SGOT`		VARCHAR(45)     NOT NULL,
    `ALBUMIN`		VARCHAR(45)     NOT NULL,
    `PROTIME`		VARCHAR(45)     NOT NULL,
    `HISTOLOGY`		INT     	NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`INTERNET` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`INTERNET` (
    `id`					INT	NOT NULL AUTO_INCREMENT,
    `ActualTime`				INT     NOT NULL,
    `Age`					INT     NOT NULL,
    `CommunityBuilding`				INT     NOT NULL,
    `CommunityMembership_Family`		INT     NOT NULL,		
    `CommunityMembership_Hobbies`		INT     NOT NULL,
    `CommunityMembership_None`			INT     NOT NULL,
    `CommunityMembership_Other`			INT     NOT NULL,
    `CommunityMembership_Political`		INT     NOT NULL,
    `CommunityMembership_Professional`		INT     NOT NULL,
    `CommunityMembership_Religious`		INT     NOT NULL,
    `CommunityMembership_Support`		INT     NOT NULL,
    `Country`					INT     NOT NULL,
    `Disability_Cognitive`			INT     NOT NULL,
    `Disability_Hearing`			INT     NOT NULL,
    `Disability_Motor`				INT     NOT NULL,
    `Disability_NotImpaired`			INT     NOT NULL,
    `Disability_NotSay`				INT     NOT NULL,
    `Disability_Vision`				INT     NOT NULL,
    `EducationAttainment`			INT     NOT NULL,
    `FalsificationofInformation`		INT     NOT NULL,
    `Gender`					INT     NOT NULL,
    `HouseholdIncome`				INT     NOT NULL,
    `HowYouHeardAboutSurvey_Banner`		INT     NOT NULL,
    `HowYouHeardAboutSurvey_Friend`		INT     NOT NULL,
    `HowYouHeardAboutSurvey_MailingList`	INT     NOT NULL,
    `HowYouHeardAboutSurvey_Others`		INT     NOT NULL,
    `HowYouHeardAboutSurvey_PrintedMedia`	INT     NOT NULL,
    `HowYouHeardAboutSurvey_Remebered`		INT     NOT NULL,
    `HowYouHeardAboutSurvey_SearchEngine`	INT     NOT NULL,
    `HowYouHeardAboutSurvey_UsenetNews`		INT     NOT NULL,
    `HowYouHeardAboutSurvey_WWWPage`		INT     NOT NULL,
    `MajorGeographicalLocation`			INT     NOT NULL,
    `MajorOccupation`				INT     NOT NULL,
    `MaritalStatus`				INT     NOT NULL,
    `MostImportIssueFacingtheInternet`		INT     NOT NULL,
    `OpinionsonCensorship`			INT     NOT NULL,
    `PrimaryComputingPlatform`			INT     NOT NULL,
    `PrimaryLanguage`				INT     NOT NULL,
    `PrimaryPlaceofWWWAccess`			INT     NOT NULL,
    `Race,NotPurchasing_Badexperience`		INT     NOT NULL,
    `NotPurchasing_Badpress`			INT     NOT NULL,
    `NotPurchasing_Cantfind`			INT     NOT NULL,
    `NotPurchasing_Companypolicy`		INT     NOT NULL,
    `NotPurchasing_Easierlocally`		INT     NOT NULL,
    `NotPurchasing_Enoughinfo`			INT     NOT NULL,
    `NotPurchasing_Judgequality`		INT     NOT NULL,
    `NotPurchasing_Nevertried`			INT     NOT NULL,
    `NotPurchasing_Nocredit`			INT     NOT NULL,
    `NotPurchasing_Notapplicable`		INT     NOT NULL,
    `NotPurchasing_Notoption`			INT     NOT NULL,
    `NotPurchasing_Other`			INT     NOT NULL,
    `NotPurchasing_Preferpeople`		INT     NOT NULL,
    `NotPurchasing_Privacy`			INT     NOT NULL,
    `NotPurchasing_Receipt`			INT     NOT NULL,
    `NotPurchasing_Security`			INT     NOT NULL,
    `NotPurchasing_Toocomplicated`		INT     NOT NULL,
    `NotPurchasing_Uncomfortable`		INT     NOT NULL,
    `NotPurchasing_Unfamiliarvendor`		INT     NOT NULL,
    `RegisteredtoVote`				INT     NOT NULL,
    `SexualPreference`				INT     NOT NULL,
    `WebOrdering`				INT     NOT NULL,
    `WebPageCreation`				INT     NOT NULL,
    `WhoPaysforAccess_DontKnow`			INT     NOT NULL,
    `WhoPaysforAccess_Other`			INT     NOT NULL,
    `WhoPaysforAccess_Parents`			INT     NOT NULL,
    `WhoPaysforAccess_School`			INT     NOT NULL,
    `WhoPaysforAccess_Self`			INT     NOT NULL,
    `WhoPaysforAccess_Work`			INT     NOT NULL,
    `WillingnesstoPayFees`			INT     NOT NULL,
    `YearsonInternet`				INT     NOT NULL,
    `x`						INT     NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`INTERNET` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`MAMOGRAFIA` (
    `id`	INT		NOT NULL AUTO_INCREMENT,
    `BI-RADS`	VARCHAR(45)     NOT NULL,
    `Age`	INT     	NOT NULL,
    `Shape`	VARCHAR(45)     NOT NULL,
    `Margin`	VARCHAR(45)     NOT NULL,
    `Density`	VARCHAR(45)     NOT NULL,
    `Severity`	INT     	NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`RISK_FACTORS_CERVICAL_CANCER` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`RISK_FACTORS_CERVICAL_CANCER` (
    `id`					INT		NOT NULL AUTO_INCREMENT,
    `Age`					INT             NOT NULL,
    `Numberofsexualpartners`			DECIMAL		NOT NULL,
    `Firstsexualintercourse`			VARCHAR(45)     NOT NULL,
    `Numofpregnancies`				VARCHAR(45)     NOT NULL,
    `Smokes`					DECIMAL         NOT NULL,
    `Smokes(years)`				DECIMAL         NOT NULL,
    `Smokes(packs/year)`			DECIMAL         NOT NULL,
    `HormonalContraceptives`			DECIMAL         NOT NULL,
    `HormonalContraceptives(years)`		DECIMAL         NOT NULL,
    `IUD`					VARCHAR(45)     NOT NULL,
    `IUD(years)`				VARCHAR(45)     NOT NULL,
    `STDs`					DECIMAL         NOT NULL,
    `STDs(number)`				DECIMAL         NOT NULL,
    `STDs:condylomatosis`			DECIMAL         NOT NULL,
    `STDs:cervicalcondylomatosis`		DECIMAL         NOT NULL,
    `STDs:vaginalcondylomatosis`		DECIMAL         NOT NULL,
    `STDs:vulvo-perinealcondylomatosis`		DECIMAL         NOT NULL,
    `STDs:syphilis`				DECIMAL         NOT NULL,
    `STDs:pelvicinflammatorydisease`		DECIMAL         NOT NULL,
    `STDs:genitalherpes`			DECIMAL         NOT NULL,
    `STDs:molluscumcontagiosum`			DECIMAL         NOT NULL,
    `STDs:AIDS`					DECIMAL         NOT NULL,
    `STDs:HIV`					DECIMAL         NOT NULL,
    `STDs:HepatitisB`				DECIMAL         NOT NULL,
    `STDs:HPV`					DECIMAL         NOT NULL,
    `STDs:Numberofdiagnosis`			DECIMAL         NOT NULL,
    `STDs:Timesincefirstdiagnosis`		VARCHAR(45)     NOT NULL,
    `STDs:Timesincelastdiagnosis`		VARCHAR(45)     NOT NULL,
    `Dx:Cancer`					INT             NOT NULL,
    `Dx:CIN`					INT             NOT NULL,
    `Dx:HPV`					INT             NOT NULL,
    `Dx`					INT             NOT NULL,
    `Hinselmann`				INT             NOT NULL,
    `Schiller`					INT             NOT NULL,
    `Citology`					INT             NOT NULL,
    `Biopsy`					INT             NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



DROP TABLE IF EXISTS `PRIVaaS`.`WDBC` ;
CREATE TABLE IF NOT EXISTS `PRIVaaS`.`WDBC` (
    `id`	INT		NOT NULL AUTO_INCREMENT,
    `c1`	INT             NOT NULL,
    `c2`	VARCHAR(45)     NOT NULL,
    `c3`	INT             NOT NULL,
    `c4`	INT             NOT NULL,
    `c5`	INT             NOT NULL,
    `c6`	INT             NOT NULL,
    `c7`	INT             NOT NULL,
    `c8`	INT             NOT NULL,
    `c9`	INT             NOT NULL,
    `c10`	INT             NOT NULL,
    `c11`	INT             NOT NULL,
    `c12`	INT             NOT NULL,
    `c13`	INT             NOT NULL,
    `c14`	INT             NOT NULL,
    `c15`	INT             NOT NULL,
    `c16`	INT             NOT NULL,
    `c17`	INT             NOT NULL,
    `c18`	INT             NOT NULL,
    `c19`	INT             NOT NULL,
    `c20`	INT             NOT NULL,
    `c21`	INT             NOT NULL,
    `c22`	INT             NOT NULL,
    `c23`	INT             NOT NULL,
    `c24`	INT             NOT NULL,
    `c25`	INT             NOT NULL,
    `c26`	INT             NOT NULL,
    `c27`	INT             NOT NULL,
    `c28`	INT             NOT NULL,
    `c29`	INT             NOT NULL,
    `c30`	INT             NOT NULL,
    `c31`	INT             NOT NULL,
    `c32`	INT             NOT NULL,
    `c33`	INT             NOT NULL,
    `c34`	INT             NOT NULL,
    `c35`	VARCHAR(45)     NOT NULL,
    PRIMARY KEY (`id`))
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



-- ---------------------------------------------------------------------------
-- CREATE USER
-- ---------------------------------------------------------------------------
-- GRANT ALL PRIVILEGES ON PRIVaaS.* TO 'PRIVaaS'@'localhost' IDENTIFIED BY 'password';
-- GRANT ALL PRIVILEGES ON PRIVaaS.* TO 'PRIVaaS'@'%'         IDENTIFIED BY 'password' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
