SELECT
  ITEM_ID itemId,
  ITEM_NAME itemName,
  ITEM_CODE itemCode,
  DESCRIPTION description,
  STATUS status
FROM
  common_gnoc.CAT_ITEM
WHERE
  1 = 1
  and CATEGORY_ID = (Select category_id from common_gnoc.category where category_code= 'OD_PRIORITY' and status = 1)
