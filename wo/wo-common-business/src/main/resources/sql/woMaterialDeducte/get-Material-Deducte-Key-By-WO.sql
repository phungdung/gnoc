select  user_id userId,
        user_name userName,
        wo_id woId,
        d.quantity quantity,
        m.material_id materialId,
        m.material_name  materialName,
        m.material_Group_Code materialGroupCode,
        a.item_id actionId ,
        a.parent_item_id parentActionId
from    wfm.wo_material_deducte d,
        wfm.wo_material m,
        common_gnoc.cat_item a
where   d.action_id = a.item_id
and     d.material_id = m.material_id
and     d.wo_id = :woId
