SELECT  w.*
FROM    WO w
WHERE   w.PARENT_ID = :parentId
AND     w.STATUS <> 4
AND     w.STATUS <> 2
