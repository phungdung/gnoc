select  a.trouble_id troubleId,
        a.description description
from    one_tm.troubles a
where   a.trouble_code = :troubleCode
