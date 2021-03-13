WITH tmp AS
  (SELECT unit_id
  FROM
    (
--     SELECT *
--     FROM COMMON_GNOC.UNIT
--     WHERE 1                    = 1
--     AND (IS_COMMITTEE          = 0
--     OR IS_COMMITTEE           IS NULL)
--       CONNECT BY prior Unit_id = PARENT_UNIT_ID
--       START WITH Unit_id       = :unitId
--     UNION
    SELECT * FROM COMMON_GNOC.UNIT WHERE Unit_id = :unitId
    )
  ORDER BY IS_COMMITTEE ASC
  ),
  tmp2 AS
  (SELECT UNIT_ID unitId,
    USER_ID userId ,
    FULLNAME fullname ,
    USERNAME username
  FROM common_gnoc.users
  WHERE unit_id  IN (select unit_id from tmp)
  ),
  tmp3 AS
  (SELECT b1.EMP_ID empId,
    b1.EMP_ARRAY empArray,
    b1.EMP_LEVEL empLevel ,
    b1.EMP_ARRAY_CHILD empChildren ,
    b2.CHILDREN_NAME arrayChildName
  FROM common_gnoc.EMPLOYEE_IMPACT b1
  JOIN COMMON_GNOC.CFG_CHILD_ARRAY b2
  ON b1.EMP_ARRAY_CHILD = b2.CHILDREN_ID
  ),
  tmp4 AS
  (SELECT CHILDREN_CODE childrenCode FROM common_gnoc.CFG_CHILD_ARRAY
  )
SELECT t1.unitId,
  t1.userName,
  t1.userId
  || ' - '
  || t1.fullName nameDisplay ,
  t3.empArray,
  t3.empLevel,
  t3.arrayChildName,
  t4.item_name empLevelName,
  CASE
    WHEN t3.empChildren != -1
    THEN t3.empChildren
    ELSE NULL
  END empChildren ,
  listagg(TO_CHAR(t2.DAYOFF,'dd/MM/yyyy'),', ') within GROUP (
ORDER BY t2.DAYOFF) dayOFF
FROM tmp2 t1
LEFT JOIN COMMON_GNOC.EMPLOYEE_DAYOFF t2
ON t1.userId = t2.EMP_ID
