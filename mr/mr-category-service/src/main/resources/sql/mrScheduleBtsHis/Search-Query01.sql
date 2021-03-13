WITH t9 AS
   ( SELECT wo_code,
     wo_code_original,
     cycle,
     device_type,
     serial,
     TASK_APPROVE,
     APPROVE_USER,
     APPROVE_DATE,
     REASON,
     TASK_APPROVE_AREA,
     APPROVE_USER_AREA,
     APPROVE_DATE_AREA,
     REASON_AREA
   FROM open_pm.MR_SCHEDULE_BTS_HIS_DETAIL DT
   JOIN (
   SELECT MAX(wo_code) wo_code_com FROM open_pm.MR_SCHEDULE_BTS_HIS_DETAIL GROUP BY cycle, device_type, serial
   ) ST ON DT.wo_code = ST.wo_code_com
   WHERE wo_code_original IS NOT NULL
   )
SELECT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
  T1.MARKET_CODE marketCode ,
  T1.AREA_CODE areaCode ,
  T1.PROVINCE_CODE provinceCode ,
  T1.DEVICE_TYPE deviceType ,
  T1.DEVICE_ID deviceId ,
  T1.DEVICE_CODE deviceCode ,
  T1.SERIAL serial ,
  T1.CYCLE cycle ,
  T1.COMPLETE_DATE completeDate ,
  T1.WO_CODE woCode ,
  T1.USER_MANAGER userManager ,
  T1.MR_DATE mrDate ,
  T1.STATION_CODE stationCode ,
  T1.IS_COMPLETE isComplete,
  wo_goc.STATUS           AS statusWoGoc ,
  detail.WO_CODE          AS wogiaoLai ,
  wo_giao_lai.STATUS      AS statusWoGL ,
  wo_giao_lai.CREATE_DATE AS createDateGL ,
  wo_goc.CREATE_DATE      AS createDateGoc ,
  t.woCode woLstTag ,
  t9.TASK_APPROVE      AS taskApprove ,
  t9.APPROVE_USER      AS approveUser ,
  t9.APPROVE_DATE      AS approveDate ,
  t9.REASON            AS reason ,
  t9.TASK_APPROVE_AREA AS taskApproveArea ,
  t9.APPROVE_USER_AREA AS approveUserArea ,
  t9.APPROVE_DATE_AREA AS approveDateArea ,
  t9.REASON_AREA       AS reasonArea
FROM MR_SCHEDULE_BTS_HIS T1
LEFT JOIN
  (SELECT MAX(WO_CODE) AS WO_CODE,
    WO_CODE_ORIGINAL
  FROM MR_SCHEDULE_BTS_HIS_DETAIL
  WHERE WO_CODE_ORIGINAL IS NOT NULL
  GROUP BY WO_CODE_ORIGINAL
  ) detail
ON T1.WO_CODE = detail.WO_CODE_ORIGINAL
LEFT JOIN
  (SELECT WO_CODE,
    STATUS ,
    CREATE_DATE
  FROM WFM.WO
  WHERE 1       =1
  AND WO_SYSTEM = 'MR'
  AND WO_SYSTEM_ID LIKE 'MR_BDC_MFD_DH%' {createDateWo}
  ) wo_goc
ON T1.WO_CODE = wo_goc.WO_CODE
LEFT JOIN
  (SELECT WO_CODE,
    STATUS,
    CREATE_DATE
  FROM WFM.WO
  WHERE 1       =1
  AND WO_SYSTEM = 'MR'
  AND WO_SYSTEM_ID LIKE 'MR_BDC_MFD_DH%' {createDateWo}
  ) wo_giao_lai
ON detail.WO_CODE = wo_giao_lai.WO_CODE
LEFT JOIN
  (SELECT wo_code_original,
    LISTAGG(t.wo_code, ',' )WITHIN GROUP (
  ORDER BY wo_code) AS woCode
  FROM
    ( SELECT DISTINCT wo_code,
      wo_code_original,
      cycle,
      device_type,
      serial
    FROM open_pm.MR_SCHEDULE_BTS_HIS_DETAIL
    WHERE wo_code_original IS NOT NULL
    ) t
  GROUP BY wo_code_original
  ) t
ON T1.wo_Code = t.wo_code_original
LEFT JOIN t9
ON T1.wo_Code = t9.wo_code
WHERE 1       = 1
