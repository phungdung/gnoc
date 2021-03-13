SELECT DISTINCT A.Service_Name serviceName,
  A.service_code serviceCode,
  A.COUNTRY country,
  cl.LOCATION_NAME countryName
FROM Sr_Catalog A
LEFT JOIN common_gnoc.CAT_LOCATION cl
ON cl.LOCATION_ID = A.COUNTRY
JOIN Sr_Config B
ON A.Service_Array = B.Config_Code
AND B.Config_Group ='SERVICE_ARRAY'
AND B.Auto_Mation IS NOT NULL
WHERE Cr           = 1
AND A.Status       ='A'
