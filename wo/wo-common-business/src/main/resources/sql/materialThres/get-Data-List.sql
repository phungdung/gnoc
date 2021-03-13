select  b.material_code materialCode,
        b.material_name materialName,
        d.item_code actionCode,
        d.item_name actionName,
        e.service_name serviceName,
        e.service_code serviceCode,
        a.material_thres_id materialThresId,
        a.tech_thres techThres,
        a.warning_thres warningThres,
        a.free_thres freeThres,
        a.material_id materialId,
        a.action_id actionId,
        a.service_id serviceId,
        decode(a.tech_distanct_thres,null,'','A/' || a.tech_distanct_thres) techDistanctThres,
        decode(a.warning_distanct_thres,null,'','A/' || a.warning_distanct_thres) warningDistanctThres,
        decode(a.free_distanct_thres,null,'','A/' || a.free_distanct_thres) freeDistanctThres,
        a.infra_type infraType,
        decode(a.infra_type,1,:copperCable,2,:coaxialCable,3,'AON',4,'GPON','N/A') technology
from    MATERIAL_THRES a,
        wo_material b,
        COMMON_GNOC.CAT_ITEM d,
        common_gnoc.cat_service e
where   d.category_id = (select category_id from common_gnoc.category where category_code = :categoryCode)
and     a.action_id = d.item_id(+)
and     a.service_id = e.service_id(+)
and     a.material_id = b.material_id(+)
and     a.is_enable = 1
