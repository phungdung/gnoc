WITH lang_exchange_priority AS (
  SELECT  p.PRIORITY_ID,
          p.PRIORITY_NAME,
          p.PRIORITY_CODE,
          le1.LEE_VALUE,
          le1.LEE_LOCALE
  FROM    WO_PRIORITY p
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le1 ON p.PRIORITY_ID = le1.BUSSINESS_ID
                                                      AND le1.APPLIED_SYSTEM = 4
                                                      AND le1.APPLIED_BUSSINESS = 3
                                                      AND le1.LEE_LOCALE = :leeLocale),
      lang_exchange_wo_type AS (
  SELECT  t.WO_TYPE_ID,
          t.WO_TYPE_CODE,
          t.WO_TYPE_NAME,
          t.TIME_OVER,
          le2.LEE_VALUE,
          le2.LEE_LOCALE
  FROM    WO_TYPE t
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = 4
                                                      AND le2.APPLIED_BUSSINESS = 1
                                                      AND le2.LEE_LOCALE = :leeLocale)
select
  w.wo_id woId,
  w.wo_code woCode,
  w.parent_id parentId,
  p.wo_code parentName,
  w.wo_type_id woTypeId,
  case
      when
      lewt.LEE_VALUE is null
      then to_char(lewt.WO_TYPE_NAME)
      else to_char(lewt.LEE_VALUE) end woTypeName,
  w.wo_content woContent,
  w.status status,
  CASE w.status
    WHEN 0 THEN :unassigned
    WHEN 1 THEN :assigned
    WHEN 2 THEN (case when w.ft_id is not null then :reject
                      else :rejectCd end)
    WHEN 3 THEN :dispatch
    WHEN 4 THEN :accept
    WHEN 5 THEN :inProcess
    WHEN 9 THEN :pending
    WHEN 8 THEN :closedCd
    WHEN 6 THEN (case when w.result is null then :closedFt
                      else :closedCd end)
    WHEN 7 THEN :draft
    ELSE '' END statusName,
  w.wo_system woSystem,
  w.wo_system_id woSystemId,
  w.create_person_id createPersonId,
  cp.username createPersonName,
  w.create_date + :offset * interval '1' hour createDate,
  w.cd_id cdId,
  g.wo_group_name cdName,
  w.ft_id ftId,
  f.username ftName,
  w.priority_id priorityId,
  case
      when
      lep.LEE_VALUE is null
      then to_char(lep.PRIORITY_NAME)
      else to_char(lep.LEE_VALUE) end priorityName,
  w.start_time + :offset * interval '1' hour startTime,
  w.end_time + :offset * interval '1' hour endTime,
  w.finish_time + :offset * interval '1' hour finishTime
from
  wo w,
  wo p,
  lang_exchange_wo_type lewt,
  common_gnoc.users cp,
  wo_cd_group g,
  common_gnoc.users f,
  lang_exchange_priority lep
where 1 = 1
and w.parent_id = p.wo_id(+)
and w.wo_type_id = lewt.wo_type_id
and w.create_person_id = cp.user_id
and w.cd_id = g.wo_group_id
and w.ft_id = f.user_id(+)
and w.priority_id = lep.priority_id(+)
and w.parent_id = :parentId
