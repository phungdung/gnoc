SELECT ci.ITEM_ID columnId,
  ci.ITEM_NAME columnNameValue,
  ci.item_value columnName,
  ci.description keyCode,
  NVL(bu.EDITABLE,0) editable,
  NVL(bu.IS_REQUIRED,0) isRequired,
  NVL(bu.IS_VISIBLE,0) isVisible,
  bu.DEFAULT_VALUE defaultValue,
  bu.TYPE_CONTROL typeControl,
  NVL(bu.IS_PARENT,0) isParent,
  NVL(bu.SCOPE_OF_USE,1) scopeOfUse,
  bu.col_name_relate colNameRelate,
  bu.STT stt
FROM COMMON_GNOC.CAT_ITEM ci,
  COMMON_GNOC.CATEGORY ca,
  (SELECT *
  FROM ONE_TM.TT_CFG_BUSINESS
  WHERE TT_CHANGE_STATUS_ID = :changeStatusId
  ) bu
WHERE ci.CATEGORY_ID = ca.CATEGORY_ID
AND ci.ITEM_VALUE    = bu.COLUMN_NAME(+)
AND ca.CATEGORY_CODE = :cfgBusinessColumn
