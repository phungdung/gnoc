SELECT c.ITEM_ID itemId,
  c.ITEM_CODE itemCode,
  c.ITEM_NAME itemName,
  c.ITEM_VALUE itemValue,
  c.DESCRIPTION description,
  c.STATUS status,
  c.EDITABLE editable,
  c.PARENT_ITEM_ID parentItemId,
  c.POSITION position,
  c.UPDATE_TIME updateTime,
  ca.CATEGORY_ID categoryId,
  ca.CATEGORY_NAME categoryName,
  c1.ITEM_NAME parentItemName
FROM CAT_ITEM c
inner JOIN CATEGORY ca
ON c.CATEGORY_ID   = ca.CATEGORY_ID
left JOIN CAT_ITEM c1
ON c.PARENT_ITEM_ID = c1.ITEM_ID
WHERE 1            = 1
