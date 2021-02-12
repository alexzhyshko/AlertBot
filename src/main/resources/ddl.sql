drop database alertbot;
create database alertbot;
use alertbot;

create table `Users`(
id INT PRIMARY KEY,
username VARCHAR(64) UNIQUE
);

create table Alerts(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(64) UNIQUE,
message VARCHAR(255),
owner_id INT,
CONSTRAINT alerts_owner_id_fk FOREIGN KEY (owner_id) REFERENCES `Users`(id)
);


create table Subscriptions(
id INT PRIMARY KEY AUTO_INCREMENT,
user_id INT,
alert_id INT,
CONSTRAINT subscriptions_user_id_fk FOREIGN KEY (user_id) REFERENCES `Users`(id),
CONSTRAINT subscriptions_alert_id_fk FOREIGN KEY (alert_id) REFERENCES Alerts(id)
);

create table Blacklist(
id INT PRIMARY KEY
);
