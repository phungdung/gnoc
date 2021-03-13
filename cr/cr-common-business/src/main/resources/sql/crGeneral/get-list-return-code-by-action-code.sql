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
    (SELECT cc.rccg_id valueStr,
            cc.return_code secondValue,
            CASE
              WHEN llx.LEE_VALUE IS NULL
                      THEN cc.return_title
              ELSE llx.LEE_VALUE
                END displayStr
     FROM return_code_catalog cc
            LEFT JOIN list_language_exchange llx
              ON cc.rccg_id = llx.BUSSINESS_ID
     WHERE cc.is_active      = 1
       AND return_category = :return_code
    )
SELECT * FROM list_result order by displayStr
