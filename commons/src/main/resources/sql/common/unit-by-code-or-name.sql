SELECT u.UNIT_ID itemId,
  u.UNIT_NAME itemName,
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
WHERE 1 = 1
AND u.STATUS = 1
