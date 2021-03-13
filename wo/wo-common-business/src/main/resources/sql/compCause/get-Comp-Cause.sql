 select (case when (a.nation_code is not null and a.nation_id is not null) then a.nation_id else a.comp_cause_id end) compCauseId,
        a.name name,
        a.parent_id parentId,
        a.level_id levelId,
        a.code code
from    common_gnoc.comp_cause a
where   a.comp_cause_id = :compCauseId
