SELECT a.cr_process_id crProcessId,
  a.cr_process_code crProcessCode,
  a.cr_process_name crProcessName,
  a.description description,
  a.impact_segment_id impactSegmentId,
  a.device_type_id deviceTypeId,
  a.subcategory_id subcategoryId,
  a.risk_level riskLevel,
  a.impact_type impactType,
  a.cr_type_id crTypeId,
  a.is_active isActive,
  a.parent_id parentId,
  a.impact_characteristic impactCharacteristic
FROM open_pm.cr_process a
WHERE a.parent_id IS NULL
ORDER BY a.cr_process_name
