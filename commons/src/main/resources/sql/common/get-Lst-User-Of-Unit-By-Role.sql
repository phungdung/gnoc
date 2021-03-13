select  user_id userId
from    common_gnoc.users
where   UNIT_ID = :unitId
and     USER_ID in (select b.USER_ID
                    from COMMON_GNOC.ROLE_USER b
                    where b.ROLE_ID = (select c.ROLE_ID
                                        from COMMON_GNOC.ROLES c
                                        where c.ROLE_CODE = :roleCode))
