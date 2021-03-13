SELECT
  a.wo_id woId,
  b.wo_type_id woTypeId,
  b.wo_type_checklist_id woTypeChecklistId,
  b.checklist_name checklistName,
  b.default_value defaultValue,
  c.wo_checklist_detail_id woChecklistDetailId,
  c.checklist_value checklistValue
FROM wo a
  inner join wo_type_checklist b on a.wo_type_id=b.wo_type_id
  left join wo_checklist_detail c
  on b.wo_type_checklist_id=c.wo_type_checklist_id and a.wo_id=c.wo_id
where  b.is_enable=1  and a.wo_id = :woId
