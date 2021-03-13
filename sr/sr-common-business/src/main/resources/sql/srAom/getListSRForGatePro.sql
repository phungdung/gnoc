SELECT t1.sr_code srCode,
  t1.status status,
  t1.sr_user srUser,
  TO_CHAR(srh.CREATED_TIME, 'dd/MM/yyyy HH24:mi:ss') updatedTime,
  srf.FILE_CONTENT fileContent
FROM
  (SELECT *
  FROM open_pm.sr
  WHERE STATUS NOT IN ('Draft','New','Concluded','Closed','Rejected','Cancelled')
  ) t1
INNER JOIN open_pm.sr_catalog t2
ON ( t1.service_array = t2.service_array
AND t1.service_group  = t2.service_group
AND t1.service_id     = t2.service_id )
INNER JOIN open_pm.sr_config t6
ON t6.config_code = t2.service_code
INNER JOIN
  (SELECT MIN(created_time) created_time,
    sr_id
  FROM open_pm.sr_his
  WHERE sr_status NOT IN ('Draft','New','Concluded','Closed','Rejected','Cancelled')
  GROUP BY sr_id
  ) srh
ON t1.sr_id = srh.sr_id
LEFT JOIN open_pm.sr_files srf
ON t1.sr_id         = srf.OBEJCT_ID
AND srf.FILE_TYPE   = 'AOM'
WHERE 1             = 1
AND t6.config_group = 'DICH_VU_AOM'
