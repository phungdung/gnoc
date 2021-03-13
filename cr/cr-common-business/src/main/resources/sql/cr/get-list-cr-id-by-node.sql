select a.CR_ID as crId  from OPEN_PM.CR_IMPACTED_NODES a
where a.INSERT_TIME >= :startDate and a.INSERT_TIME <= :endDate
and a.IP_ID in (:ipIds)
