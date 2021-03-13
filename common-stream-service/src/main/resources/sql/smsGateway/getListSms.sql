SELECT sgw.SMS_GATEWAY_ID smsGatewayId,
sgw.CONTENT_TYPE_TEXT contentTypeText,
sgw.DEFAULT_SESSION_ID defaultSessionId,
sgw.SENDER sender,
sgw.SERVICE_ID serviceId,
sgw.STATUS_NOT_CHARGE statusNotCharge,
sgw.USER_NAME userName,
sgw.PASS_WORD passWord,
sgw.URL url,
sgw.XMLNS xmlns,
sgw.ALIAS alias,
sgw.PREFIX prefix,
sgw.NUM_OF_THREAD numOfThread
FROM COMMON_GNOC.SMS_GATEWAY sgw
WHERE 1=1
