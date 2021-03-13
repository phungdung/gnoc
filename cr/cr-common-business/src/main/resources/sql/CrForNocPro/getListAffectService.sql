SELECT a.CR_ID crId ,
  a.AFFECTED_SERVICE_ID AS affectedServiceId,
  b.SERVICE_CODE        AS affectedServiceCode ,
  b.SERVICE_NAME        AS affectedServiceName ,
  a.INSERT_TIME         AS insertTime
FROM CR_AFFECTED_SERVICE_DETAILS a
INNER JOIN affected_services b
ON a.AFFECTED_SERVICE_ID = b.AFFECTED_SERVICE_ID
WHERE ( a.CR_ID         IN (:crId0)
