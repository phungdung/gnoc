WITH list_location AS (
	SELECT
  a.LOCATION_ID location_id
	FROM common_gnoc.cat_location a
  START WITH (:p_location is null and a.parent_id is null) or (:p_location is not null and a.location_id = :p_location)
  CONNECT BY PRIOR a.location_id = a.parent_id 
),
list_result AS (
SELECT u.UNIT_ID itemId,
  CASE
    WHEN u.UNIT_CODE IS NULL
    THEN ''
    ELSE TO_CHAR(u.UNIT_CODE
      || ' ('
      || u.UNIT_NAME
      || ')')
  END AS itemName,
  u.UNIT_CODE itemCode,
  u.PARENT_UNIT_ID parenItemId,
  v.DEPT_FULL parenItemName
FROM common_gnoc.unit u
LEFT JOIN (SELECT  UNIT_ID,
        (CASE
          WHEN UNIT_NAME = DEPT_FULL THEN NULL
          ELSE
            CASE
              WHEN INSTR(NVL(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), DEPT_FULL), '>') = 0
                THEN NVL(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), DEPT_FULL)
              ELSE SUBSTR(NVL(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), DEPT_FULL),
                  INSTR(NVL(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), DEPT_FULL), '>', -1) +2
              ) END
          END) DEPT_FULL
  FROM common_gnoc.v_unit) v
ON u.UNIT_ID = v.UNIT_ID
INNER JOIN list_location lc ON lc.location_id = u.LOCATION_ID
WHERE u.STATUS = 1
)
SELECT ls.* FROM
   (SELECT lr.*, ROW_NUMBER() OVER (ORDER BY itemName) RO FROM list_result lr
   WHERE 1 = 1
