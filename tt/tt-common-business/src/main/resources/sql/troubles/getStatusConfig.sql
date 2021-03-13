WITH lst_lang AS
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
    AND ITEM_CODE     = :p_system
    ) CAT1
  ON LE.APPLIED_SYSTEM = CAT1.ITEM_VALUE
  JOIN
    (SELECT *
    FROM COMMON_GNOC.CAT_ITEM
    WHERE CATEGORY_ID = 262
    AND ITEM_CODE     = :p_bussiness
    ) CAT2
  ON LE.APPLIED_BUSSINESS = CAT2.ITEM_VALUE
  WHERE LE.LEE_LOCALE     = :p_leeLocale
  ),
  lst_status AS
  (SELECT cat.ITEM_ID,
    cat.ITEM_CODE,
    CASE
      WHEN ll.LEE_VALUE IS NOT NULL
      THEN ll.LEE_VALUE
      ELSE cat.ITEM_NAME
    END ITEM_NAME
  FROM COMMON_GNOC.CAT_ITEM cat
  LEFT JOIN lst_lang ll
  ON ll.bussiness_id      = cat.ITEM_ID
  WHERE cat.STATUS        = 1
  AND cat.PARENT_ITEM_ID IS NULL
  AND cat.CATEGORY_ID     =
    (SELECT CATEGORY_ID
    FROM COMMON_GNOC.CATEGORY
    WHERE CATEGORY_CODE = :categoryCode
    AND status          = 1
    )
  )
SELECT distinct
  ttcs.NEW_STATUS itemValue,
  ci2.ITEM_NAME itemName,
  ttcsr.ROLE_ID description
FROM ONE_TM.TT_CHANGE_STATUS ttcs
INNER JOIN lst_status ci2
ON ttcs.NEW_STATUS = ci2.ITEM_ID
INNER JOIN ONE_TM.tt_change_status_role ttcsr
ON ttcsr.TT_CHANGE_STATUS_ID =ttcs.ID
AND ttcsr.ROLE_ID is not null
WHERE 1            =1
