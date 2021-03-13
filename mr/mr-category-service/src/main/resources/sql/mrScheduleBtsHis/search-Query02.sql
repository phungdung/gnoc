WITH tmp_user_cfg_sms as(
  select USERS_ID,
  USERNAME,
  AREA_CODE,
  PROVINCE_CODE
  from MR_USER_CFG_APPROVED_SMS_BTS
  WHERE 1=1
  AND MR_USER_CFG_APPROVED_SMS_BTS.USERNAME = :userName
  ),
tmp_province as (
SELECT PRE_CODE_STATION, LOCATION_CODE, LOCATION_NAME, PARENT_ID
FROM common_gnoc.cat_location
WHERE status                   = 1
AND level                      = 3
  START WITH parent_id        IS NULL
  CONNECT BY prior location_id = parent_id
),
t9 AS
(
SELECT NVL(T1.WO_CODE_ORIGINAL, T1.WO_CODE) WO_CODE,
       max(T1.APPROVE_DATE) APPROVE_DATE,
       MIN(NVL(T1.TASK_APPROVE_AREA, -1)) as tonTaiTuChoiCap2,
       MIN(NVL(T1.TASK_APPROVE, -1)) as tonTaiTuChoiCap1
FROM MR_SCHEDULE_BTS_HIS_DETAIL t1
JOIN ( select CHECKLIST_ID, SERIAL , MAX(WO_CODE) WO_CODE from MR_SCHEDULE_BTS_HIS_DETAIL
GROUP BY CHECKLIST_ID, SERIAL) tmp
ON t1.CHECKLIST_ID = tmp.CHECKLIST_ID AND t1.serial = tmp.serial
WHERE tmp.WO_CODE = t1.WO_CODE
group by NVL(T1.WO_CODE_ORIGINAL, T1.WO_CODE)
)
SELECT DISTINCT T1.MR_DEVICE_HIS_ID mrDeviceHisId ,
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
  t.woCode woLstTag,
  t9.tonTaiTuChoiCap1 as taskApprove,
  t9.tonTaiTuChoiCap2 as taskApproveArea,
  TO_CHAR(t9.APPROVE_DATE, 'dd/MM/yyyy HH24:mi:ss') AS approveDate
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
  AND WO_SYSTEM_ID LIKE 'MR_BDC_MFD_DH%'
  ) wo_goc
ON T1.WO_CODE = wo_goc.WO_CODE
LEFT JOIN
  (SELECT WO_CODE,
    STATUS,
    CREATE_DATE
  FROM WFM.WO
  WHERE 1       =1
  AND WO_SYSTEM = 'MR'
  AND WO_SYSTEM_ID LIKE 'MR_BDC_MFD_DH%'
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
LEFT JOIN tmp_province cc
on T1.PROVINCE_CODE = cc.pre_code_station
JOIN tmp_user_cfg_sms tmp
ON (
case when cc.location_code = tmp.PROVINCE_CODE then 1
    when tmp.PROVINCE_CODE is null and tmp.AREA_CODE = T1.AREA_CODE then 1
    when tmp.AREA_CODE = 'CT' then 1
    else 0
   end
)  = 1
WHERE 1       = 1
