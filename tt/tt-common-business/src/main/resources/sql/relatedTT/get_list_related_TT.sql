SELECT
a.TROUBLE_CODE troubleCode,
a.TROUBLE_NAME troubleName,
 a.RECEIVE_UNIT_NAME receiveUnitName,
 a.CREATED_TIME createdTime,
 a.STATE state,
 s.ITEM_NAME stateName,
 a.TROUBLE_ID troubleId,
 a.RELATED_TT
 FROM ONE_TM.TROUBLES a, COMMON_GNOC.CAT_ITEM s WHERE a.STATE = s.ITEM_ID AND a.TROUBLE_CODE =:troubleCode
