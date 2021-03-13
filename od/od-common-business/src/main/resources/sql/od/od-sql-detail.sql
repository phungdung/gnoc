select o.od_id odId, o.OD_CODE odCode, o.status oldStatus, o.OD_TYPE_ID odTypeId, o.INSERT_SOURCE insertSource,
o.PRIORITY_ID priorityId, o.PLAN_CODE planCode, o.START_TIME startTime, o.END_TIME endTime,
o.CREATE_PERSON_ID createPersonId, o.CREATE_TIME createTime, o.RECEIVE_UNIT_ID receiveUnitId, o.RECEIVE_USER_ID receiveUserId,
o.CLEAR_CODE_ID, o.CLOSE_CODE_ID, o.END_PENDING_TIME, o.OD_NAME, o.DESCRIPTION, o.VOFFICE_TRANS_CODE vofficeTransCode,
us.USERNAME createPersonName, ut.UNIT_NAME || '(' || ut.UNIT_CODE || ')' receiveUnitName, us2.USERNAME receiveUserName,
o.SOLUTION_GROUP solutionGroup, o.REASON_GROUP reasonGroup, o.REASON_DETAIL reasonDetail, o.SOLUTION_DETAIL solutionDetail,
o.SOLUTION_COMPLETE_TIME solutionCompleteTime, o.CREATE_UNIT_ID createUnitId, o.APPROVER_ID approverId, to_char(o.FINISHED_TIME, 'dd/MM/yyyy HH24:mi:ss') finishedTime
, o.REASON_PAUSE
from wfm.od o, COMMON_GNOC.USERS us, COMMON_GNOC.UNIT ut, COMMON_GNOC.USERS us2
where o.od_id = :odId
and o.CREATE_PERSON_ID = us.USER_ID(+)
and o.RECEIVE_UNIT_ID = ut.unit_id(+)
and o.RECEIVE_USER_ID = us2.USER_ID(+)
