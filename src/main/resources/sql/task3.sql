DROP FUNCTION IF EXISTS split_str;
DROP PROCEDURE IF EXISTS column_into_rows;

delimiter //
-- Mysql has not split function, so I have to add on my own - the same as in task2, but with custom delimiter
CREATE FUNCTION split_str(s VARCHAR(1024), i INT, delim CHAR) RETURNS VARCHAR(1024) DETERMINISTIC
BEGIN
    DECLARE n INT;

    SET n = LENGTH(s) - LENGTH(REPLACE(s, delim, '')) + 1;

    IF i > n THEN
        RETURN NULL;
    ELSE
        RETURN SUBSTRING_INDEX(SUBSTRING_INDEX(s, delim, i), delim, -1);
    END IF;

END
//
CREATE PROCEDURE column_into_rows()
BEGIN
    DECLARE v_over INT;
    DECLARE v_id INT;
    DECLARE v_name VARCHAR(50);
    DECLARE v_part_name VARCHAR(50);
    DECLARE i INT;
    DECLARE sometbl_cursor CURSOR FOR
        SELECT id, name from sometbl WHERE name like '%|%';
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_over = 1;

    OPEN sometbl_cursor;

    run_update: LOOP
        FETCH sometbl_cursor INTO v_id, v_name;

		IF v_over = 1 THEN
			LEAVE run_update;
		 END IF;

		SET i = 0;
        -- Split and insert splitted records
        split_name_loop: LOOP
			SET i = i + 1;
			SET v_part_name = split_str(v_name, i, '|');
			IF v_part_name IS NULL THEN
				LEAVE split_name_loop;
			END IF;

            INSERT INTO sometbl (id, name) values (v_id, v_part_name);

		END LOOP split_name_loop;


    END LOOP run_update;

    CLOSE sometbl_cursor;

    -- Clear
    DELETE FROM sometbl WHERE name like '%|%';
END
//

DELIMITER ;
