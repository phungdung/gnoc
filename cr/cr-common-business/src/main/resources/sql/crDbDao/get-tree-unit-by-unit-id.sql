select unit_id as unitId,
       is_committee as isNoc,
       status
from common_gnoc.unit ut
start with unit_id = :unit_id
connect by prior parent_unit_id = unit_id
order  SIBLINGS by unit_id;
