SELECT      u.UNIT_ID unitId,
            u.UNIT_NAME unitName,
            u.UNIT_CODE unitCode,
            u.PARENT_UNIT_ID parentUnitId,
            v.DEPT_FULL parentUnitName
FROM        common_gnoc.unit u
LEFT JOIN   (SELECT  UNIT_ID,
                    (CASE
                      WHEN UNIT_NAME = DEPT_FULL THEN NULL
                      ELSE
                        CASE
                          WHEN INSTR(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), '>') = 0
                            THEN SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5)
                          ELSE SUBSTR(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5),
                                      INSTR(SUBSTR(DEPT_FULL, 0, INSTR(DEPT_FULL, UNIT_NAME) -5), '>', -1) +2
                          ) END
                      END) DEPT_FULL
              FROM common_gnoc.v_unit) v
ON          u.UNIT_ID = v.UNIT_ID
WHERE       u.STATUS = 1
