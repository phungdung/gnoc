select  UNIT_ID unitId,
        UNIT_NAME unitName,
        UNIT_CODE unitCode,
        PARENT_UNIT_ID parentUnitId
from    common_gnoc.unit
where   unit_id in (select b.ASSIGN_UNIT_ID
                    from ONE_TM.TROUBLES a
                          inner join ONE_TM.TROUBLE_assign b on a.trouble_id = b.trouble_id
                    where lower(a.TROUBLE_CODE) = :troubleCode)
or       unit_id = (select RECEIVE_UNIT_ID
                    from ONE_TM.TROUBLES
                    where lower(TROUBLE_CODE) = :troubleCode1)
