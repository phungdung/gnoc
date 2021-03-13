select w.wo_type_id  woTypeId, w.wo_type_code woTypeCode
                     , DECODE(l.wo_type_name, null , w.wo_type_name, l.wo_type_name) woTypeName
                     , is_enable isEnable, wo_group_type woGroupType, w.enable_Create enableCreate
                     , w.TIME_OVER timeOver, w.SMS_CYCLE smsCycle , w.WO_CLOSE_AUTOMATIC_TIME as woCloseAutomaticTime
                     , w.allow_pending allowPending, w.create_from_other_sys createFromOtherSys
                     , TIME_AUTO_CLOSE_WHEN_OVER timeAutoCloseWhenOver
                     from wfm.wo_type w , wfm.wo_type_locale l
                     where w.wo_type_id = l.wo_type_id(+)

