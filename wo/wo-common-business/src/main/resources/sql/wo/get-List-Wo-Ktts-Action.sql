WITH list_language_exchange AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  WHERE LE.APPLIED_SYSTEM  = 1
  AND LE.APPLIED_BUSSINESS = 3
  AND LE.LEE_LOCALE        = :p_leeLocale
  ),
  list_data AS
  (SELECT ci.item_id itemId,
    ci.item_name itemName,
    ci.item_value itemValue,
    ci.parent_item_id parentItemId
  FROM COMMON_GNOC.CAT_ITEM ci
  WHERE ci.STATUS    = 1
  AND ci.CATEGORY_ID =
    (SELECT c.CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY c
    WHERE c.CATEGORY_CODE = 'WO_KTTS_ACTION'
    )
  )
SELECT ld.itemId,
  ld.itemValue,
  CASE
    WHEN llx.LEE_VALUE IS NULL
    THEN ld.itemName
    ELSE llx.LEE_VALUE
  END itemName,
  ld.parentItemId
FROM list_data ld
LEFT JOIN list_language_exchange llx
ON ld.itemId = llx.BUSSINESS_ID
WHERE 1      = 1
