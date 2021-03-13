

WITH lang_exchange_array AS (
  SELECT  ci.ITEM_ID, ci.ITEM_NAME,
          CASE
            WHEN le.LEE_VALUE IS NULL THEN ci.ITEM_NAME
            ELSE le.LEE_VALUE
          END ARRAY_CODE
  FROM    COMMON_GNOC.CAT_ITEM ci
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le ON ci.ITEM_ID = le.BUSSINESS_ID AND le.APPLIED_SYSTEM = :systemArray AND le.APPLIED_BUSSINESS = :bussinessArray AND le.LEE_LOCALE = :leeLocale
  WHERE   STATUS = 1
  AND     PARENT_ITEM_ID IS NULL
  AND     CATEGORY_ID = (SELECT CATEGORY_ID FROM COMMON_GNOC.CATEGORY WHERE CATEGORY_CODE = :categoryCode))
SELECT
       T1.CFG_ID cfgId
      ,T1.MARKET_CODE marketCode
      ,T1.ARRAY_CODE arrayCode
      ,T1.DEVICE_TYPE_ID deviceTypeId
      ,T1.IMPLEMENT_UNIT implementUnit
      ,T1.CHECKING_UNIT checkingUnit
      ,T1.CREATE_USER createUser
      ,T1.APPROVE_USER_LEVEL1 approveUserLv1
      ,T1.APPROVE_USER_LEVEL2 approveUserLv2
      ,T1.CREATE_WO isCreateWO
      ,T1.MANAGE_UNIT manageUnit
      ,T1.REGION region
       FROM MR_CFG_CR_IMPL_UNIT T1
        LEFT JOIN lang_exchange_array lea ON t1.ARRAY_CODE = lea.ITEM_NAME
       WHERE 1 = 1



