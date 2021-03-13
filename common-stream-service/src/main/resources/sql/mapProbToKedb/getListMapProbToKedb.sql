select
a.id,
a.PROB_TYPE_ID_LV1 probTypeIdLv1,
a.PROB_TYPE_NAME_LV1 probTypeNameLv1,
a.PROB_TYPE_ID_LV2 probTypeIdLv2,
a.PROB_TYPE_NAME_LV2 probTypeNameLv2,
a.PROB_TYPE_ID_LV3 probTypeIdLv3,
a.PROB_TYPE_NAME_LV3 probTypeNameLv3,
a.KEDB_CODE kedbCode
from COMMON_GNOC.MAP_PROB_TO_KEDB a
where 1=1
