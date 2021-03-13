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
        k.CREATED_TIME + :offset * interval '1' hour createdTime,
        k.INFLUENCE_SCOPE influenceScope,
        k.RECEIVE_USER_ID receiveUserId,
        k.RECEIVE_USER_NAME receiveUserName,
        k.PT_TT_RELATED ptTtRelated,
        k.TT_WA ttWa,
        k.RCA rca,
        k.PT_WA ptWa,
        k.SOLUTION solution,
        k.WORKLOG worklog,
        k.COMPLETER completer,
        k.COMPLETE_TIME + :offset * interval '1' hour completedTime,
        k.DESCRIPTION description,
        k.HARDWARE_VERSION hardwareVersion,
        k.SOFTWARE_VERSION softwareVersion
FROM    KEDB k
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci1 ON k.VENDOR = TO_CHAR(ci1.ITEM_ID)
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci2 ON k.PARENT_TYPE_ID = ci2.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci3 ON k.TYPE_ID = ci3.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci4 ON k.SUB_CATEGORY_ID = ci4.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci5 ON k.KEDB_STATE = ci5.ITEM_ID
WHERE   1 = 1
