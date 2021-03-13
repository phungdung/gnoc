SELECT d.WO_MATERIAL_DEDUCTE_ID woMaterialDeducteId,
  d.SEND_IM_RESULT sendImResult,
  d.user_id userId,
  d.user_name userName,
  wo_id woId,
  d.quantity quantity ,
  d.material_id materialId,
  m.nation_code nationCode,
  m.nation_id nationId ,
  d.IS_DEDUCTE isDeducte,
  d.action_id actionId,
  d.REASON_ID reasonId,
  d.CREATE_DATE createDate ,
  d.SEND_IM_TIME sendImTime,
  d.FIRST_METERS_INDEX firstMetersIndex ,
  d.LAST_METERS_INDEX lastMetersIndex,
  d.CABLE_LENGTH_ON_NIMS cableLengthOnNims ,
  d.serial,
  d.type
FROM wfm.wo_material_deducte d,
  wfm.wo_material m
WHERE d.material_id = m.material_id
AND d.WO_ID = :woId
