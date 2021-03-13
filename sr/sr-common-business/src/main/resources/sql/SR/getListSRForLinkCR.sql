SELECT c.service_code serviceCode ,
  c.SERVICE_NAME serviceName ,
  sr.sr_code srCode ,
  sr.TITLE title ,
  sr.STATUS status ,
  sr.START_TIME startTime ,
  sr.END_TIME endTime
FROM open_pm.sr sr,
  SR_CATALOG c
WHERE sr.service_id = c.service_id
AND SR_USER         = :loginUser
AND sr.STATUS      IN
  (SELECT config_code
  FROM open_pm.sr_config
  WHERE config_group = 'STATUS_LINK_CR'
  )
