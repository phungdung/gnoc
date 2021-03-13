SELECT a.tida_id tidaId,
       a.temp_import_id tempImportId,
       a.temp_import_col_id tempImportColId,
       a.temp_import_value tempImportValue,
       a.cr_id crId,
       a.row_order rowOrder
FROM temp_import_data a
WHERE 1=1 
