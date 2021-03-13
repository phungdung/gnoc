SELECT  w.*
FROM    WO w
WHERE   w.AMI_ONE_ID = :amiOneId
AND     w.CREATE_DATE > sysdate - 60
