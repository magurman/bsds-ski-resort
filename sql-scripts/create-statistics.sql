use `upic-ski-resort`;
CREATE TABLE statistics(
	statistics_id INT NOT NULL AUTO_INCREMENT,
	URL VARCHAR(100) NOT NULL,
	operation VARCHAR(10) NOT NULL,
    average_latency FLOAT,
    max_latency FLOAT,
    total_num_requests INT,
    PRIMARY KEY (statistics_id));
select * from statistics;

INSERT INTO statistics (URL, operation, average_latency, max_latency, total_num_requests) 
VALUES ("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", "GET", 0, 0, 0);
INSERT INTO statistics (URL, operation, average_latency, max_latency, total_num_requests) 
VALUES ("/skiers/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}", "POST", 0, 0, 0);