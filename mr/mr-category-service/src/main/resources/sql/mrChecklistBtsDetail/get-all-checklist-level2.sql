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
       t1.IMES_ERROR_CODE imesErrorCode,
       t1.IMES_MESSAGE imesMessage,
       t1.TASK_APPROVE_AREA taskApproveArea,
       t1.REASON reason,
       t1.REASON_AREA reasonArea,
       t1.APPROVE_DATE_AREA approveDateArea,
       t1.APPROVE_USER_AREA approveUserArea,
       t1.SCORE_CHECKLIST scoreChecklist,
       t1.IS_IMPORTAINT isImportaint,

--       (select LISTAGG(WO_CODE, ',') WITHIN GROUP (ORDER BY WO_CODE) WO_CODE from MR_SCHEDULE_BTS_HIS_DETAIL chilhis
--       where chilhis.SERIAL = t1.SERIAL and chilhis.CHECKLIST_ID = t1.CHECKLIST_ID and t1.CYCLE = chilhis.CYCLE) woCodeStr
      t1.WO_CODE woCodeStr
FROM MR_SCHEDULE_BTS_HIS_DETAIL t1
JOIN ( select CHECKLIST_ID, SERIAL , MAX(WO_CODE) WO_CODE from MR_SCHEDULE_BTS_HIS_DETAIL  where NVL(WO_CODE_ORIGINAL,WO_CODE) = :wo_ori GROUP BY CHECKLIST_ID, SERIAL) tmp
ON t1.CHECKLIST_ID = tmp.CHECKLIST_ID AND t1.serial = tmp.serial
WHERE tmp.WO_CODE = t1.WO_CODE
 ORDER BY t1.CHECKLIST_ID
