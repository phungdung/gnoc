select oct.id id, oct.OD_TYPE_ID odTypeId, oct.OLD_STATUS oldStatus, oct.SEND_APPROVER sendApprover, oct.APPROVER_CONTENT approverContent,
oct.NEW_STATUS newStatus, oct.SEND_CREATE sendCreate, oct.CREATE_CONTENT createContent, oct.SEND_RECEIVE_USER sendReceiveUser, oct.RECEIVE_USER_CONTENT receiveUserContent,
oct.IS_DEFAULT isDefault, oct.SEND_RECEIVE_UNIT sendReceiveUnit, oct.RECEIVE_UNIT_CONTENT receiveUnitContent, oct.OD_PRIORITY odPriority, oct.NEXT_ACTION nextAction, od.OD_TYPE_NAME odTypeName
from OD_CHANGE_STATUS oct , OD_TYPE od
where id = :id AND oct.OD_TYPE_ID = od.OD_TYPE_ID(+)
