select  ci.*
from    COMMON_GNOC.CAT_ITEM ci
where   ci.STATUS = 1
and     ci.CATEGORY_ID = (Select c.CATEGORY_ID
                          from COMMON_GNOC.CATEGORY c
                          where c.CATEGORY_CODE = :categoryCode)
and     LOWER(ci.ITEM_CODE) LIKE :itemCode ESCAPE '\'
