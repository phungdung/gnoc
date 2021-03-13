WITH
list_language_exchange AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 263 and ITEM_CODE = :p_system) CAT1
    on LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  join (select * from COMMON_GNOC.CAT_ITEM where CATEGORY_ID = 262 and ITEM_CODE = :p_bussiness) CAT2
    on LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  where LE.BUSSINESS_ID = :p_bussinessId
  and (:p_leeLocale is null or (:p_leeLocale is not null and LE.LEE_LOCALE = :p_leeLocale))
)
SELECT
  llx.LEE_ID leeId,
  llx.APPLIED_SYSTEM appliedSystem,
  llx.APPLIED_BUSSINESS appliedBussiness,
  llx.BUSSINESS_ID bussinessId,
  llx.BUSSINESS_CODE bussinessCode,
  gl.LANGUAGE_KEY leeLocale,
  llx.LEE_VALUE leeValue,
  gl.LANGUAGE_NAME leeLocaleName,
  gl.LANGUAGE_FLAG leeLocaleFlag
FROM list_language_exchange llx
RIGHT JOIN COMMON_GNOC.GNOC_LANGUAGE gl ON gl.LANGUAGE_KEY = llx.LEE_LOCALE
ORDER BY gl.LANGUAGE_NAME
