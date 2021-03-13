WITH
  list_language_exchange AS (
  SELECT
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
      Select CATEGORY_ID from COMMON_GNOC.CATEGORY
      where CATEGORY_CODE= 'WO_CD_GROUP_TYPE' and EDITABLE = 1)
      ) cat
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID = cat.ITEM_ID
  AND LE.LEE_LOCALE = :p_leeLocale
  and LE.APPLIED_SYSTEM = 1
  and LE.APPLIED_BUSSINESS = 3
  )
SELECT  g.WO_GROUP_ID woGroupId,
   g.WO_GROUP_CODE woGroupCode,
   g.WO_GROUP_NAME woGroupName,
   g.EMAIL email,
   g.MOBILE mobile,
   g.GROUP_TYPE_ID groupTypeId,
   case
     when llex.LEE_VALUE is null
     then llex.ITEM_NAME
   else llex.LEE_VALUE end groupTypeName,
   g.IS_ENABLE isEnable,
   g.NATION_ID nationId,
   ci2.ITEM_NAME nationName
FROM    WFM.WO_CD_GROUP g
   left join list_language_exchange llex ON to_char(g.GROUP_TYPE_ID) = llex.ITEM_VALUE
   LEFT JOIN COMMON_GNOC.CAT_ITEM ci2 ON g.NATION_ID = ci2.ITEM_ID
WHERE   1 = 1
