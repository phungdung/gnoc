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
  TO_CHAR(t1.APPROVE_DATE,'dd/MM/yyyy HH24:mi:ss') approveDate,
  t1.WO_CODE_ORIGINAL woCodeOriginal,
  t1.IMES_ERROR_CODE imesErrorCode,
  t1.IMES_MESSAGE imesMessage,
  t1.REASON reason,
  t1.REASON_AREA reasonArea,
  t1.TASK_APPROVE_AREA taskApproveArea,
  t1.APPROVE_USER_AREA approveUserArea,
  TO_CHAR(t1.APPROVE_DATE_AREA,'dd/MM/yyyy HH24:mi:ss') approveDateArea,
  u1.MOBILE mobile,
  u2.MOBILE mobileArea
  FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_DETAIL t1
  LEFT JOIN COMMON_GNOC.USERS u1
  ON t1.APPROVE_USER = u1.USERNAME
  LEFT JOIN COMMON_GNOC.USERS u2
  ON t1.APPROVE_USER_AREA = u2.USERNAME
  WHERE 1 =1
