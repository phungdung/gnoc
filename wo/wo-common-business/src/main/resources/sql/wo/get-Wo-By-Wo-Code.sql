SELECT  w.*
FROM    WO w
WHERE   LOWER(w.WO_CODE) = :woCode
