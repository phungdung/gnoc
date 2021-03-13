SELECT  a.wo_type_checklist_id woTypeChecklistId,
        a.wo_type_id woTypeId,
        a.checklist_name checklistName,
        a.is_enable isEnable,
        a.default_value defaultValue
FROM    wo_type_checklist a
WHERE   1 = 1
