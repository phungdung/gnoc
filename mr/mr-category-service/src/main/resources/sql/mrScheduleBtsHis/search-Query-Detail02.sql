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
)
SELECT DISTINCT DT.WO_CODE woCode,
       DT.CHECKLIST_ID checkListId,
       DT.CONTENT content ,
       DT.DEVICE_TYPE deviceType,
       DT.SERIAL serial,
       DT.PHOTO_REQ photoReq,
       DT.MIN_PHOTO minPhoto,
       DT.MAX_PHOTO maxPhoto,
       DT.CYCLE cycle,
       DT.CAPTURE_GUIDE captureGuide,
       DT.TASK_STATUS taskStatus,
       DT.SCHEDULE_HIS_DETAIL_ID scheduleBtsHisDetailId,
       DT.TASK_APPROVE taskApprove,
       DT.APPROVE_USER approveUser,
       DT.APPROVE_DATE approveDate,
       DT.WO_CODE_ORIGINAL woCodeOriginal,
       DT.IMES_ERROR_CODE imesErrorCode,
       DT.IMES_MESSAGE imesMessage,
       DT.TASK_APPROVE_AREA taskApproveArea,
       DT.REASON reason,
       DT.REASON_AREA reasonArea

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
JOIN open_pm.MR_SCHEDULE_BTS_HIS_DETAIL DT
ON T1.SERIAL = DT.SERIAL
WHERE 1       = 1
