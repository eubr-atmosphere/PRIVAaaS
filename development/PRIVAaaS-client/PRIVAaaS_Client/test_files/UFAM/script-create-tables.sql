-- script tables atmosphere

CREATE DATABASE atmosphere;

CREATE TABLE atmosphere.doctor (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  register VARCHAR(255) NOT NULL UNIQUE,
  birth_date DATE NOT NULL,
  address VARCHAR(255) NULL,
  city VARCHAR(255) NULL,
  state VARCHAR(255) NULL,
  country VARCHAR(255) NULL,
  cell_phone VARCHAR(255) NOT NULL,
  specialty VARCHAR(255) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE atmosphere.patient (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  mother_name VARCHAR(255) NOT NULL,
  birth_date DATE NOT NULL,
  address VARCHAR(255) NULL,
  city VARCHAR(255) NULL,
  state VARCHAR(255) NULL,
  country VARCHAR(255) NULL,
  cell_phone VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE atmosphere.appointment (
  id INT(11) NOT NULL AUTO_INCREMENT,
  doctor_id INT(11) NOT NULL, 
  patient_id INT(11) NOT NULL, 
  appointment_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  note VARCHAR(255) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (doctor_id) REFERENCES doctor(id),
  FOREIGN KEY (patient_id) REFERENCES patient(id)
);

CREATE TABLE atmosphere.medical_exam (
  id INT(11) NOT NULL AUTO_INCREMENT,
  appointment_id INT(11) NOT NULL,   
  reason VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  exam_date DATE NULL,
  exam_result VARCHAR(255) NULL,
  status VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (appointment_id) REFERENCES appointment(id)
);

CREATE TABLE atmosphere.medication (
  id INT(11) NOT NULL AUTO_INCREMENT,
  appointment_id INT(11) NOT NULL,   
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  medication_date DATE NULL,  
  PRIMARY KEY (id),
  FOREIGN KEY (appointment_id) REFERENCES appointment(id)
);

CREATE TABLE atmosphere.profile (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,  
  PRIMARY KEY (id)
);

CREATE TABLE atmosphere.user (
  id INT(11) NOT NULL AUTO_INCREMENT,
  profile_id INT(11) NOT NULL,   
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (profile_id) REFERENCES profile(id)
);


CREATE TABLE atmosphere.appl_log (
  id INT(11) NOT NULL AUTO_INCREMENT,
  command VARCHAR(255) NOT NULL,
  text_result VARCHAR(255) NULL,
  user varchar(20) NOT NULL, 
  result INT(1) NOT NULL,
  date_exec DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE VIEW atmosphere.appl_tables AS
SELECT DISTINCT(TABLE_NAME)
FROM information_schema.columns 
WHERE table_schema='atmosphere';

CREATE VIEW atmosphere.appl_columns AS
SELECT TABLE_NAME, COLUMN_NAME 
FROM information_schema.columns 
WHERE table_schema='atmosphere';

CREATE VIEW atmosphere.appl_users AS
SELECT user
FROM mysql.user; 

CREATE TABLE atmosphere.appl_autorization_tables (
  id INT(11) NOT NULL AUTO_INCREMENT,
  table_name VARCHAR(255) NOT NULL,
  user_name VARCHAR(20) NOT NULL, 
  type VARCHAR(20) NOT NULL,  
  PRIMARY KEY (id), 
  constraint UNIQUE(table_name,user_name,type)
);

CREATE TABLE atmosphere.appl_autorization_columns (
  id INT(11) NOT NULL AUTO_INCREMENT,
  table_name VARCHAR(255) NOT NULL,
  column_name VARCHAR(255) NOT NULL, 
  user_name VARCHAR(20) NOT NULL, 
  type VARCHAR(20) NOT NULL,  
  PRIMARY KEY (id)
);

CREATE TABLE atmosphere.appl_autorization_table_register (
  id INT(11) NOT NULL AUTO_INCREMENT,
  table_name VARCHAR(255) NOT NULL,
  register_id INT(11) NOT NULL, 
  user_name VARCHAR(20) NOT NULL, 
  type VARCHAR(20) NOT NULL,  
  PRIMARY KEY (id)
);