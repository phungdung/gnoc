 WITH 
list_language_exchange AS (
  SELECT
    cat.ITEM_VALUE,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  JOIN (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
  Select CATEGORY_ID from COMMON_GNOC.CATEGORY
  where CATEGORY_CODE= 'WO_PRIORITY_CODE' and EDITABLE = 1)) cat
    ON LE.BUSSINESS_ID = cat.ITEM_ID 
  WHERE LE.LEE_LOCALE = :p_leeLocale and LE.APPLIED_SYSTEM = 1 and LE.APPLIED_BUSSINESS = 3)
  SELECT
		p.PRIORITY_ID priorityId
	, p.WO_TYPE_ID woTypeId,
  case
      when 
      llex.LEE_VALUE is null
      then p.PRIORITY_NAME
      else to_char(llex.LEE_VALUE) end priorityName,
	 p.PRIORITY_CODE priorityCode,
	 p.IS_ENABLE isEnable
	FROM WFM.WO_PRIORITY p 
  LEFT JOIN list_language_exchange llex
  ON p.PRIORITY_CODE = llex.ITEM_VALUE
  Where 1 =1
