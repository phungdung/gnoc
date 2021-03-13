SELECT
  e.wo_system_id woSystemId,
  e.WO_CODE woCode,
  us.USERNAME stationId,
  us.STAFF_CODE stationCode
FROM
  wfm.wo e
LEFT JOIN common_gnoc.users us
ON
  e.ft_id = us.USER_ID
WHERE
  e.wo_system_id = :crId
AND e.wo_system  ='CR'
