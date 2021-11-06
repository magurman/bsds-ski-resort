use `upic-ski-resort`;
DELIMITER //
CREATE PROCEDURE createSkiers()
BEGIN
    DECLARE i int DEFAULT 0;
    WHILE i < 20000 DO
        INSERT INTO skier (skierid) VALUES (i);
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

call createSkiers();



