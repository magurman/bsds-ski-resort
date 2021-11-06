SELECT * FROM `upic-ski-resort`.resort;
DELIMITER //
CREATE PROCEDURE createResorts()
BEGIN
	INSERT INTO resort (resortid, name) VALUES (0, "Blue Hills");
    INSERT INTO resort (resortid, name) VALUES (1, "Nashoba");
    INSERT INTO resort (resortid, name) VALUES (2, "Wachusett");
    INSERT INTO resort (resortid, name) VALUES (3, "Catamount");
    INSERT INTO resort (resortid, name) VALUES (4, "Jiminy-Peak");
END //
DELIMITER ;

call createResorts();