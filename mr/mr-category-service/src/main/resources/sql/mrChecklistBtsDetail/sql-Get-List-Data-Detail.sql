WITH lang_exchange_content AS (
  SELECT  d1.CHECKLIST_DETAIL_ID,
          d1.CHECKLIST_ID,
          CASE
            WHEN le1.LEE_VALUE IS NULL THEN TRANSLATE (d1.CONTENT USING NCHAR_CS)
            ELSE le1.LEE_VALUE
          END CONTENT,
          le1.LEE_VALUE
  FROM    MR_CHECKLISTS_BTS_DETAIL d1
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le1 ON d1.CHECKLIST_DETAIL_ID = le1.BUSSINESS_ID
                                                      AND le1.APPLIED_SYSTEM = :systemMrCheck
                                                      AND le1.APPLIED_BUSSINESS = :bussinessMrCheck
                                                      AND le1.LEE_LOCALE = :leeLocale
                                                      AND le1.BUSSINESS_CODE = :bussinessContent),
      lang_exchange_capture AS (
  SELECT  d2.CHECKLIST_DETAIL_ID,
          d2.CHECKLIST_ID,
          CASE
            WHEN le2.LEE_VALUE IS NULL THEN TRANSLATE (d2.CAPTURE_GUIDE USING NCHAR_CS)
            ELSE le2.LEE_VALUE
          END CAPTURE_GUIDE,
          le2.LEE_VALUE
  FROM    MR_CHECKLISTS_BTS_DETAIL d2
          LEFT JOIN COMMON_GNOC.LANGUAGE_EXCHANGE le2 ON d2.CHECKLIST_DETAIL_ID = le2.BUSSINESS_ID
                                                      AND le2.APPLIED_SYSTEM = :systemMrCheck
                                                      AND le2.APPLIED_BUSSINESS = :bussinessMrCheck
                                                      AND le2.LEE_LOCALE = :leeLocale
                                                      AND le2.BUSSINESS_CODE = :bussinessCapture)
SELECT  d.CHECKLIST_DETAIL_ID checklistDetailId,
        d.CHECKLIST_ID checklistId,
        lec1.CONTENT content,
        d.PHOTO_REQ photoReq,
        d.MIN_PHOTO minPhoto,
        d.MAX_PHOTO maxPhoto,
        d.SCORE_CHECKLIST scoreChecklist,
        d.IS_IMPORTAINT isImportant,
        lec2.CAPTURE_GUIDE captureGuide
FROM    MR_CHECKLISTS_BTS_DETAIL d
        LEFT JOIN lang_exchange_content lec1 ON d.CHECKLIST_DETAIL_ID = lec1.CHECKLIST_DETAIL_ID
        LEFT JOIN lang_exchange_capture lec2 ON d.CHECKLIST_DETAIL_ID = lec2.CHECKLIST_DETAIL_ID
WHERE   1 = 1
