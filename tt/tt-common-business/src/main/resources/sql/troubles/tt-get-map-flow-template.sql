select a.ID id, a.TYPE_ID typeId, t.ITEM_NAME typeName, a.ALARM_GROUP_ID alarmGroupId,al.ITEM_NAME alarmGroupName,
           a.last_update_time lastUpdateTime
           from COMMON_GNOC.MAP_FLOW_TEMPLATES a, COMMON_GNOC.CAT_ITEM t, COMMON_GNOC.CAT_ITEM al
           WHERE a.TYPE_ID = t.ITEM_ID AND a.ALARM_GROUP_ID = al.ITEM_ID
