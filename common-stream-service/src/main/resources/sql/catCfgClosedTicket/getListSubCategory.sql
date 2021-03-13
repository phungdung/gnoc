SELECT  *
FROM    COMMON_GNOC.CAT_ITEM a
WHERE   status = 1
AND     CATEGORY_ID IN (SELECT  CATEGORY_ID
                        FROM    COMMON_GNOC.CATEGORY
                        WHERE   CATEGORY_CODE = :ptSubCategory)
