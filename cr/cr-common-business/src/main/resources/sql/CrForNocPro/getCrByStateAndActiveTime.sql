SELECT a.CR_ID          AS crId ,
  a.CR_NUMBER           AS crNumber ,
  a.CREATED_DATE        AS createdDate ,
  a.EARLIEST_START_TIME AS earliestStartTime
FROM open_pm.cr a
WHERE a.state              =:state
AND a.EARLIEST_START_TIME <= :activeTime
AND a.LATEST_START_TIME   >= :activeTime
