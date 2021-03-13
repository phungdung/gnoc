SELECT
  a.lee_id leeId,
  a.applied_system appliedSystem,
  a.applied_bussiness appliedBussiness,
  a.bussiness_id bussinessId,
  a.bussiness_code bussinessCode,
  a.lee_locale leeLocale,
  a.lee_value leeValue
FROM
  common_gnoc.language_exchange a
where
  a.applied_system = :appliedSystem and
  a.applied_bussiness = :appliedBussiness
  and lower(a.lee_locale) like :leeLocale
