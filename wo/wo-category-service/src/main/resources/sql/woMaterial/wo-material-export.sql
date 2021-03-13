WITH
  list_language_exchange AS (
  SELECT
    cat.ITEM_VALUE,
    cat.ITEM_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE,
    LE.LEE_LOCALE
  FROM (SELECT * FROM COMMON_GNOC.CAT_ITEM WHERE CATEGORY_ID = (
    Select CATEGORY_ID
    from COMMON_GNOC.CATEGORY
    where CATEGORY_CODE= 'WO_TECHNOLOGY_CODE'
    and EDITABLE = 1
    )
  ) cat
  LEFT JOIN  COMMON_GNOC.LANGUAGE_EXCHANGE LE
    ON LE.BUSSINESS_ID = cat.ITEM_ID
    and LE.APPLIED_SYSTEM = 1
    and LE.APPLIED_BUSSINESS = 3
    and LE.LEE_LOCALE = :p_leeLocale
    ),
  list_language_exchange_service As (
  SELECT
    cs.SERVICE_CODE,
    cs.SERVICE_ID,
    cs.SERVICE_NAME,
    LE.BUSSINESS_ID,
    LE.LEE_VALUE
  FROM COMMON_GNOC.CAT_SERVICE cs
  LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE LE
    ON LE.BUSSINESS_ID = cs.SERVICE_ID
    and LE.LEE_LOCALE = :p_leeLocale
    and LE.APPLIED_SYSTEM = 1
    and LE.APPLIED_BUSSINESS = 8
  )
  SELECT
	b.material_code materialCode,
	b.material_name materialName,
	d.item_code actionCode,
	d.item_name actionName,
  case
    when ls.LEE_VALUE is null
    then ls.SERVICE_NAME
  else to_char(ls.LEE_VALUE) end serviceName,
	ls.service_code serviceCode,
	a.material_thres_id materialThresId,
  a.tech_thres techThresStr,
	a.warning_thres warningThresStr,
	a.free_thres freeThresStr,
  a.material_id materialId,
	a.action_id actionId,
	a.service_id serviceId,
	decode(a.tech_distanct_thres,null,'','A/' || a.tech_distanct_thres) techDistanctThresStr,
  decode(a.warning_distanct_thres,null,'','A/' || a.warning_distanct_thres) warningDistanctThresStr,
  decode(a.free_distanct_thres,null,'','A/' || a.free_distanct_thres) freeDistanctThresStr,
	a.infra_type infraType,
  case
    when
    llex.LEE_VALUE is null
    then llex.ITEM_NAME
  else llex.LEE_VALUE end technology
from
	MATERIAL_THRES a
	left join wo_material b on a.material_id = b.material_id
	left join COMMON_GNOC.CAT_ITEM d on a.action_id = d.item_id
	left join list_language_exchange_service ls ON a.service_id = ls.SERVICE_ID
  left join list_language_exchange llex ON to_char(a.infra_type) = llex.ITEM_VALUE
where
	d.category_id = (select category_id from common_gnoc.category where category_code = 'WO_ACTION_GROUP')
and a.is_enable = 1
and 1=1
