-- script insert
-- doctor
INSERT INTO atmosphere.doctor (name, specialty, register, birth_date, address, city, state, country, cell_phone) 
VALUES ('MARIO VIANA', 'GERIATRIC', '123456', '1970-7-04','STREET ONE','MANAUS','AMAZONAS','BRAZIL','+55929999999');
INSERT INTO atmosphere.doctor (name, specialty, register, birth_date, address, city, state, country, cell_phone) 
VALUES ('KATARINA JOLIE', 'GENERAL CLINIC', '123457', '1985-9-12','STREET APPLE BLUE','MANAUS','AMAZONAS','BRAZIL','+55929999991');
-- patient
INSERT INTO atmosphere.patient(name, mother_name, birth_date, address, city, state, country, cell_phone)
VALUES ('PAUL ALLAN', 'REBECA ALLAN', '1969-10-15','BOULEVARD PRINCIPAL','MANAUS','AMAZONAS','BRAZIL','+559299991212');
INSERT INTO atmosphere.patient(name, mother_name, birth_date, address, city, state, country, cell_phone)
VALUES ('ROXELE TUNER', 'CRIS TUNER', '1990-2-21','STREET MOON RIVER','MANAUS','AMAZONAS','BRAZIL','+559299994032');
-- appointment
INSERT INTO atmosphere.appointment (doctor_id, patient_id, appointment_date, start_time, end_time, note)
VALUES (1,1,'2018-5-21','08:00:00','10:00:00','');
INSERT INTO atmosphere.appointment (doctor_id, patient_id, appointment_date, start_time, end_time, note)
VALUES (1,2,'2018-3-1','10:00:00','10:30:00','');
INSERT INTO atmosphere.appointment (doctor_id, patient_id, appointment_date, start_time, end_time, note)
VALUES (2,1,'2018-1-26','14:00:00','14:20:00','');
INSERT INTO atmosphere.appointment (doctor_id, patient_id, appointment_date, start_time, end_time, note)
VALUES (2,2,'2018-4-15','16:00:00','16:30:00','');
-- medical exam
INSERT INTO atmosphere.medical_exam (appointment_id, description, reason, exam_date, exam_result, status) 
VALUES (1,'X-RAY','SUSPECTED PNEUMONIA','2018-5-21','WITHOUT CHANGES','DONE');
INSERT INTO atmosphere.medical_exam (appointment_id, description, reason, exam_date, exam_result, status) 
VALUES (2,'ELECTROCARDIOGRAM','SUSPICIOUS OF ARRHYTHMIA','2018-3-5','SINUSAL TAQUICARDIA','DONE');
INSERT INTO atmosphere.medical_exam (appointment_id, description, reason, exam_date, exam_result, status) 
VALUES (3,'COMPLETE BLOOD COUNT','ROUTINE','2018-2-20','DIABETES','DONE');
INSERT INTO atmosphere.medical_exam (appointment_id, description, reason, exam_date, exam_result, status) 
VALUES (4,'FEZES EXAM','SUSPICIOUS FOR AMEBA','2018-4-18','POSITIVE FROM ENTAMOEBA COLI','DONE');
-- medication
INSERT INTO atmosphere.medication (appointment_id, name, description, medication_date) 
VALUES (1, 'DIPRINE', '3 X DAY FOR 7 DAYS', NULL);
INSERT INTO atmosphere.medication (appointment_id, name, description, medication_date) 
VALUES (2, 'ASPIRIN', 'SINGLE DOSE', NULL);
INSERT INTO atmosphere.medication (appointment_id, name, description, medication_date) 
VALUES (3, 'VITAMIN C', 'SINGLE DOSE', NULL);
INSERT INTO atmosphere.medication (appointment_id, name, description, medication_date) 
VALUES (4, 'REMEDY FOR AMEBA', 'DOUBLE DOSE', NULL);
-- profile
INSERT INTO atmosphere.profile (name) VALUES ('DOCTOR');
INSERT INTO atmosphere.profile (name) VALUES ('PATIENT');
INSERT INTO atmosphere.profile (name) VALUES ('ADMINISTRATOR');
INSERT INTO atmosphere.profile (name) VALUES ('NURSERY');
INSERT INTO atmosphere.profile (name) VALUES ('SECRETARY');
-- appl_autorization_tables
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('DOCTOR','ROOT','INSERT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('DOCTOR','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('DOCTOR','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('APPOINTMENT','ROOT','INSERT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('APPOINTMENT','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('APPOINTMENT','ROOT','DELETE');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('APPOINTMENT','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('MEDICAL_EXAM','ROOT','INSERT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('MEDICAL_EXAM','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_tables (table_name, user_name, type)
VALUES ('MEDICATION','ROOT','SELECT');
-- appl_autorization_columns
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','NAME','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','REGISTER','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','SPECIALTY','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','ADDRESS','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','CITY','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','STATE','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('DOCTOR','COUNTRY','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('APPOINTMENT','APPOINTMENT_DATE','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('APPOINTMENT','START_TIME','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('APPOINTMENT','END_TIME','ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('MEDICAL_EXAM','ID','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('MEDICAL_EXAM','APPOINTMENT_ID','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('MEDICAL_EXAM','REASON','ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_columns (table_name,column_name,user_name,type)
VALUES ('MEDICAL_EXAM','DESCRIPTION','ROOT','SELECT');
-- appl_autorization_table_register
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',1,'ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',1,'ROOT','UPDATE');
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',1,'ROOT','DELETE');
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',4,'ROOT','SELECT');
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',4,'ROOT','DELETE');
INSERT INTO atmosphere.appl_autorization_table_register (table_name,register_id,user_name,type)
VALUES ('DOCTOR',4,'ROOT','UPDATE');



