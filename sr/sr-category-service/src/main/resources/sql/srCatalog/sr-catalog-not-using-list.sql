SELECT b1.SERVICE_ID serviceId
FROM
  (SELECT DISTINCT service_id FROM open_pm.sr_catalog
  ) b1
WHERE TO_CHAR(b1.service_id) NOT IN
  (SELECT TO_CHAR(sr.service_id)
  FROM open_pm.sr sr
  WHERE sr.status NOT IN (:lstStatus)
  AND sr.service_id IS NOT NULL
  )
