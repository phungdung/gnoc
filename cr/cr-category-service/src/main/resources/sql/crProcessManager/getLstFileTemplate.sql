SELECT
  c.temp_import_id tempImportId,
  c.code code,
  c.name name,
  c.title title,
  c.is_active isActive
FROM open_pm.temp_import c
WHERE 1 = 1 and c.is_active = 1
