SELECT  u.UNIT_ID unitId,
        u.UNIT_NAME unitName,
        u.UNIT_CODE unitCode,
        u.SMS_GATEWAY_ID smsGatewayId
FROM    COMMON_GNOC.UNIT u
WHERE   u.STATUS = 1
