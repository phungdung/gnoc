SELECT DISTINCT wo_code woCode,
FLOOR(avg(nvl(TASK_APPROVE, -999999))) taskApprove,
FLOOR(avg(nvl(TASK_APPROVE_AREA, -999999))) taskApproveArea
FROM open_pm.MR_SCHEDULE_BTS_HIS_DETAIL
WHERE serial    = :serial
  AND device_type = :deviceType
  AND cycle       = :cycle
