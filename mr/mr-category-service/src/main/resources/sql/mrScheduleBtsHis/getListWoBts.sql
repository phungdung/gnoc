SELECT t1.WO_CODE woCode,
  t1.CHECKLIST_ID checkListId,
  t1.CONTENT content ,
  t1.DEVICE_TYPE deviceType,
  t1.SERIAL serial,
  t1.PHOTO_REQ photoReq,
  t1.MIN_PHOTO minPhoto,
  t1.MAX_PHOTO maxPhoto,
  t1.CYCLE cycle,
  t1.CAPTURE_GUIDE captureGuide,
  t1.TASK_STATUS taskStatus,
  t1.SCHEDULE_HIS_DETAIL_ID scheduleBtsHisDetailId,
  t1.TASK_APPROVE taskApprove,
  t1.APPROVE_USER approveUser,
  t1.APPROVE_DATE approveDate,
  t1.WO_CODE_ORIGINAL woCodeOriginal,
  t2.IMES_CHECK imesCheck,
  t1.IMES_ERROR_CODE imesErrorCode,
  t1.IMES_MESSAGE imesMessage
FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_DETAIL t1
JOIN OPEN_PM.MR_CHECKLISTS_BTS t2
ON t1.CHECKLIST_ID = t2.CHECKLIST_ID
WHERE 1            =1
