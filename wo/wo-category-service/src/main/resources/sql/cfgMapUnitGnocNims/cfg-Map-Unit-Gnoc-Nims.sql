WITH
  list_language_exchange AS (
  SELECT
    cat.ITEM_ID,
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
    Select CATEGORY_ID from COMMON_GNOC.CATEGORY
    where CATEGORY_CODE= 'CFG_MAP_GNOC_NIMS_BUSINESS' and EDITABLE = 1)
    ) cat
LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
ON LE.BUSSINESS_ID = cat.ITEM_ID
  and LE.LEE_LOCALE = :p_leeLocale
  and LE.APPLIED_SYSTEM = 1
  and LE.APPLIED_BUSSINESS = 3
  )
SELECT m.ID id
, m.UNIT_GNOC_CODE unitGnocCode
, m.UNIT_NIMS_CODE unitNimsCode
, m.BUSINESS_CODE businessCode
,case
    when llex.LEE_VALUE is null
    then llex.ITEM_NAME
    else llex.LEE_VALUE end businessName
 FROM WFM.CFG_MAP_UNIT_GNOC_NIMS m
 left join list_language_exchange llex ON m.BUSINESS_CODE = llex.ITEM_ID
WHERE 1=1
