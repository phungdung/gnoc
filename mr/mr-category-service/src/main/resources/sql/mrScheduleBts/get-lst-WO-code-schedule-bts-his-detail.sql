SELECT DISTINCT wo_code woCode
FROM open_pm.MR_SCHEDULE_BTS_HIS_DETAIL
WHERE serial    = :serial
  AND device_type = :deviceType
  AND cycle       = :cycle
