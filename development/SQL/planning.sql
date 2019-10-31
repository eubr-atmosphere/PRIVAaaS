-- Resource Consumption per Pod
INSERT INTO QualityModel(modelName, modelDescriptionReference) VALUES ("ResConsPod", 1);

-- Metric (not all fields are present
INSERT INTO Metric(qualityModelId, metricName) VALUES (1, "ResConsPod");

-- MetricData (sample data)
INSERT INTO MetricData(qualityModelId, metricId, valueTime, value, resourceId) VALUES (1, 1, NOW(), 0.82, 9);

