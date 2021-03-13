SELECT a.cr_process_wo_id crProcessWoId, a.wo_name woName, a.wo_status woStatus, a.description description,a.IS_REQUIRE_WO isRequireCloseWo, a.is_require isRequire,
 a.duration_wo durationWo, a.cr_process_id crProcessId ,
 a.WO_TYPE_ID as woTypeId , a.CREATE_WHEN_CLOSE_CR as createWoWhenCloseCR , b.WO_TYPE_NAME as woTypeName  FROM open_pm.cr_process_wo a
left join WFM.WO_TYPE b on a.WO_TYPE_ID = b.WO_TYPE_ID where a.cr_process_id= :crProcessId
