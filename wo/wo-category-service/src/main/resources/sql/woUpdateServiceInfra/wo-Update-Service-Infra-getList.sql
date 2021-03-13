WITH
  list_language_exchange AS (
  SELECT
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
    Select CATEGORY_ID from COMMON_GNOC.CATEGORY
    where CATEGORY_CODE= 'WO_TECHNOLOGY_CODE' and EDITABLE = 1)
    ) cat
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID = cat.ITEM_ID
    AND LE.LEE_LOCALE = :p_leeLocale
    and LE.APPLIED_SYSTEM = 1
    and LE.APPLIED_BUSSINESS = 3
  ),
  list_catitem_language_exchange AS (
  SELECT
    cat1.ITEM_VALUE,
    cat1.ITEM_NAME,
    LE1.BUSSINESS_ID,
    LE1.LEE_VALUE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
    Select CATEGORY_ID from COMMON_GNOC.CATEGORY
    where CATEGORY_CODE= 'WO_STATUS')
    ) cat1
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE1
  ON LE1.BUSSINESS_ID = cat1.ITEM_ID
    AND LE1.LEE_LOCALE = :p_leeLocale
    and LE1.APPLIED_SYSTEM = 1
    and LE1.APPLIED_BUSSINESS = 3
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
  a.WO_ID woId,
  a.WO_CODE woCode,
  a.status status,
  CASE
  WHEN lcle.LEE_VALUE is null
	then to_char(lcle.ITEM_NAME)
  when lcle.ITEM_VALUE = '2'
  then (case when a.ft_id is not null then to_char(lcle.LEE_VALUE) else :rejectCd end)
  when lcle.ITEM_VALUE = '6'
  then (case when a.result is null then to_char(lcle.LEE_VALUE) else :closedCd end)
  ELSE to_char(lcle.LEE_VALUE) END statusName,
  b.ACCOUNT_ISDN accountIsdn,
  b.INFRA_TYPE infraType,
  b.SERVICE_ID serviceId,
  case
    when ls.LEE_VALUE is null
    then to_char(ls.SERVICE_NAME)
    else to_char(ls.LEE_VALUE) end serviceName,
  case
    when llex.LEE_VALUE is null
    then to_char(llex.ITEM_NAME)
  else to_char(llex.LEE_VALUE) end infraTypeName
FROM
  WFM.WO a
  left join WFM.WO_DETAIL b on a.WO_ID = b.WO_ID
  left join list_language_exchange_service ls ON b.service_id = ls.SERVICE_ID
  left join list_language_exchange llex ON to_char(b.infra_type) = llex.ITEM_VALUE
  left join list_catitem_language_exchange lcle ON a.STATUS = lcle.ITEM_VALUE
WHERE
    a.create_date > sysdate - 30
AND a.WO_SYSTEM = 'SPM'
AND 1=1
