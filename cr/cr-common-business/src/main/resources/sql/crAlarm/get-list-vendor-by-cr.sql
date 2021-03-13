SELECT CVD_ID cvdId,
  CR_ID crId,
  VENDOR_CODE vendorCode,
  VENDOR_NAME vendorName,
  to_char(CREATE_TIME, 'dd/mm/yyyy hh24:mi:ss') createTime
FROM CR_VENDOR_DETAIL
WHERE 1 = 1
