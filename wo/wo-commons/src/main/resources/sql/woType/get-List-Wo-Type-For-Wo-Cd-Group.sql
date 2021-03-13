WITH lang_exchange_wo_type AS (
  SELECT  t.WO_TYPE_ID,
          t.WO_TYPE_CODE,
          t.WO_TYPE_NAME,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    WO_TYPE t
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = 4
                                                      AND le2.APPLIED_BUSSINESS = 1
                                                      AND le2.LEE_LOCALE = :leeLocale)
SELECT  t.WO_TYPE_ID woTypeId,
        case
          when
          lewt.LEE_VALUE is null
          then to_char(lewt.WO_TYPE_NAME)
          else to_char(lewt.LEE_VALUE) end woTypeName,
        t.WO_TYPE_CODE woTypeCode
FROM    WO_TYPE t,
        lang_exchange_wo_type lewt
WHERE   1 = 1
and     t.wo_type_id = lewt.wo_type_id
