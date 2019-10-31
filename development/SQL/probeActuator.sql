INSERT INTO Probe(probeId , probeName , password, salt , token) VALUES (8, "probe PRIVAaaS", "n/a", "n/a", "n/a");
INSERT INTO Resource(resourceId , resourceName , resourceType, resourceAddress) VALUES (8, "Bin PRIVAaaS", "BIN", "n/a");

INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (30, 'int', 'k', '');
INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (31, 'double', 'riskP', '');
INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (32, 'double', 'riskJ', '');
INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (33, 'double', 'riskM', '');
INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (34, 'double', 'score', '');
INSERT INTO Description (descriptionId, dataType, descriptionName, unit) VALUES (35, 'double', 'id', '');


## ------------

## Actuator INSERT by tma-admin

## ------------

INSERT INTO Metric (metricId) VALUES (1);

INSERT INTO MetricData (metricId, valueTime) VALUES (1, "1999-12-31 23:59:59");

INSERT INTO Plan (planId, metricId, valueTime) VALUES (1, 1,"1999-12-31 23:59:59");
INSERT INTO Plan (planId, metricId, valueTime) VALUES (2, 1,"1999-12-31 23:59:59");

## ------------

INSERT INTO Action(actionId,actuatorId,resourceId,actionName) VALUES(1,35001,8,"update");
INSERT INTO Action(actionId,actuatorId,resourceId,actionName) VALUES(2,35002,8,"none"  );

## ------------

INSERT INTO ActionPlan (planId,actionId) VALUES (1,1);
INSERT INTO ActionPlan (planId,actionId) VALUES (2,2);
