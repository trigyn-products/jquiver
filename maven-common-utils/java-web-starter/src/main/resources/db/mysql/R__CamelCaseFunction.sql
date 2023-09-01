DROP FUNCTION IF EXISTS camel_case;
CREATE FUNCTION `camel_case`(str varchar(500)) RETURNS varchar(500)
BEGIN
DECLARE startIndex, currentIndex INT DEFAULT 1;
DECLARE sub, camelCaseStr VARCHAR(500) DEFAULT '';

IF LENGTH(TRIM(str)) > 0 THEN
    WHILE currentIndex > 0 DO
        SET currentIndex = LOCATE('_',str,startIndex);
        IF currentIndex = 0 THEN
          SET sub = LOWER(SUBSTRING(str,startIndex));
        ELSE 
          SET sub = LOWER(SUBSTRING(str,startIndex,currentIndex-startIndex));
        END IF;
        
        IF startIndex <> 1 THEN
          SET camelCaseStr = CONCAT_WS('', camelCaseStr, CONCAT(UPPER(LEFT(sub,1)),substr(sub,2)));
        ELSE
          SET camelCaseStr = CONCAT_WS('', camelCaseStr, sub);
        END IF;
        SET startIndex = currentIndex + 1;
    END WHILE;
END IF;

RETURN TRIM(camelCaseStr);
END;
