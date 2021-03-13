SELECT t1.ID_SCHEDULE idSchedule,
  t1.UNIT_ID unitId,
  t1.STATUS status,
  t1.START_DATE startDate,
  t1.END_DATE endDate,
  t1.CREATED_USER createdUser,
  t1.CREATED_DATE createdDate,
  t1.TYPE type,
  t1.DETAIL_SCHEDULE detail,
  t1.WORK_TIME workTime,
  t1.COMPLICATE_WORK complicateWork,
  t1.CR_SAME_NODE sameNode,
  t1.CR_SAME_SERVICE sameService,
  t1.CR_SAME_NODE_SHIFT sameNodeShift,
  t1.CR_SAME_SERVICE_SHIFT sameServiceShift
FROM REQUEST_SCHEDULE t1
WHERE 1 = 1
