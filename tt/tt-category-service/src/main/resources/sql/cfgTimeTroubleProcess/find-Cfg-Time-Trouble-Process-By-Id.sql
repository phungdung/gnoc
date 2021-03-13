SELECT  cfg.ID id,
        cfg.TYPE_ID typeId,
        ci1.ITEM_NAME typeName,
        cfg.SUB_CATEGORY_ID subCategoryId,
        ci2.ITEM_NAME subCategoryName,
        cfg.PRIORITY_ID priorityId,
        ci3.ITEM_NAME priorityName,
        cfg.CREATE_TT_TIME createTtTime,
        cfg.PROCESS_TT_TIME processTtTime,
        cfg.TIME_STATION_VIP timeStationVip,
        cfg.TIME_WO_VIP timeWoVip,
        cfg.CLOSE_TT_TIME closeTtTime,
        cfg.PROCESS_WO_TIME processWoTime,
        cfg.COUNTRY country,
        ci4.ITEM_NAME countryName,
        cfg.TIME_ABOUT_OVERDUE timeAboutOverdue,
        cfg.IS_STATION_VIP isStationVip,
        cfg.IS_CALL isCall,
        cfg.CD_AUDIO_NAME cdAudioName,
        cfg.FT_AUDIO_NAME ftAudioName,
        cfg.LAST_UPDATE_TIME lastUpdateTime
FROM    CFG_TIME_TROUBLE_PROCESS cfg
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci1 ON cfg.TYPE_ID = ci1.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci2 ON cfg.SUB_CATEGORY_ID = ci2.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci3 ON cfg.PRIORITY_ID = ci3.ITEM_ID
        LEFT JOIN COMMON_GNOC.CAT_ITEM ci4 ON cfg.COUNTRY = ci4.ITEM_CODE
WHERE   1 = 1
