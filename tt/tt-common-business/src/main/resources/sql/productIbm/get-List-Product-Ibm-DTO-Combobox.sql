SELECT  p.CODE code,
        p.NAME name,
        p.STATUS status,
        p.UNIT_CODE unitCode
FROM    PRODUCT_IBM p
WHERE   p.STATUS = 1
