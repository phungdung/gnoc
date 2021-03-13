SELECT DISTINCT cife.ife_id valueStr,
  ( NVL(le.LEE_VALUE,cife.ife_name)
  || ' ['
  || cife.start_time
  || ' - '
  || cife.end_time
  || ']') displayStr,
  (cife.start_time
  || ','
  || cife.end_time) secondValue
FROM open_pm.cr_impact_frame cife
JOIN open_pm.cr_process cp
ON cife.IFE_ID            = cp.IMPACT_TYPE
LEFT JOIN common_gnoc.language_exchange le
ON cife.ife_id           =le.BUSSINESS_ID
AND le.applied_system    = 2
AND le.applied_bussiness = 4
AND le.lee_locale        = :lee_locale
WHERE 1                   =1
AND NVL(cife.IS_ACTIVE,1) = 1
AND cp.CR_PROCESS_ID      =:CR_PROCESS_ID
