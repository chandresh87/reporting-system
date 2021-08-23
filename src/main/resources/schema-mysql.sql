CREATE DATABASE company;

USE company;

CREATE TABLE `employee` (
                            `empid` varchar(255) NOT NULL,
                            `monthly_cost` decimal(19,2) DEFAULT NULL,
                            `currency` varchar(255) DEFAULT NULL,
                            `department` varchar(255) DEFAULT NULL,
                            `title` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`empid`),
                            KEY `employee_search` (`department`,`currency`)
);


CREATE INDEX employee_search
    ON employee(department,currency);