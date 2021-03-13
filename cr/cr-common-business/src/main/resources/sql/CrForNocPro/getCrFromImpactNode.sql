SELECT cr.CR_ID                                            AS crId ,
  cr.CR_NUMBER                                             AS crNumber ,
  cr.TITLE                                                 AS title ,
  TO_CHAR(cr.EARLIEST_START_TIME, 'DD/MM/YYYY HH24:MI:SS') AS startActiveDate ,
  TO_CHAR(cr.LATEST_START_TIME , 'DD/MM/YYYY HH24:MI:SS')  AS endActiveDate ,
  b.USERNAME                                               AS activeUser
FROM open_pm.cr cr
LEFT JOIN COMMON_GNOC.USERS b
ON cr.CHANGE_RESPONSIBLE = b.USER_ID
WHERE cr.CR_ID          IN
  (SELECT a.CR_ID
  FROM open_pm.CR_IMPACTED_NODES a
  WHERE a.CR_ID IN (:crIds)
  AND ( 1        =0
