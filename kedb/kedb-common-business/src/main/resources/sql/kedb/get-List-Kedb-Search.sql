select  *
from    Kedb
where   (LOWER(kedbCode) like :kedbCode escape '\\'
        or LOWER(kedbName) like :kedbName escape '\\'
        or LOWER(description) like :description escape '\\'
        or LOWER(ttWa) like :ttWa escape '\\'
        or LOWER(rca) like :rca escape '\\'
        or LOWER(ptWa) like :ptWa escape '\\'
        or LOWER(solution) like :solution escape '\\')
