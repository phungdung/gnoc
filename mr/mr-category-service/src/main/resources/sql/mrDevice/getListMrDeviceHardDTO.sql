WITH list_language_exchange AS
  (SELECT cat.ITEM_VALUE,
    cat.ITEM_NAME,
    cat.ITEM_CODE,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE,
    LE.LEE_LOCALE
  FROM
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID =
      (SELECT CATEGORY_ID
      FROM COMMON_GNOC.CATEGORY
      WHERE CATEGORY_CODE= 'MR_SUBCATEGORY'
      AND EDITABLE       = 1
      )
    ) cat
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
  ON LE.BUSSINESS_ID       = cat.ITEM_ID
  AND LE.APPLIED_SYSTEM    = 1
  AND LE.APPLIED_BUSSINESS = 3
  AND LE.LEE_LOCALE        = :p_leeLocale
  )
SELECT T1.DEVICE_ID deviceId ,
  T1.MARKET_CODE marketCode,
  L1.LOCATION_NAME marketName,
  T1.ARRAY_CODE arrayCode,
  CASE
    WHEN llex.LEE_VALUE IS NULL
    THEN llex.ITEM_NAME
    ELSE llex.LEE_VALUE
  END arrayCodeStr,
  T1.REGION_SOFT regionSoft,
  T1.REGION_HARD regionHard,
  T1.DEVICE_TYPE deviceType,
  T1.NETWORK_TYPE networkType,
  T1.NODE_CODE nodeCode,
  T1.NODE_IP nodeIp,
  T1.DEVICE_NAME deviceName,
  T1.CD_ID cdId,
  T1.CD_ID_HARD cdIdHard,
  T1.CREATE_USER_SOFT createUserSoft,
  T1.CREATE_USER_HARD createUserHard,
  T1.CREATE_MR createMr,
  T1.IMPACT_NODE impactNode,
  T1.NUMBER_OF_CR numberOfCr,
  T1.MR_HARD mrHard,
  T1.MR_12M mr12M,
  T1.COMMENTS comments,
  T1.MR_SOFT mrSoft,
  T1.LAST_DATE lastDate,
  T1.GROUP_CODE groupCode,
  T1.UPDATE_DATE updateDate,
  T1.UPDATE_USER updateUser,
  T1.STATION_CODE stationCode,
  T1.IS_COMPLETE_1M isComplete1m,
  T1.IS_COMPLETE_3M isComplete3m,
  T1.IS_COMPLETE_6M isComplete6m,
  T1.IS_COMPLETE_12M isComplete12m,
  T1.IS_COMPLETE_SOFT isCompleteSoft,
  T1.LAST_DATE_1M lastDate1m,
  T1.LAST_DATE_3M lastDate3m,
  T1.LAST_DATE_6M lastDate6m,
  T1.LAST_DATE_12M lastDate12m,
  T1.GROUP_ID groupId,
  T1.VENDOR vendor,
  T1.NETWORK_CLASS networkClass,
  T1.MR_CONFIRM_HARD mrConfirmHard,
   c.CONFIG_NAME mrConfirmHardStr,
  T1.MR_CONFIRM_SOFT mrConfirmSoft,
  T1.STATUS status ,
  T1.DATE_INTEGRATED dateIntegrated,
  T1.USER_MR_HARD userMrhard,
  T1.BO_UNIT_HARD boUnitHard,
  CASE
    WHEN T4.unit_code IS NULL
    THEN ''
    ELSE TO_CHAR(T4.unit_name
      || ' ('
      || T4.unit_code
      || ')')
  END AS boUnitHardName,
  T1.APPROVE_STATUS_HARD approveStatusHard,
  T1.APPROVE_REASON_HARD approveReasonHard,
  g.CD_ID_HARD cdIdHardConfig,
  g.WO_GROUP_NAME cdIdHardConfigStr
FROM MR_DEVICE T1
LEFT JOIN COMMON_GNOC.CAT_LOCATION L1
ON T1.MARKET_CODE = L1.LOCATION_ID
LEFT JOIN list_language_exchange llex
ON T1.ARRAY_CODE = llex.ITEM_CODE
LEFT Join OPEN_PM.MR_CONFIG c
On T1.MR_CONFIRM_HARD = c.CONFIG_CODE and c.CONFIG_GROUP = 'LY_DO_KO_BD'
LEFT JOIN COMMON_GNOC.UNIT T4
ON T1.BO_UNIT_HARD = T4.UNIT_ID
LEFT JOIN
  (SELECT ID,
    m.MARKET_CODE,
    m.REGION,
    m.ARRAY_CODE,
    m.NETWORK_TYPE,
    m.DEVICE_TYPE,
    m.CD_ID_HARD,
    m.STATION_CODE,
    w.WO_GROUP_NAME
  FROM MR_HARD_GROUP_CONFIG m
  LEFT JOIN WFM.wo_cd_group w
  ON m.CD_ID_HARD       = w.WO_GROUP_ID
  ) g ON T1.MARKET_CODE = g.MARKET_CODE
AND T1.REGION_HARD      = g.REGION
AND T1.ARRAY_CODE       = g.ARRAY_CODE
AND T1.NETWORK_TYPE     = g.NETWORK_TYPE
AND T1.DEVICE_TYPE      = g.DEVICE_TYPE
AND T1.STATION_CODE     = g.STATION_CODE
WHERE 1          = 1
