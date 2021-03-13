SELECT t1.ID_SCHEDULE idSchedule,
  t1.CR_ID idCr,
  t1.ID id,
  t1.CR_NUMBER codeCR,
  t1.IMPACT_SEGMENT crArray,
  t1.IMPACT_SEGMENT_CHILD crChildren,
  t2.CHILDREN_NAME crChildrenName,
  t1.DEADLINE crDeadline,
  t1.CR_LEVEL crLevel,
  t1.EXECUTION_TIME executionTime,
  t1.PRIORITY crPrioritize,
  t1.REGISTRATION_DATE registrationDate,
  t1.STATUS status,
  t1.START_DATE startDate,
  t1.TYPE type,
  t1.END_DATE endDate,
  t1.CR_PERFORMER crPerformer,
  t1.FORBIDDEN_DATE forbiddenDate,
  t1.IMPACT_NODE_LIST impactNodeList,
  t1.AFFECT_SERVICE_LIST affectServiceList
FROM COMMON_GNOC.SCHEDULE_CR t1
left join COMMON_GNOC.CFG_CHILD_ARRAY t2
on t1.IMPACT_SEGMENT_CHILD = t2.CHILDREN_ID and t2.STATUS = 1
WHERE 1=1
