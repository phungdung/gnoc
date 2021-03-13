WITH lang_exchange_risk_type AS (
  SELECT  rt.RISK_TYPE_ID,
          CASE
            WHEN le.LEE_VALUE IS NULL THEN rt.RISK_TYPE_NAME
            ELSE le.LEE_VALUE
          END RISK_TYPE_NAME
  FROM    RISK_TYPE rt
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le ON rt.RISK_TYPE_ID = le.BUSSINESS_ID
                                                      AND le.APPLIED_SYSTEM = :systemRiskType
                                                      AND le.APPLIED_BUSSINESS = :bussinessRiskType
                                                      AND le.LEE_LOCALE = :leeLocale)
SELECT  t.RISK_TYPE_ID riskTypeId,
        t.RISK_TYPE_CODE riskTypeCode,
        lt.RISK_TYPE_NAME riskTypeName,
        t.STATUS status,
        CASE t.STATUS
          WHEN 0 THEN :inactive
          WHEN 1 THEN :active
        END statusName,
        t.RISK_GROUP_TYPE_ID riskGroupTypeId,
        CASE
          WHEN le2.LEE_VALUE IS NULL THEN ci.ITEM_NAME
          ELSE le2.LEE_VALUE
        END riskGroupTypeName
FROM    RISK_TYPE t
        LEFT JOIN lang_exchange_risk_type lt ON t.RISK_TYPE_ID = lt.RISK_TYPE_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci ON t.RISK_GROUP_TYPE_ID = ci.ITEM_ID
        LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.RISK_GROUP_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = :systemGroupType
                                                      AND le2.APPLIED_BUSSINESS = :bussinessGroupType
                                                      AND le2.LEE_LOCALE = :leeLocale
WHERE   t.RISK_TYPE_ID = :riskTypeId
