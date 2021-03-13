WITH list_language_exchange AS
    (SELECT LE.LEE_ID,
            LE.APPLIED_SYSTEM,
            LE.APPLIED_BUSSINESS,
            LE.BUSSINESS_ID,
            LE.BUSSINESS_CODE,
            LE.LEE_LOCALE,
            LE.LEE_VALUE
     FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
     WHERE LE.APPLIED_SYSTEM  = :applied_system
       AND LE.APPLIED_BUSSINESS = :bussiness
       AND LE.LEE_LOCALE        = :p_leeLocale
    ),
     list_result AS
    (SELECT dts.device_type_id valueStr,
            dts.device_type_code secondValue,
            CASE
                   WHEN llx.LEE_VALUE IS NULL
                      THEN dts.device_type_name
                   ELSE llx.LEE_VALUE
                END displayStr
     FROM device_types dts
                 LEFT JOIN list_language_exchange llx
              ON dts.device_type_id   = llx.BUSSINESS_ID
     WHERE dts.is_active     = 1
       AND dts.device_type_id IN
           (SELECT cps.device_type_id
            FROM cr_process cps
            WHERE cps.impact_segment_id = :impact_segment_id
              AND NOT EXISTS
                (SELECT 1 FROM cr_process WHERE parent_id = cps.cr_process_id
                )
           )
    )
SELECT * FROM list_result order by displayStr
