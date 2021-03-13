SELECT subQr.unit_id unitId,
  subQr.unit_code unitCode,
  CASE
    WHEN subQr.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(subQr.unit_code
      || ' ('
      ||subQr.unit_name
      ||')')
  END    AS unitName,
  0      AS status,
  ROWNUM AS madtLevel
FROM
  (SELECT ut.*,
    level AS lv
  FROM common_gnoc.unit ut
    START WITH ut.unit_id IN
    ( SELECT unit_id FROM common_gnoc.users WHERE user_id = :userID
    )
    CONNECT BY PRIOR parent_unit_id = unit_id
  ) subQr
WHERE subQr.is_committee = 0
AND ROWNUM               < 3
ORDER BY subQr.lv
