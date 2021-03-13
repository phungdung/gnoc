SELECT FLEX.FLOW_NAME flowName,
  FLEX.FLOW_DESCRIPTION flowDescription,
  FLEX.listCountry listCountry,
  FLEX.listflowId listflowId
FROM
  (SELECT FLOW_NAME,
    FLOW_DESCRIPTION ,
    listagg(COUNTRY, ',') within GROUP (
  ORDER BY FLOW_ID) listCountry ,
    listagg(FLOW_ID, ',') within GROUP (
  ORDER BY FLOW_ID) listflowId,
  REGEXP_SUBSTR(listagg(FLOW_ID, ',') within GROUP (
  ORDER BY FLOW_ID desc),'[^,]+',1,1) flowIdOrder
  FROM SR_FLOW_EXECUTE
  GROUP BY FLOW_NAME,
    FLOW_DESCRIPTION
  ) FLEX
WHERE 1=1
