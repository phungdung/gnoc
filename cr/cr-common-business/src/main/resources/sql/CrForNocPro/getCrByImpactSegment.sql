SELECT cr.CR_ID                                            AS crId ,
  cr.CR_NUMBER                                             AS crNumber ,
  cr.TITLE                                                 AS title ,
  TO_CHAR(cr.EARLIEST_START_TIME, 'DD/MM/YYYY HH24:MI:SS') AS startActiveDate ,
  TO_CHAR(cr.LATEST_START_TIME , 'DD/MM/YYYY HH24:MI:SS')  AS endActiveDate ,
  b.USERNAME                                               AS activeUser
FROM open_pm.cr cr
LEFT JOIN COMMON_GNOC.USERS b
ON cr.CHANGE_RESPONSIBLE = b.USER_ID
INNER JOIN IMPACT_SEGMENT ig
ON cr.IMPACT_SEGMENT = ig.IMPACT_SEGMENT_id
WHERE cr.state       =:state
