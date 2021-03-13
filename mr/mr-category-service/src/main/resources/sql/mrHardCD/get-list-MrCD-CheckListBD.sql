WITH 
list_language_exchange AS (
  select
    LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  from COMMON_GNOC.LANGUAGE_EXCHANGE LE
  where LE.APPLIED_SYSTEM = :applied_system and LE.APPLIED_BUSSINESS = :bussiness and LE.LEE_LOCALE = :p_leeLocale
)
SELECT CD.CHECKLIST_ID checkListId,
       CD.MARKET_CODE marketCode,
       CD.ARRAY_CODE arrayCode,
       CD.DEVICE_TYPE deviceType,
       CD.CYCLE cycle,
       CD.CREATED_DATE createdDate,
       CD.CREATED_USER createdUser,
       CD.UPDATED_DATE updatedDate,
       CD.UPDATED_USER updatedUser,
       CD.PURPOSE purPose,
       CD.CONTENT content,
       CD.GOAL goal,
       B.LOCATION_NAME as marketName,
      case
      when llx.LEE_VALUE is null
      then to_char(CD.ARRAY_CODE)
      else to_char(llx.LEE_VALUE) end arrayName
FROM MR_CD_CHECKLIST_BD CD
left join COMMON_GNOC.CAT_LOCATION b on CD.MARKET_CODE = B.LOCATION_ID
left join COMMON_GNOC.CAT_ITEM c on CD.ARRAY_CODE = c.ITEM_VALUE
left join list_language_exchange llx on c.item_id = llx.BUSSINESS_ID
WHERE 1 = 1
