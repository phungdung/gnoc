WITH lang_exchange_array AS (
  SELECT  ci.ITEM_ID, ci.ITEM_NAME,
          CASE
            WHEN le.LEE_VALUE IS NULL THEN ci.ITEM_NAME
            ELSE le.LEE_VALUE
          END ARRAY_CODE
  FROM    COMMON_GNOC.CAT_ITEM ci
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le ON ci.ITEM_ID = le.BUSSINESS_ID
                                                      AND le.APPLIED_SYSTEM = :systemArray
                                                      AND le.APPLIED_BUSSINESS = :bussinessArray
                                                      AND le.LEE_LOCALE = :leeLocale
  WHERE   STATUS = 1
  AND     PARENT_ITEM_ID IS NULL
  AND     CATEGORY_ID = (SELECT CATEGORY_ID FROM COMMON_GNOC.CATEGORY WHERE CATEGORY_CODE = :categoryCode))
SELECT  d.DEVICE_ID deviceId,
        d.MARKET_CODE marketCode,
        d.REGION_SOFT regionSoft,
        d.REGION_HARD regionHard,
        d.NODE_CODE nodeCode,
        d.NODE_IP nodeIp,
        cfg.IMPLEMENT_UNIT implementUnit,
        cfg.CHECKING_UNIT checkingUnit,
        d.CD_ID cdId,
        d.CD_ID_HARD cdIdHard,
        d.CREATE_USER_SOFT createUserSoft,
        d.CREATE_USER_HARD createUserHard,
        d.CREATE_MR createMr,
        d.IMPACT_NODE impactNode,
        d.NUMBER_OF_CR numberOfCr,
        d.MR_HARD mrHard,
        d.MR_12M mr12M,
        d.COMMENTS comments,
        d.MR_SOFT mrSoft,
        d.LAST_DATE lastDate,
        d.ARRAY_CODE arrayCode,
        lea.ARRAY_CODE arrayName,
        d.DEVICE_TYPE deviceType,
        d.GROUP_CODE groupCode,
        d.UPDATE_DATE updateDate,
        d.UPDATE_USER updateUser,
        d.DEVICE_NAME deviceName,
        d.USER_MR_HARD userMrHard,
        d.STATION_CODE stationCode,
        d.IS_COMPLETE_1M isComplete1m,
        d.IS_COMPLETE_3M isComplete3m,
        d.IS_COMPLETE_6M isComplete6m,
        d.IS_COMPLETE_12M isComplete12m,
        d.IS_COMPLETE_SOFT isCompleteSoft,
        d.LAST_DATE_1M lastDate1m,
        d.LAST_DATE_3M lastDate3m,
        d.LAST_DATE_6M lastDate6m,
        d.LAST_DATE_12M lastDate12m,
        d.GROUP_ID groupId,
        d.VENDOR vendor,
        d.NETWORK_TYPE networkType,
        d.NETWORK_CLASS networkClass,
        d.MR_CONFIRM_HARD mrConfirmHard,
        d.MR_CONFIRM_SOFT mrConfirmSoft,
        d.STATUS status,
        d.DATE_INTEGRATED dateIntegrated,
        TO_CHAR(d.DATE_INTEGRATED, 'dd/MM/yyyy') dateIntegratedStr,
        d.BO_UNIT_SOFT boUnitSoft,
        d.APPROVE_STATUS_SOFT approveStatusSoft,
        d.APPROVE_REASON_SOFT approveReasonSoft
FROM    MR_DEVICE d
        LEFT JOIN lang_exchange_array lea ON d.ARRAY_CODE = lea.ITEM_NAME
        LEFT JOIN MR_SCHEDULE_TEL st ON d.DEVICE_ID = st.DEVICE_ID
        LEFT JOIN MR_CFG_CR_UNIT_TEL cfg ON d.MARKET_CODE = cfg.MARKET_CODE
                                          AND d.ARRAY_CODE = cfg.ARRAY_CODE
                                          AND d.NETWORK_TYPE = cfg.NETWORK_TYPE
                                          AND d.REGION_SOFT = cfg.REGION
                                          AND d.DEVICE_TYPE = cfg.DEVICE_TYPE
WHERE   d.DEVICE_ID = :deviceId
