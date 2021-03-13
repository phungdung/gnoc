WITH lang_exchange_wo_type AS (
  SELECT  t.WO_TYPE_ID,
          t.WO_TYPE_CODE,
          t.WO_TYPE_NAME,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    WFM.WO_TYPE t
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = 4
                                                      AND le2.APPLIED_BUSSINESS = 1
                                                      AND le2.LEE_LOCALE = :leeLocale)
select  w.wo_type_id  woTypeId,
        w.wo_type_code woTypeCode,
         a.LABEL label, a.NUMBER_FILE numberFile, a.WO_FILE_TEMP_ID woFileTempId,
        case
          when
          lewt.LEE_VALUE is null
          then to_char(lewt.WO_TYPE_NAME)
          else to_char(lewt.LEE_VALUE) end woTypeName
from    WFM.wo_type w,
        lang_exchange_wo_type lewt,
        COMMON_GNOC.WO_FILE_TEMP a
where   w.is_enable = 1
and     w.wo_type_id = lewt.wo_type_id and a.wo_type_id = w.wo_type_id
