
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
 {createDateWo}
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
 {createDateWo}
  ) wo_giao_lai
ON detail.WO_CODE = wo_giao_lai.WO_CODE
JOIN open_pm.MR_SCHEDULE_BTS_HIS_DETAIL DT
ON T1.SERIAL = DT.SERIAL
WHERE 1       = 1
