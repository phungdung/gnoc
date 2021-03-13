select a.TROUBLE_CARD_ID troubleCardId,
a.CARD_NAME cardName,
a.MER_NAME merName,
a.MER_CODE merCode,
a.CONTRACT_CODE contractCode,
a.SERIAL_NO serialNo,
a.STATION_CODE stationCode ,
a.UPDATE_TIME updateTime,
a.CR_ALTERNATIVE crAlternative,
a.SERIAL_ALTERNATIVE serialAlternative ,
a.DELIVERY_BILL deliveryBill,
a.SLOT_CARD_PORT slotCardPort,
a.TROUBLE_ID troubleId
from ONE_TM.TROUBLE_CARD a
WHERE 1=1
