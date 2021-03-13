
WITH
     lang_exchange_priority AS (
    SELECT  p.PRIORITY_ID,
            p.PRIORITY_NAME,
            p.PRIORITY_CODE,
            le1.LEE_VALUE,
            le1.LEE_LOCALE
    FROM    wfm.WO_PRIORITY p
              LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le1 ON p.PRIORITY_ID = le1.BUSSINESS_ID
                                                               AND le1.APPLIED_SYSTEM = 4
                                                               AND le1.APPLIED_BUSSINESS = 3
                                                               AND le1.LEE_LOCALE = :leeLocale
    ),
     lang_exchange_wo_type AS (
    SELECT  t.WO_TYPE_ID,
            t.WO_TYPE_CODE,
            t.WO_TYPE_NAME,
            t.TIME_OVER,
            le2.LEE_VALUE,
            le2.LEE_LOCALE
    FROM    wfm.WO_TYPE t
              LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON t.WO_TYPE_ID = le2.BUSSINESS_ID
                                                               AND le2.APPLIED_SYSTEM = 4
                                                               AND le2.APPLIED_BUSSINESS = 1
                                                               AND le2.LEE_LOCALE = :leeLocale
    )
SELECT
       w.wo_id woId
FROM wfm.wo w
         --LEFT JOIN wo p on w.parent_id = p.wo_id
       INNER JOIN wfm.wo_cd_group g on w.cd_id = g.wo_group_id
       INNER JOIN lang_exchange_wo_type lewt on w.wo_type_id = lewt.wo_type_id
       LEFT JOIN lang_exchange_priority lep on w.priority_id = lep.priority_id
       LEFT JOIN wfm.wo_detail wd on w.wo_id = wd.wo_id
       LEFT JOIN wfm.wo_trouble_info wti on w.wo_id = wti.wo_id
       LEFT JOIN wfm.wo_ktts_info wki on w.wo_id = wki.wo_id
       LEFT JOIN common_gnoc.users f on w.ft_id = f.USER_ID
       INNER JOIN common_gnoc.users cp on w.create_person_id = cp.USER_ID
       INNER JOIN common_gnoc.unit uc on cp.UNIT_ID = uc.UNIT_ID
WHERE 1=1
