SELECT
  cr.id id,
  cr.PARENT_ID parentId,
  cr.REASON_NAME reasonName,
  cr.REASON_CODE reasonCode,
  cr.TYPE_ID typeId,
  cr.DESCRIPTION,
  cr.IS_CHECK_SCRIPT isCheckScript,
  cr.is_update_closure isUpdateClosure,
  cr.reasonType
from (SELECT
        a.id,
        a.PARENT_ID,
        a.REASON_NAME,
        a.REASON_CODE,
        a.TYPE_ID,
        SYS_CONNECT_BY_PATH (a.REASON_NAME, '-->')|| '-->' as DESCRIPTION,
        SYS_CONNECT_BY_PATH (a.LEE_VALUE, '-->')|| '-->' as reasonType,
        a.IS_CHECK_SCRIPT,
        a.is_update_closure
      FROM
        (select * FROM one_tm.cat_reason crn
        LEFT JOIN
            (SELECT *
                FROM COMMON_GNOC.LANGUAGE_EXCHANGE
                WHERE applied_system  = 3
                AND applied_bussiness = 1
                AND lee_locale        = 'en_US'
            ) b ON crn.ID = b.LEE_ID
    ) a
 WHERE LEVEL < 50 )
