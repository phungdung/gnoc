SELECT t3.returnCodeCategoryId returnCodeCategoryId,
  t3.returnCode returnCode,
  t3.returnTitle returnTitle,
  t3.returnCategory returnCategory,
  t3.description description,
  t3.isActive isActive,
  t3.isEditable isEditable,
  t3.returnCategoryName returnCategoryName
FROM
  (SELECT t1.RCCG_ID returnCodeCategoryId,
    t1.RETURN_CODE returnCode,
    t1.RETURN_TITLE returnTitle,
    t1.RETURN_CATEGORY returnCategory,
    t1.DESCRIPTION description,
    t1.IS_ACTIVE isActive,
    t1.IS_EDITABLE isEditable,
    t2.ITEM_CODE returnCategoryName
  FROM OPEN_PM.RETURN_CODE_CATALOG t1
  LEFT JOIN COMMON_GNOC.CAT_ITEM t2
  ON t2.CATEGORY_ID = 650
  AND t2.ITEM_VALUE = t1.RETURN_CATEGORY
  ) t3
WHERE 1 =1
