WITH list_language_exchange as (
SELECT
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    cat.ITEM_CODE,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE,
    LE.LEE_LOCALE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
  Select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE= 'MR_SUBCATEGORY' and EDITABLE = 1)
  ) cat
  LEFT JOIN  COMMON_GNOC.LANGUAGE_EXCHANGE LE
    ON LE.BUSSINESS_ID = cat.ITEM_ID
    and LE.APPLIED_SYSTEM = 1
    and LE.APPLIED_BUSSINESS = 3
    and LE.LEE_LOCALE = :p_leeLocale
)
SELECT ID id,
  m.MARKET_CODE marketCode,
  m.REGION region,
  m.ARRAY_CODE arrayCode,
    case
    when
    llex.LEE_VALUE is null
    then llex.ITEM_NAME
  else llex.LEE_VALUE end arrayCodeStr,
  m.NETWORK_TYPE networkType,
  m.DEVICE_TYPE deviceType,
  m.CD_ID_HARD cdIdHard,
  m.USER_MR_HARD userMrHard,
  m.STATION_CODE stationCode,
  m.UPDATE_USER updateUser,
  m.UPDATE_DATE updateDate,
  w.WO_GROUP_NAME cdIdHardStr,
  L1.LOCATION_NAME marketCodeStr
FROM MR_HARD_UNIT_CONFIG m
LEFT JOIN WFM.wo_cd_group w
ON m.CD_ID_HARD = w.WO_GROUP_ID
LEFT JOIN COMMON_GNOC.CAT_LOCATION L1
ON m.MARKET_CODE = L1.LOCATION_ID
left join list_language_exchange llex
ON m.ARRAY_CODE = llex.ITEM_CODE
WHERE 1         =1
