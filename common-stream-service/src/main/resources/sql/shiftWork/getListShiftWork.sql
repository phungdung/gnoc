SELECT
t1.ID id,
t1.WORK_NAME workName,
t1.DESCRIPTION description,
t1.PROCESS process,
t1.REASON_EXIST reasonExist,
t1.CONTACT contact,
t1.OPINION opinion,
t1.COUNTRY country,
t1.SHIFT_HANDOVER_ID shiftHandoverId,
t1.DEADLINE deadline,
t1.START_TIME startTime,
t1.OWNER owner,
t1.HANDLE handle,
t1.IMPORTANT_LEVEL importantLevel,
t1.RESULT result,
t1.NEXT_WORK nextWork,
t1.WORK_STATUS workStatus
FROM COMMON_GNOC.SHIFT_WORK t1 WHERE 1 = 1
