SELECT
  COUNT(1) AS COUNT
FROM
  WFM.CLOSE_CR_AUTO_SETTING setting
WHERE
  setting.WO_TYPE_ID = :woTypeId
AND setting.UNIT_ID IN
  (
    SELECT
      UNIT_ID
    FROM
      COMMON_GNOC.UNIT unit
      CONNECT BY NOCYCLE PRIOR unit.PARENT_UNIT_ID = unit.UNIT_ID
      START WITH unit.UNIT_ID                      = :unitId )
