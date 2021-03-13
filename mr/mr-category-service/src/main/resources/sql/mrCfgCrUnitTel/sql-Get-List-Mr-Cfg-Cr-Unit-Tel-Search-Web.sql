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
SELECT  t.CFG_ID cfgId,
        t.MARKET_CODE marketCode,
        t.ARRAY_CODE arrayCode,
        lea.ARRAY_CODE arrayName,
        t.DEVICE_TYPE deviceType,
        t.IMPLEMENT_UNIT implementUnit,
        t.CHECKING_UNIT checkingUnit,
        t.CREATED_USER createdUser,
        t.CREATED_TIME createdTime,
        t.UPATED_USER updatedUser,
        t.UPDATED_TIME updatedTime,
        t.REGION region,
        t.CREATED_DATE createdDate,
        t.NETWORK_TYPE networkType
FROM    MR_CFG_CR_UNIT_TEL t
        LEFT JOIN lang_exchange_array lea ON t.ARRAY_CODE = lea.ITEM_NAME
WHERE   1 = 1
