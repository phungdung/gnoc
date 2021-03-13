select re.SYSTEM, re.SYSTEM_CODE systemCode, re.SYSTEM_ID systemId, re.CREATE_TIME createTime, re.END_TIME endTime, re.CONTENT, us.USERNAME createPersonName, u.UNIT_NAME receiveUnitName
from wfm.OD_RELATION re, common_gnoc.unit u, COMMON_GNOC.users us, wfm.Od od
where
re.CREATE_PERSON_ID = us.User_ID(+)
AND  re.RECEIVE_UNIT_ID = u.unit_id(+)
AND re.OD_ID = od.OD_ID(+)
AND re.OD_ID = :odId
