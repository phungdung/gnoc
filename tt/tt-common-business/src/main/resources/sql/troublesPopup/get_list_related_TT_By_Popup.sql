/*SELECT
a.TROUBLE_CODE troubleCode,
a.TROUBLE_NAME troubleName,
 a.RECEIVE_UNIT_NAME receiveUnitName,
 a.CREATED_TIME createdTime,
 a.STATE state,
 a.TROUBLE_ID troubleId
 FROM ONE_TM.TROUBLES a
 WHERE a.TROUBLE_CODE =:troubleCode OR a.TROUBLE_NAME =:troubleName*/


SELECT
a.TROUBLE_CODE troubleCode,
a.TROUBLE_NAME troubleName,
 a.RECEIVE_UNIT_NAME receiveUnitName,
 a.CREATED_TIME createdTime,
 a.STATE state,
 a.TROUBLE_ID troubleId,
 s.ITEM_NAME stateName
 FROM ONE_TM.TROUBLES a, COMMON_GNOC.CAT_ITEM s
 WHERE a.STATE = s.ITEM_ID
