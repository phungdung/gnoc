SELECT  k.KEDB_ID kedbId,
        k.KEDB_CODE kedbCode,
        k.KEDB_NAME kedbName,
        k.VENDOR vendor,
        ci1.ITEM_NAME vendorName,
        k.PARENT_TYPE_ID parentTypeId,
        ci2.ITEM_NAME parentTypeName,
        k.TYPE_ID typeId,
        ci3.ITEM_NAME typeName,
        k.SUB_CATEGORY_ID subCategoryId,
        ci4.ITEM_NAME subCategoryName,
        k.KEDB_STATE kedbState,
        ci5.ITEM_NAME kedbStateName,
        k.CREATE_USER_ID createUserId,
        k.CREATE_USER_NAME createUserName,
        k.CREATED_TIME createdTime,
        k.SOFTWARE_VERSION softwareVersion,
        k.HARDWARE_VERSION hardwareVersion,
        k.COMPLETER completer,
        k.PT_TT_RELATED ptTtRelated,
        k.UNIT_CHECK_ID unitCheckId,
        k.UNIT_CHECK_NAME unitCheckName,
        kr.AVERAGE_RATING averageRating,
        kr.NUM_LIKE numLike,
        kr2.POINT yourRating,
        k.DESCRIPTION description,
        k.RCA rca,
        k.SOLUTION solution,
        k.COMMENTS comments
FROM    KEDB k
        LEFT JOIN (SELECT KEDB_ID, SUM(POINT)/COUNT(KEDB_ID) AVERAGE_RATING, COUNT(KEDB_ID) NUM_LIKE
                          FROM KEDB_RATING GROUP BY KEDB_ID) kr ON k.KEDB_ID = kr.KEDB_ID
        LEFT JOIN KEDB_RATING kr2 ON k.KEDB_ID = kr2.KEDB_ID AND kr2.USER_NAME = :userName
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci1 ON k.VENDOR = TO_CHAR(ci1.ITEM_ID)
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci2 ON k.PARENT_TYPE_ID = ci2.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci3 ON k.TYPE_ID = ci3.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci4 ON k.SUB_CATEGORY_ID = ci4.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci5 ON k.KEDB_STATE = ci5.ITEM_ID
WHERE   1 = 1
