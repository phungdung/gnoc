SELECT
  b.TROUBLE_ID troubleId,
  b.TROUBLE_CODE troubleCode,
  b.wo_code woCode,
  b.insert_source insertSource,
  CASE
    WHEN b.STATE             IN(3,5,6,8,9,10,11)
    THEN NVL(b.time_process,0)- (NVL(b.time_used,0) + ROUND( (NVL(b.clear_time,
      sysdate)                -NVL(b.assign_time_temp,b.assign_time))*24,2))
    WHEN b.STATE             IN (7)
    THEN NVL(b.time_process,0)- NVL(b.time_used,0)
    ELSE NULL
  END remainTime
FROM
  one_tm.IT_ACCOUNT a
INNER JOIN ONE_TM.TROUBLES b
ON
  a.INCIDENT_ID = b.TROUBLE_ID
WHERE
  ACCOUNT           IN (:lstAccount)
AND b.STATE NOT     IN (10,11)
AND b.AUTO_CREATE_WO = 1
AND b.CREATED_TIME   > sysdate - 30
