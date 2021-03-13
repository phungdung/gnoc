SELECT c.WO_TYPE_CHECKLIST_ID woTypeChecklistId
	, c.WO_TYPE_ID woTypeId
	, c.CHECKLIST_NAME checklistName
	, c.DEFAULT_VALUE defaultValue
	, c.IS_ENABLE isEnable
	FROM WFM.WO_TYPE_CHECKLIST c
	WHERE
	c.IS_ENABLE = 1
	AND 1=1
