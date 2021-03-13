SELECT oct.id id, oct.OD_TYPE_ID odTypeId, oct.OLD_STATUS oldStatus,
 oct.NEW_STATUS newStatus, oct.SEND_CREATE sendCreate, oct.CREATE_CONTENT createContent,
 oct.SEND_RECEIVE_USER sendReceiveUser, oct.RECEIVE_USER_CONTENT receiveUserContent,
 oct.IS_DEFAULT isDefault, oct.SEND_RECEIVE_UNIT sendReceiveUnit, oct.RECEIVE_UNIT_CONTENT receiveUnitContent,
 oct.OD_PRIORITY odPriority, oct.NEXT_ACTION nextAction, oct.SEND_APPROVER sendApprover, oct.APPROVER_CONTENT approverContent
 FROM OD_CHANGE_STATUS oct
 WHERE 1 = 1
