select w.wo_type_id  woTypeId, w.wo_type_code woTypeCode
 , DECODE(l.wo_type_name, null , w.wo_type_name, l.wo_type_name) woTypeName
 , wg.wo_type_group_id woTypeGroupId, wg.wo_group_id woGroupId
 , w.TIME_OVER timeOver, w.SMS_CYCLE smsCycle , w.WO_CLOSE_AUTOMATIC_TIME as woCloseAutomaticTime  from wfm.wo_type w , wfm.wo_type_locale l, wfm.wo_type_group wg
 where w.wo_type_id = l.wo_type_id(+)
 and w.wo_type_id = wg.wo_type_id
