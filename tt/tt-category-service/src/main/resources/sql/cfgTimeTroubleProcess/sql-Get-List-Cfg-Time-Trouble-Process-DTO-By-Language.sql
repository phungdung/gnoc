WITH list_language_exchange AS (
SELECT  ci.ITEM_ID,
        ci.ITEM_NAME,
        ci.ITEM_CODE,
        le.LEE_VALUE,
        le.LEE_LOCALE
FROM    COMMON_GNOC.CAT_ITEM ci
        LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le ON ci.ITEM_ID = le.BUSSINESS_ID
WHERE   le.APPLIED_SYSTEM = :appliedSystem
AND     le.APPLIED_BUSSINESS = :appliedBussiness
AND     le.LEE_LOCALE = :leeLocale
)
SELECT  cfg.ID id,
        cfg.TYPE_ID typeId,
        lle1.LEE_VALUE typeName,
        cfg.SUB_CATEGORY_ID subCategoryId,
        lle2.LEE_VALUE subCategoryName,
        cfg.PRIORITY_ID priorityId,
        lle3.LEE_VALUE priorityName,
        cfg.CREATE_TT_TIME createTtTime,
        cfg.PROCESS_TT_TIME processTtTime,
        cfg.TIME_STATION_VIP timeStationVip,
        cfg.TIME_WO_VIP timeWoVip,
        cfg.CLOSE_TT_TIME closeTtTime,
        cfg.PROCESS_WO_TIME processWoTime,
        cfg.COUNTRY country,
        lle4.LEE_VALUE countryName,
        lle4.ITEM_ID countryId,
        cfg.TIME_ABOUT_OVERDUE timeAboutOverdue,
        cfg.IS_STATION_VIP isStationVip,
        cfg.IS_CALL isCall,
        cfg.CD_AUDIO_NAME cdAudioName,
        cfg.FT_AUDIO_NAME ftAudioName,
        cfg.LAST_UPDATE_TIME lastUpdateTime
FROM    CFG_TIME_TROUBLE_PROCESS cfg
        LEFT JOIN list_language_exchange lle1 ON cfg.TYPE_ID = lle1.ITEM_ID
        LEFT JOIN list_language_exchange lle2 ON cfg.SUB_CATEGORY_ID = lle2.ITEM_ID
        LEFT JOIN list_language_exchange lle3 ON cfg.PRIORITY_ID = lle3.ITEM_ID
        LEFT JOIN list_language_exchange lle4 ON cfg.COUNTRY = lle4.ITEM_CODE
WHERE   1 = 1
