SELECT a.id       AS id ,
  a.GROUP_ID      AS groupId ,
  a.UNIT_ID       AS unitId ,
  b.WO_GROUP_CODE AS groupCode ,
  b.WO_GROUP_NAME AS groupName ,
  c.UNIT_CODE     AS unitCode ,
  c.UNIT_NAME     AS unitName
FROM open_pm.COORDINATION_SETTING a
LEFT JOIN WFM.WO_CD_GROUP b
ON a.GROUP_ID = b.WO_GROUP_ID
LEFT JOIN COMMON_GNOC.UNIT c
ON a.UNIT_ID = c.UNIT_ID
WHERE 1      = 1
