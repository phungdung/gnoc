 UPDATE $tbName t
 SET t.rcaFoundTime = :rcaFoundTime,
 t.waFoundTime = :waFoundTime,
 t.slFoundTime = :slFoundTime,
 t.lastUpdateTime = :lastUpdateTime
 WHERE t.cfgCode in (:lstCfgCode)
