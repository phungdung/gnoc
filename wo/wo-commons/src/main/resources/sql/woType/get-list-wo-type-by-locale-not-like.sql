SELECT w.wo_type_id woTypeId,
  w.wo_type_code woTypeCode ,
  DECODE(l.wo_type_name, NULL , w.wo_type_name, l.wo_type_name) woTypeName ,
  is_enable isEnable,
  wo_group_type woGroupType,
  w.enable_Create enableCreate ,
  w.TIME_OVER timeOver,
  w.SMS_CYCLE smsCycle ,
  w.WO_CLOSE_AUTOMATIC_TIME AS woCloseAutomaticTime
FROM wfm.wo_type w ,
  wfm.wo_type_locale l
WHERE w.wo_type_id = l.wo_type_id(+)
