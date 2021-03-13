SELECT
a.TROUBLE_CODE troubleCode,
a.TROUBLE_NAME troubleName,
 a.RECEIVE_UNIT_NAME receiveUnitName,
 a.CREATED_TIME createdTime,
 a.STATE state,
 a.TROUBLE_ID troubleId
   FROM ONE_TM.TROUBLES a WHERE a.TROUBLE_ID
