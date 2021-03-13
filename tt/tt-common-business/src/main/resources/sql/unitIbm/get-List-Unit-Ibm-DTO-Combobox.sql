SELECT  u.UNIT_CODE unitCode,
        u.UNIT_NAME unitName,
        u.STATUS status
FROM    UNIT_IBM u
WHERE   u.STATUS = 1
