use `upic-ski-resort`;
select * from lift;
delete from lift;
DELIMITER //
CREATE PROCEDURE createLifts()
BEGIN
    DECLARE resortId int DEFAULT 0;
    DECLARE liftNum int DEFAULT 1;
    DECLARE liftId int DEFAULT 0;
    
    WHILE resortId < 5 DO
		set liftNum = 1;
        WHILE liftNum < 9 DO
			INSERT INTO lift (liftid, lift_number, vertical_distance, resort_resortid) VALUES (liftId, liftNum, 10 * liftNum, resortId);
			set liftId = liftId + 1;
            set liftNum = liftNum + 1;
        END WHILE;
        SET resortId = resortId + 1;
    END WHILE;
END //
DELIMITER ;

call createLifts();