CREATE DATABASE IF NOT EXISTS bank_blood CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bank_blood;

CREATE TABLE IF NOT EXISTS receivers (
id bigint primary key auto_increment,
first_name varchar(50) not null,
last_name varchar(50) not null,
cin varchar(32) not null unique,
phone varchar(32) not null,
date_Of_Birth date not null,
gender char not null,
blood_type varchar(10) not null,
urgency varchar(20) not null,
status varchar(20) not null,
required_Units INT not null
);


CREATE TABLE IF NOT EXISTS donors (
id BIGINT primary key auto_increment,
first_name varchar(50) not null,
last_name varchar(50) not null,
cin varchar(32) not null unique,
phone varchar(32) not null,
date_Of_Birth DATE not null,
weight double not null,
gender char not null,
blood_Type varchar(16) not null,
availability_Status VARCHAR(20) not null,
last_Donation_Date DATE,
medical_flags varchar(255),
current_receiver_id bigint,
constraint fk_donor_current_receiver foreign key (current_receiver_id)
references receivers(id)
);


CREATE TABLE IF NOT EXISTS donations (
id bigint primary key auto_increment,
donor_id bigint not null,
receiver_id bigint not null,
donation_date date not null,
constraint fk_donation_donor foreign key (donor_id) references donors(id),
constraint fk_donation_receiver foreign key (receiver_id) references receivers(id);
);