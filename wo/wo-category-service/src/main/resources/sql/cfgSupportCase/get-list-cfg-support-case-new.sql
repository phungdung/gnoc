WITH
  list_language_exchange AS (
  SELECT
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
      Select CATEGORY_ID from COMMON_GNOC.CATEGORY
      where CATEGORY_CODE= 'WO_TECHNOLOGY_CODE' and EDITABLE = 1)) cat
  LEFT JOIN  COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID = cat.ITEM_ID
  AND LE.LEE_LOCALE = :p_leeLocale
  and LE.APPLIED_SYSTEM = 1
  and LE.APPLIED_BUSSINESS = 3
  ),
list_language_exchange_service As (
  SELECT
    cs.SERVICE_ID,
    cs.SERVICE_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM COMMON_GNOC.CAT_SERVICE cs
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID = cs.SERVICE_ID
  AND LE.LEE_LOCALE = :p_leeLocale
  and LE.APPLIED_SYSTEM = 1
  and LE.APPLIED_BUSSINESS = 8
  )
SELECT
  a.ID id,
  a.ID cfgSupportCaseID,
  a.CASE_NAME caseName,
  a.SERVICE_ID serviceID,
  case
    when ls.LEE_VALUE is null
    then ls.SERVICE_NAME
    else to_char(ls.LEE_VALUE) end serviceName,
  a.INFRA_TYPE_ID infraTypeID,
  case
    when llex.LEE_VALUE is null
    then llex.ITEM_NAME
    else llex.LEE_VALUE end infraTypeName
FROM
  CFG_SUPPORT_CASE a
	left join common_gnoc.cat_service e on a.service_id = e.service_id
  left join list_language_exchange llex ON to_char(a.INFRA_TYPE_ID) = llex.ITEM_VALUE
  left join list_language_exchange_service ls ON a.service_id = ls.SERVICE_ID
where
  1=1
