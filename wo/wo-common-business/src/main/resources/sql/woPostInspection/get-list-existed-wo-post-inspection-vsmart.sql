SELECT
  a.ID id,
  a.CD_ID cdId
FROM
  WFM.WO_POST_INSPECTION a,
  WFM.WO b,
  WFM.WO_TYPE c
WHERE
  a.wo_id = b.wo_id
  AND b.wo_type_id = c.wo_type_id
  AND TO_CHAR(sysdate, 'mm') = TO_CHAR(b.create_date,'mm')
  AND TO_CHAR(b.create_date,'dd') < 16
