SELECT sr.SR_ID srId ,
  sr.SR_CODE srCode ,
  sr.STATUS status ,
  sr.UPDATED_TIME updatedTime ,
  src.SUB_ORDER_ID subOrderId
FROM OPEN_PM.SR sr
INNER JOIN OPEN_PM.SR_CREATED_FROM_OTHER_SYS src
ON sr.SR_CODE    = src.SR_CODE
WHERE sr.SR_CODE = :srCode
