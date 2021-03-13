WITH list_language_exchange AS (
SELECT  p.PRIORITY_ID,
        p.WO_TYPE_ID,
        p.PRIORITY_NAME,
        p.PRIORITY_CODE,
        p.IS_ENABLE,
        le.LEE_VALUE,
        le.LEE_LOCALE
FROM    WO_PRIORITY p
        LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le ON p.PRIORITY_ID = le.BUSSINESS_ID
                                                    AND le.APPLIED_SYSTEM = 4
                                                    AND le.APPLIED_BUSSINESS = 3
                                                    AND le.LEE_LOCALE = :leeLocale)
SELECT
		lle.PRIORITY_ID priorityId,
    lle.WO_TYPE_ID woTypeId,
    case
      when
      lle.LEE_VALUE is null
      then lle.PRIORITY_NAME
      else to_char(lle.LEE_VALUE) end priorityName,
    lle.PRIORITY_CODE priorityCode,
    lle.IS_ENABLE isEnable
FROM list_language_exchange lle
WHERE 1 = 1
