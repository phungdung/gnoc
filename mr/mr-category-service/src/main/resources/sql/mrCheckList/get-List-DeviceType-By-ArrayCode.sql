SELECT device_type deviceType
FROM
  (SELECT DISTINCT market_code, array_code,device_type FROM open_pm.mr_device
  UNION
  SELECT DISTINCT market_code,
    'Hệ thống nguồn và hệ thống phụ trợ tại Tổng trạm KV',
    device_type
  FROM open_pm.mr_device_cd
  ORDER BY market_code,
    array_code,
    device_type
  )
WHERE 1 = 1
