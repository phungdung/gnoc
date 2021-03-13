select  distinct s.ID id,
        s.LAST_UPDATE_TIME + :offset * interval '1' hour lastUpdateTime,
        s.SCHEDULE schedule,
        s.SYSTEM_CODE systemCode,
        s.SYSTEM_NAME systemName,
        s.SYSTEM_PRIORITY systemPriority,
        ci2.item_name systemPriorityName,
        s.COUNTRY_ID countryId,
        ci1.item_name countryName
from    risk_system s
        left join risk_system_detail sd on s.ID = sd.SYSTEM_ID
        left join common_gnoc.cat_item ci1 on s.COUNTRY_ID = ci1.item_id
        left join (select * from COMMON_GNOC.CAT_ITEM
                    where STATUS = 1
                    and PARENT_ITEM_ID is null
                    and CATEGORY_ID = (select CATEGORY_ID
                                        from COMMON_GNOC.CATEGORY
                                        where CATEGORY_CODE = :categoryCode
                                        and status = 1)) ci2
            ON TO_CHAR(s.SYSTEM_PRIORITY) = ci2.ITEM_VALUE
where   s.ID = :id
