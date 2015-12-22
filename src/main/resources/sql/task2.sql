DROP FUNCTION IF EXISTS split_str;
DROP FUNCTION IF EXISTS init_cap;

delimiter //
-- Mysql has not split function, so I have to add on my own
CREATE FUNCTION split_str(s VARCHAR(1024), i INT) RETURNS VARCHAR(1024) DETERMINISTIC
BEGIN
    DECLARE n INT;

    SET n = LENGTH(s) - LENGTH(REPLACE(s, ' ', '')) + 1;

    IF i > n THEN
        RETURN NULL;
    ELSE
        RETURN SUBSTRING_INDEX(SUBSTRING_INDEX(s, ' ', i), ' ', -1);
    END IF;

END
//

CREATE FUNCTION init_cap(s VARCHAR(1024)) RETURNS VARCHAR(1024) DETERMINISTIC
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE subs VARCHAR(32);
    DECLARE result VARCHAR(1024);
        -- Iterate over tokens (words) separated by spaces
        split_string_loop: LOOP
			SET i = i + 1;
			SET subs = split_str(s, i);
			IF subs IS NULL THEN
				LEAVE split_string_loop;
			END IF;

            SET result = CONCAT_WS(' ', result, CONCAT(UCASE(LEFT(subs, 1)), LCASE(SUBSTRING(subs, 2))) );

		END LOOP split_string_loop;

        RETURN result;
END
//

DELIMITER ;