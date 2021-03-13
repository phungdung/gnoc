WITH list_language_exchange_p AS
  (SELECT LE.LEE_ID,
    LE.APPLIED_SYSTEM,
    LE.APPLIED_BUSSINESS,
    LE.BUSSINESS_ID,
    LE.BUSSINESS_CODE,
    LE.LEE_LOCALE,
    LE.LEE_VALUE
  FROM COMMON_GNOC.LANGUAGE_EXCHANGE LE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 263
    AND ITEM_CODE     = 'OPEN_PM'
    ) CAT1
  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 262
    AND ITEM_CODE     = 'OPEN_PM.CR_PROCESS'
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  lst_process_language AS
  (SELECT lcp.cr_process_id CR_PROCESS_ID,
    lcp.cr_process_code cr_process_code,
    CASE
      WHEN lle.LEE_VALUE IS NULL
      THEN lcp.cr_process_name
      ELSE lle.LEE_VALUE
    END cr_process_name
  FROM OPEN_PM.cr_process lcp
  LEFT JOIN list_language_exchange_p lle
  ON lcp.cr_process_id = lle.BUSSINESS_ID
  )
SELECT DISTINCT cr_id.ID id,
  cr_id.SERVICE_CODE serviceCode,
  src.SERVICE_NAME serviceName,
  cr_id.PROCESS_TYPE_LV3_ID processTypeLv3Id,
  cr_parent.CR_PROCESS_ID crProcessParentId,
  cr_parent.cr_process_name process,
  cr_id.WO_TEST_TYPE_ID woTestTypeId,
  wt2.WO_TYPE_NAME woTestTypeIdStr,
  cr_id.WO_FT_TYPE_ID woFtTypeId,
  wt1.WO_TYPE_NAME woFtTypeIdStr,
  cr_id.GROUP_CD_FT groupCDFT,
  cr_id.GROUP_CD_FT_SERVICE groupCdFtService,
  cr_id.WO_CONTENT_CDFT woContentCDFT,
  cr_id.WO_CONTENT_SERVICE woContentService,
  cr_id.SERVICE_AFFECTING serviceAffecting,
  cr_id.AFFECTED_SERVICE affectingService,
  cr_id.TOTAL_AFFECTED_CUSTOMERS totalAffectingCustomers,
  cr_id.TOTAL_AFFECTED_MINUTES totalAffectingMinutes,
  cr_id.IS_CR_NODES isCrNodes,
  cr_id.UNIT_IMPLEMENT unitImplement,
  un.UNIT_NAME unitImplementName,
  cr_id.DUTY_TYPE dutyType,
  cr_id.AUTO_CREATE_CR autoCreateCR,
  cr_id.CR_DESCRIPTION descriptionCr,
  cr_id.WO_FT_DESCRIPTION woFtDescription,
  cr_id.WO_TEST_DESCRIPTION woTestDescription,
  cr_id.WO_FT_PRIORITY woFtPriority,
  w1.PRIORITY_NAME woFtPriorityStr,
  cr_id.WO_TEST_PRIORITY woTestPriority,
  w2.PRIORITY_NAME woTestPriorityStr,
  cr_id.CR_TITLE crTitle,
  cr_id.CR_STATUS crStatus,
  cr_id.TYPE_FIND_NODE typeFindNode
FROM OPEN_PM.sr_mapping_process_cr cr_id
LEFT JOIN COMMON_GNOC.UNIT un
ON un.UNIT_ID = cr_id.UNIT_IMPLEMENT
LEFT JOIN lst_process_language cr_parent
ON cr_parent.CR_PROCESS_ID = cr_id.CR_PROCESS_PARENT_ID
LEFT JOIN lst_process_language cr_children
ON cr_children.CR_PROCESS_ID = cr_id.CR_PROCESS_ID
LEFT JOIN WFM.WO_PRIORITY w1
ON w1.PRIORITY_ID = cr_id.WO_FT_PRIORITY
LEFT JOIN WFM.WO_PRIORITY w2
ON w2.PRIORITY_ID = cr_id.WO_TEST_PRIORITY
LEFT JOIN WFM.WO_TYPE wt1
ON wt1.WO_TYPE_ID = cr_id.WO_FT_TYPE_ID
LEFT JOIN WFM.WO_TYPE wt2
ON wt2.WO_TYPE_ID = cr_id.WO_TEST_TYPE_ID
JOIN OPEN_PM.sr_catalog src
ON cr_id.SERVICE_CODE = src.SERVICE_CODE
WHERE 1               = 1
