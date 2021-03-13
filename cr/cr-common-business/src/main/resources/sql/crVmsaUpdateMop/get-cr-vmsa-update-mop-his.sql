select  his.CR_VMSA_UDATE_MOP_HIS_ID crVmsaUpdateMopHisId ,
  his.CR_ID as crId ,
  his.VALDATE_KEY as validateKey  ,
  his.SYSTEM_CCODE as systemCode ,
  his.result_code as resultCode ,
  his.CREATE_TIME as createTime
from OPEN_PM.CR_VMSA_UDATE_MOP_HIS his
where
CR_ID =:crId
and VALDATE_KEY = :validateKey
and CREATE_TIME >= SYSDATE - 30
