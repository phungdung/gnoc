 SELECT ci.ITEM_ID columnId, ci.ITEM_NAME columnNameValue, ci.ITEM_VALUE columnName, bu.EDITABLE editable, bu.IS_REQUIRED isRequired, bu.IS_VISIBLE isVisible, bu.MESSAGE message
 FROM COMMON_GNOC.CAT_ITEM ci, COMMON_GNOC.CATEGORY ca,
(SELECT * FROM wfm.OD_CFG_BUSINESS WHERE OD_CHANGE_STATUS_ID = :changeStatusId) bu
 WHERE
 ci.CATEGORY_ID = ca.CATEGORY_ID AND ci.ITEM_VALUE = bu.COLUMN_NAME(+)
 AND ca.CATEGORY_CODE = :cfgBusinessColumn
 ORDER BY ci.Item_id ASC
