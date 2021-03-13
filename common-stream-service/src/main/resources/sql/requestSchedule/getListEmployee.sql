SELECT
       t1.ID_EMPLOYEE idEmployee,
       t1.ID_SCHEDULE idSchedule,
       t1.NAME_DISPLAY nameDisplay,
       t1.USERNAME userName,
       t1.EMP_ARRAY empArray,
       t1.EMP_CHILDREN empChildren,
       t1.EMP_LEVEL empLevel,
       t1.UNIT_ID unitId,
       t1.DAY_OFF dayOff,
       t4.item_name empLevelName
FROM COMMON_GNOC.SCHEDULE_EMPLOYEE t1
       left join COMMON_GNOC.CAT_ITEM t4 ON to_char(t1.EMP_LEVEL) = t4.ITEM_VALUE
                                              and t4.CATEGORY_ID in (select CATEGORY_ID from COMMON_GNOC.CATEGORY where CATEGORY_CODE = 'GNOC_IMPACT')
WHERE t1.UNIT_ID = :unitId AND t1.ID_SCHEDULE = :idSchedule
