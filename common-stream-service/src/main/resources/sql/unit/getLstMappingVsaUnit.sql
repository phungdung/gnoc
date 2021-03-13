SELECT mvut.mvut_id mvutId,
  utApp.unit_code appUnitCode,
  utApp.unit_name appUnitName,
  utApp.unit_name_full appUnitNameFull,
  utVsa.unit_code vsaUnitCode,
  utVsa.unit_name vsaUnitName,
  utVsa.unit_name_full vsaUnitNameFull
FROM common_gnoc.mapping_vsa_unit mvut
LEFT JOIN common_gnoc.v_unit_full utApp
ON mvut.app_unit_id = utApp.unit_id
LEFT JOIN common_gnoc.V_UNIT_VSA_FULL utVsa
ON mvut.vsa_unit_id = utVsa.unit_id
WHERE 1             =1
