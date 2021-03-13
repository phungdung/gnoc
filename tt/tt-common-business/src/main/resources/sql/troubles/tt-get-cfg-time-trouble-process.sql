SELECT   a.id,
                  a.type_id typeId,
                  a.sub_category_id subCategoryId,
                  a.priority_id priorityId,
                   a.create_tt_time createTtTime,
                  a.PROCESS_WO_TIME processWoTime,
                   a.process_tt_time processTtTime,
                   a.close_tt_time closeTtTime,
                  a.is_call isCall,
                  a.IS_STATION_VIP isStationVip,
                   a.TIME_STATION_VIP timeStationVip,
                   a.TIME_WO_VIP timeWoVip,
                  a.cd_Audio_Name cdAudioName,
                   a.ft_Audio_Name ftAudioName
           FROM   one_tm.cfg_time_trouble_process a
            WHERE a.type_id = :typeId
            AND a.sub_category_id = :subCategoryId
            AND a.priority_id = :priority
