-- -- Quality Model Properties -- --
CREATE TABLE properties (
	id BIGINT NOT NULL AUTO_INCREMENT,  
	name varchar(30) NOT NULL,
  	relevance float NOT NULL,
  	threshold float NOT NULL,
  	periodicity float NOT NULL,
	PRIMARY KEY (id)
);

-- -- Quality Model -- --

DROP TABLE IF EXISTS historicaldata;
DROP TABLE IF EXISTS attribute;
DROP TABLE IF EXISTS preference;
DROP TABLE IF EXISTS metric;
DROP TABLE IF EXISTS configurationprofile;
DROP TABLE IF EXISTS leafattribute;
DROP TABLE IF EXISTS compositeattribute;

CREATE TABLE attribute (
   attributeId INT NOT NULL AUTO_INCREMENT,
   compositeattributeId INT NOT NULL,
   name VARCHAR(1024),
   primary key(attributeId)  
);

CREATE TABLE compositeattribute (
   attributeId INT NOT NULL,
   operator INT,
   PRIMARY KEY (attributeId)
);

CREATE TABLE leafattribute (
   attributeId INT NOT NULL,
   normalizationMin DOUBLE PRECISION,
   normalizationMax DOUBLE PRECISION,
   operator INT,
   numSamples INT,
   normalizationKind INT,
   PRIMARY KEY (attributeId)
);

CREATE TABLE historicaldata (
   historicalDataId INT NOT NULL AUTO_INCREMENT,
   instant TIMESTAMP(6) NOT NULL,
   value DOUBLE PRECISION,
   attributeId INT NOT NULL,
   PRIMARY KEY (historicalDataId, instant)
);

CREATE TABLE configurationprofile (
   configurationprofileId INT NOT NULL AUTO_INCREMENT,
   PRIMARY KEY (configurationprofileId)
);

CREATE TABLE preference (
   attributeId INT NOT NULL,
   configurationprofileId INT NOT NULL,
   weight DOUBLE PRECISION,
   threshold DOUBLE PRECISION,
   PRIMARY KEY (attributeId)
);

CREATE TABLE metricqm (
   attributeId INT NOT NULL,
   configurationprofileId INT NOT NULL,
   probeName VARCHAR(1024),
   descriptionName VARCHAR(1024),
   resourceName VARCHAR(1024),
   PRIMARY KEY (attributeId)
);

ALTER TABLE historicaldata ADD CONSTRAINT FK_HistoricalData_0 FOREIGN KEY (attributeId) REFERENCES attribute (attributeId);

ALTER TABLE preference ADD CONSTRAINT FK_Preference_0 FOREIGN KEY (configurationprofileId) REFERENCES configurationprofile (configurationprofileId);
ALTER TABLE metricqm ADD CONSTRAINT FK_Metricqm_0 FOREIGN KEY (configurationprofileId) REFERENCES configurationprofile (configurationprofileId);

ALTER TABLE preference ADD CONSTRAINT FK_Preference_1 FOREIGN KEY (attributeId) REFERENCES attribute (attributeId);
ALTER TABLE metricqm ADD CONSTRAINT FK_Metricqm_1 FOREIGN KEY (attributeId) REFERENCES leafattribute (attributeId);

ALTER TABLE attribute ADD CONSTRAINT FK_Attribute_0 FOREIGN KEY (compositeattributeId) REFERENCES compositeattribute (attributeId);


-- ------------------------------------------------------------------------------

insert into compositeattribute (attributeId, operator) values (1,0);

insert into attribute (attributeId, compositeattributeId, name) values (1, 1, 'PRIVACY');
insert into attribute (attributeId, compositeattributeId, name) values (2, 1, 'INFORMATIONLOSS');
insert into attribute (attributeId, compositeattributeId, name) values (3, 1, 'REIDENTIFICATIONRISK');

insert into leafattribute (attributeId, normalizationMin, normalizationMax, operator, numSamples, normalizationKind) values (2, 0.0, 1.0, 0, 1, 0);
insert into leafattribute (attributeId, normalizationMin, normalizationMax, operator, numSamples, normalizationKind) values (3, 0.0, 1.0, 0, 1, 1);

insert into configurationprofile (configurationprofileId) values (1);

insert into preference (attributeId,configurationprofileId, weight, threshold) values (1, 1, 0.2, 0.05);
insert into preference (attributeId,configurationprofileId, weight, threshold) values (2, 1, 0.1, 0.7);
insert into preference (attributeId,configurationprofileId, weight, threshold) values (3, 1, 0.9, 0.05);

insert into metricqm (attributeId, configurationprofileId, probeName, descriptionName, resourceName) values (2,1,'probe PRIVaaS','n/a','anonymizator');
insert into metricqm (attributeId, configurationprofileId, probeName, descriptionName, resourceName) values (3,1,'probe PRIVaaS','measurement','anonymizator');

-- ------------------------------------------------------------------------------


-- ------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------
-- ATMOSPHERE - http://www.atmosphere-eubrazil.eu/
-- 
-- 
-- Trustworthiness Monitoring & Assessment Framework
-- Component: Knowledge - database
-- 
-- Repository: https://github.com/eubr-atmosphere/tma-framework
-- License: https://github.com/eubr-atmosphere/tma-framework/blob/master/LICENSE
-- ------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------
--
-- Normalized MySQL version of the script for the knowledge 
-- database creation. 
--
-- @author José Pereira <josep@dei.uc.pt>
-- @author Nuno Antunes <nmsa@dei.uc.pt>
-- 
-- ------------------------------------------------------------------------------
-- ------------------------------------------------------------------------------

DROP TABLE IF EXISTS metricdata;
DROP TABLE IF EXISTS metriccomposition;
DROP TABLE IF EXISTS metriccomponent;

DROP TABLE IF EXISTS configurationdata;
DROP TABLE IF EXISTS actionplan;
DROP TABLE IF EXISTS plan;

DROP TABLE IF EXISTS metric;
DROP TABLE IF EXISTS qualityModel;

DROP TABLE IF EXISTS data;
DROP TABLE IF EXISTS configuration;

DROP TABLE IF EXISTS action;
DROP TABLE IF EXISTS actuator;
DROP TABLE IF EXISTS description;
DROP TABLE IF EXISTS probe;
DROP TABLE IF EXISTS resource;

-- -- Table time was removed for normalization.
-- DROP TABLE IF EXISTS Time;

CREATE TABLE actuator (
    actuatorId INT NOT NULL AUTO_INCREMENT,
    address VARCHAR(1024),
    pubKey VARCHAR(1024),
    PRIMARY KEY (actuatorId)
);

CREATE TABLE description (
    descriptionId INT NOT NULL AUTO_INCREMENT,
    dataType VARCHAR(16),
    descriptionName VARCHAR(128),
    unit VARCHAR(16),
    PRIMARY KEY (descriptionId)
);

CREATE TABLE probe (
    probeId INT NOT NULL AUTO_INCREMENT,
    probeName VARCHAR(128),
    password VARCHAR(128),
    salt VARCHAR(128) NOT NULL,
    token VARCHAR(256),
    tokenExpiration TIMESTAMP(6),
    PRIMARY KEY (probeId)
);

CREATE TABLE qualitymodel (
    qualityModelId INT NOT NULL AUTO_INCREMENT,
    modelName VARCHAR(10),
    modelDescriptionReference INT,
    PRIMARY KEY (qualityModelId)
);

CREATE TABLE resource (
    resourceId INT NOT NULL AUTO_INCREMENT,
    resourceName VARCHAR(128),
    resourceType VARCHAR(16),
    resourceAddress VARCHAR(1024),
    PRIMARY KEY (resourceId)
);

CREATE TABLE action (
    actionId INT NOT NULL AUTO_INCREMENT,
    actuatorId INT NOT NULL,
    resourceId INT NOT NULL,
    actionName VARCHAR(128),
    PRIMARY KEY (actionId)
);

CREATE TABLE configuration (
    configurationId INT NOT NULL AUTO_INCREMENT,
    actionId INT NOT NULL,
    keyName VARCHAR(128),
    domain VARCHAR(1024),
    PRIMARY KEY (configurationId)
);

CREATE TABLE metric (
    metricId INT NOT NULL AUTO_INCREMENT,
    qualityModelId INT NOT NULL,
    normalizationKind VARCHAR(10),
    metricName VARCHAR(10),
    metricAggregationOperator INT,
    threshold DOUBLE PRECISION,
    blockLevel INT,
    PRIMARY KEY (metricId)
);

CREATE TABLE metricdata (
    metricId INT NOT NULL,
    valueTime TIMESTAMP(6) NOT NULL,
    qualityModelId INT NOT NULL,
    value DOUBLE PRECISION,
    resourceId INT NOT NULL,
    PRIMARY KEY (metricId,valueTime,qualityModelId)
);

CREATE TABLE plan (
    planId INT NOT NULL AUTO_INCREMENT,
    metricId INT NOT NULL,
    valueTime TIMESTAMP(6) NOT NULL,
    qualityModelId INT NOT NULL,
    status INT,
    PRIMARY KEY (planId)
);

CREATE TABLE actionplan (
    actionPlanId INT NOT NULL AUTO_INCREMENT,
    planId INT NOT NULL,
    actionId INT NOT NULL,
    executionOrder INT,
    status INT,

    PRIMARY KEY (actionPlanId)
);

CREATE TABLE configurationdata (
    actionPlanId INT NOT NULL,
    configurationId INT NOT NULL,
    value VARCHAR(1024),

    PRIMARY KEY (actionPlanId,configurationId)
);

CREATE TABLE metriccomponent (
    descriptionId INT NOT NULL,
    metricId INT NOT NULL,
    qualityModelId INT NOT NULL,
    attributeAggregationOperator INT,
    numSamples INT,
    weight DOUBLE PRECISION,

    PRIMARY KEY (descriptionId,metricId,qualityModelId)
);

-- Table created to represent a metric composed by other(s)
CREATE TABLE metriccomposition (
    parentMetric INT NOT NULL,
    childMetric INT NOT NULL,

    PRIMARY KEY (parentMetric, childMetric)
);

CREATE TABLE data (
    probeId INT NOT NULL,
    descriptionId INT NOT NULL,
    resourceId INT NOT NULL,
    valueTime TIMESTAMP(6) NOT NULL,
    value DOUBLE PRECISION,
    PRIMARY KEY (probeId,descriptionId,resourceId,valueTime)
);

ALTER TABLE action ADD CONSTRAINT FK_Action_0 FOREIGN KEY (actuatorId) REFERENCES actuator (actuatorId);
ALTER TABLE action ADD CONSTRAINT FK_Action_1 FOREIGN KEY (resourceId) REFERENCES resource (resourceId);

ALTER TABLE configuration ADD CONSTRAINT FK_Configuration_0 FOREIGN KEY (actionId) REFERENCES action (actionId);

ALTER TABLE data ADD CONSTRAINT FK_Data_0 FOREIGN KEY (probeId) REFERENCES probe (probeId);
ALTER TABLE data ADD CONSTRAINT FK_Data_1 FOREIGN KEY (descriptionId) REFERENCES description (descriptionId);
ALTER TABLE data ADD CONSTRAINT FK_Data_2 FOREIGN KEY (resourceId) REFERENCES resource (resourceId);

-- -- Table time was removed for normalization.
-- ALTER TABLE Data ADD CONSTRAINT FK_Data_3 FOREIGN KEY (valueTime) REFERENCES Time (valueTime);

ALTER TABLE metric ADD CONSTRAINT FK_Metric_0 FOREIGN KEY (qualityModelId) REFERENCES qualitymodel (qualityModelId);

ALTER TABLE metricdata ADD CONSTRAINT FK_MetricData_0 FOREIGN KEY (metricId) REFERENCES metric (metricId);
ALTER TABLE metricdata ADD CONSTRAINT FK_MetricData_1 FOREIGN KEY (resourceId) REFERENCES resource (resourceId);
ALTER TABLE metricdata ADD CONSTRAINT FK_MetricData_2 FOREIGN KEY (qualityModelId) REFERENCES qualitymodel (qualityModelId);

ALTER TABLE metriccomponent ADD CONSTRAINT FK_MetricComponent_0 FOREIGN KEY (descriptionId) REFERENCES description (descriptionId);
ALTER TABLE metriccomponent ADD CONSTRAINT FK_MetricComponent_1 FOREIGN KEY (metricId) REFERENCES metric (metricId);
ALTER TABLE metriccomponent ADD CONSTRAINT FK_MetricComponent_2 FOREIGN KEY (qualityModelId) REFERENCES qualitymodel (qualityModelId);

ALTER TABLE metriccomposition ADD CONSTRAINT FK_MetricComposition_0 FOREIGN KEY (parentMetric) REFERENCES metric (metricId);
ALTER TABLE metriccomposition ADD CONSTRAINT FK_MetricComposition_1 FOREIGN KEY
(childMetric) REFERENCES metric (metricId);

ALTER TABLE plan ADD CONSTRAINT FK_Plan_0 FOREIGN KEY (metricId) REFERENCES metric (metricId);
ALTER TABLE plan ADD CONSTRAINT FK_Plan_1 FOREIGN KEY (qualityModelId) REFERENCES qualitymodel (qualityModelId);

ALTER TABLE actionplan ADD CONSTRAINT FK_ActionPlan_0 FOREIGN KEY (planId) REFERENCES plan (planId);
ALTER TABLE actionplan ADD CONSTRAINT FK_ActionPlan_1 FOREIGN KEY (actionId) REFERENCES action (actionId);

ALTER TABLE configurationdata ADD CONSTRAINT FK_ConfigurationData_0 FOREIGN KEY (actionPlanId) REFERENCES actionplan (actionPlanId);
ALTER TABLE configurationdata ADD CONSTRAINT FK_ConfigurationData_1 FOREIGN KEY (configurationId) REFERENCES configuration (configurationId);

-- ------------------------------------------------------------------------------
-- -- Dump Data -- --

INSERT INTO description VALUES (1,'CPU_Usage','measurement','Mi'),
(2,'Memory','measurement','Bytes'),
(3,'Disk','event','n/a'),
(4,'CPU_Usage','measurement','Mi'),
(5,'Memory','measurement','Bytes'),
(6,'Disk','event','n/a'),
(7,'CPU_Usage','measurement','Mi'),
(8,'Memory','measurement','Bytes'),
(9,'Disk','event','n/a'),
(10,'CPU_Usage','measurement','Mi'),
(11,'Memory','measurement','Bytes'),
(12,'Disk','event','n/a'),
(13,'Memory','event','n/a'),
(14,'CPU_Usage','measurement','Mi'),
(15,'Memory','measurement','Bytes'),
(16,'Disk','event','n/a'),
(17,'CPU_Usage','measurement','Mi'),
(18,'Memory','measurement','Bytes'),
(19,'Disk','event','n/a'),
(20,'CPU_Usage','measurement','Mi'),
(21,'Memory','measurement','Bytes'),
(22,'Disk','event','n/a'),
(23,'CPU_Usage','measurement','Mi'),
(24,'Memory','measurement','Bytes'),
(25,'Disk','event','n/a'),
(26,'Memory','event','n/a'),
(27,'k','measurement','n/a'),
(28,'riskP','measurement','n/a'),
(29,'riskJ','measurement','n/a'),
(30,'riskM','measurement','n/a'),
(31,'lScore','measurement','n/a'),
(32,'hScore','measurement','n/a');

INSERT INTO probe VALUES (1,'probe Wildfly WSVD','n/a','n/a','n/a',NULL),
(2,'probe MySQL WSVD','n/a','n/a','n/a',NULL),
(3,'probe Demo Java','n/a','n/a','n/a',NULL),
(4,'probe Demo Python','n/a','n/a','n/a',NULL),
(5,'probe Docker','n/a','n/a','n/a',NULL),
(6,'probe Kubernetes','n/a','n/a','n/a',NULL),
(7,'probe PRIVaaS','n/a','n/a','n/a',NULL),
(8,'probe PRIVaaS','n/a','n/a','n/a',NULL);

INSERT INTO resource VALUES (1,'VM_VIRT_NODE','VM','n/a'),
(2,'Pod Apache Flume','POD','n/a'),
(3,'VM_VIRT_MASTER','VM','n/a'),
(4,'Pod MySQL','POD','n/a'),
(5,'Pod Apache Zookeeper','POD','n/a'),
(6,'Pod Apache Kafka','POD','n/a'),
(7,'Pod Monitor','POD','n/a'),
(8,'anonymizator','VM','n/a');

INSERT INTO data (probeId, descriptionId, resourceId, valueTime, value) VALUES 
(8,27,8,'1995-12-11 18:34:41.000000',10),
(8,27,8,'1995-12-12 07:13:25.000000',100),
(8,27,8,'1995-12-12 13:21:03.000000',10),
(8,27,8,'1995-12-12 18:39:44.000000',10),
(8,27,8,'1995-12-12 22:52:08.000000',10),
(8,27,8,'1995-12-13 03:16:21.000000',10),
(8,27,8,'1995-12-13 17:31:17.000000',10),
(8,28,8,'1995-12-11 18:34:41.000000',0.004545454545454545),
(8,28,8,'1995-12-12 07:13:25.000000',0.0010070493454179255),
(8,28,8,'1995-12-12 13:21:03.000000',0.05555555555555555),
(8,28,8,'1995-12-12 18:39:44.000000',0.041666666666666664),
(8,28,8,'1995-12-12 22:52:08.000000',0.07142857142857142),
(8,28,8,'1995-12-13 03:16:21.000000',0.03225806451612903),
(8,28,8,'1995-12-13 17:31:17.000000',0.005050505050505051),
(8,29,8,'1995-12-11 18:34:41.000000',0.004545454545454545),
(8,29,8,'1995-12-12 07:13:25.000000',0.0010070493454179255),
(8,29,8,'1995-12-12 13:21:03.000000',0.05555555555555555),
(8,29,8,'1995-12-12 18:39:44.000000',0.041666666666666664),
(8,29,8,'1995-12-12 22:52:08.000000',0.07142857142857142),
(8,29,8,'1995-12-13 03:16:21.000000',0.03225806451612903),
(8,29,8,'1995-12-13 17:31:17.000000',0.005050505050505051),
(8,30,8,'1995-12-11 18:34:41.000000',0.001358695652173913),
(8,30,8,'1995-12-12 07:13:25.000000',0.00015355793740978472),
(8,30,8,'1995-12-12 13:21:03.000000',0.0111731843575419),
(8,30,8,'1995-12-12 18:39:44.000000',0.012987012987012988),
(8,30,8,'1995-12-12 22:52:08.000000',0.0008909126905563256),
(8,30,8,'1995-12-13 03:16:21.000000',0.005208333333333333),
(8,30,8,'1995-12-13 17:31:17.000000',0.005050505050505051),
(8,31,8,'1995-12-11 18:34:41.000000',0),
(8,31,8,'1995-12-12 07:13:25.000000',0.5217272081421163),
(8,31,8,'1996-12-12 07:13:25.000000',0.5217272081421163),
(8,31,8,'1995-12-12 13:21:03.000000',0),
(8,31,8,'1995-12-12 18:39:44.000000',0),
(8,31,8,'1995-12-12 22:52:08.000000',0.1508305756999999),
(8,31,8,'1995-12-13 03:16:21.000000',0),
(8,31,8,'1995-12-13 17:31:17.000000',1),
(8,32,8,'1995-12-11 18:34:41.000000',0),
(8,32,8,'1995-12-12 07:13:25.000000',0.5217272081421163),
(8,32,8,'1995-12-12 13:21:03.000000',0),
(8,32,8,'1995-12-12 18:39:44.000000',0),
(8,32,8,'1995-12-12 22:52:08.000000',0.1508305756999999),
(8,32,8,'1995-12-13 03:16:21.000000',0),
(8,32,8,'1995-12-13 17:31:17.000000',1);

-- ------------------------------------------------------------------------------