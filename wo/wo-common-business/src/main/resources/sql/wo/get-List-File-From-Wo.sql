select  w.wo_id woId,
        w.file_name fileName,
        w.last_update_time lastUpdateTime,
        w.create_date createDate
from    wo w
where   w.wo_id = :woId
