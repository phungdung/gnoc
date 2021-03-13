SELECT
  t1.country country,
  t1.sr_id srId,
  t1.sr_code srCode,
  t1.title title,
  t1.DESCRIPTION,
  t1.status status,
  t2.SERVICE_ID serviceId,
  t2.SERVICE_CODE serviceCode,
  t2.service_name serviceName,
  TO_CHAR(t1.start_time,'dd/MM/yyyy HH24:mi') startTime,
  TO_CHAR(t1.end_time,'dd/MM/yyyy HH24:mi') endTime,
  t4.username
  || '('
  || t4.fullname
  || ', '
  || t4.mobile
  || ')' AS createdUser,
  t5.UNIT_CODE
  || '('
  || T5.unit_name
  || ')' AS createdUnit,
  CASE
    WHEN t7.username IS NULL
    THEN NULL
    ELSE t7.username
      || '('
      || t7.fullname
      || ', '
      || t7.mobile
      || ')'
  END AS srUser,
  CASE
    WHEN t8.UNIT_CODE IS NULL
    THEN NULL
    ELSE t8.UNIT_CODE
      || '('
      || T8.unit_name
      || ')'
  END AS srUnit,
  t1.ROLE_CODE roleCode,
  t1.sr_unit executionUnit
FROM open_pm.sr t1
INNER JOIN open_pm.sr_catalog t2
ON ( t1.service_array = t2.service_array
AND t1.service_group  = t2.service_group
AND t1.service_id     = t2.service_id )
INNER JOIN open_pm.sr_config t3
ON t1.status        = t3.config_code
AND t3.config_group = 'STATUS'
LEFT JOIN common_gnoc.users t4
ON t1.created_user = t4.username
LEFT JOIN common_gnoc.unit t5
ON t4.unit_id = t5.unit_id
LEFT JOIN common_gnoc.users t7
ON t1.sr_user = t7.username
LEFT JOIN common_gnoc.unit t8
ON t1.sr_unit = t8.unit_id
INNER JOIN open_pm.sr_config t6
ON t6.config_code   = t2.service_code
WHERE 1             = 1
AND t6.config_group = 'DICH_VU_VSMART'
AND ( ( t1.sr_user IS NULL
AND t1.sr_unit     IN
  ( SELECT unit_id FROM sr_role_user WHERE USER_NAME = :loginUser
  ) )
OR t1.sr_user      =:loginUser
OR t1.created_user =:loginUser )
