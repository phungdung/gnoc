 select * from ( WITH
  list_language_exchange AS (
    SELECT
    a.lee_id ,
    a.applied_system ,
    a.applied_bussiness ,
    a.bussiness_id ,
    a.bussiness_code ,
    a.lee_locale ,
    a.lee_value
  FROM
    common_gnoc.language_exchange a
  where
    a.applied_system = 2 and
    a.applied_bussiness = 9
    and lower(a.lee_locale) like lower(:leeLocale)  escape '\'
  ),
  list_search as(
    select b.GROUP_UNIT_CODE groupUnitCode,b.GROUP_UNIT_ID groupUnitId,b.GROUP_UNIT_NAME groupUnitName,b.IS_ACTIVE isActive from open_pm.GROUP_UNIT b where 1=1 and b.IS_ACTIVE=1
  ),
  list_result as(
   select
     a.groupUnitId groupUnitId,
     a.groupUnitCode groupUnitCode,
      a.isActive isActive,
      case
        when llx.lee_value is null
        then  a.groupUnitName
        else llx.lee_value end groupUnitName
    from list_search a
    left join list_language_exchange llx on a.groupUnitId = llx.bussiness_id
  )
  SELECT
      b.groupUnitId groupUnitId,
     b.groupUnitCode groupUnitCode,
      b.isActive isActive,
      b.groupUnitName
  FROM list_result b) re where 1=1
