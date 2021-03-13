select  a.key,
        a.value,
        a.description
from    common_gnoc.config_property a
where   a.key = :key
