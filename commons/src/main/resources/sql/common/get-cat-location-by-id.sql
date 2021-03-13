select  a.LOCATION_ID locationId,
        a.LOCATION_CODE locationCode,
        a.LOCATION_NAME locationName,
        a.PARENT_ID parentId,
        a.STATUS,
        a.NATION_CODE nationCode
from    COMMON_GNOC.cat_LOCATION a
where   a.LOCATION_ID = :locationId
