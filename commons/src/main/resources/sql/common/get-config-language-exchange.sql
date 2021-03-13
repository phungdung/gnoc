select
CI1.ITEM_CODE itemCode,
CI1.ITEM_VALUE itemValue
from COMMON_GNOC.CAT_ITEM CI1
where CI1.CATEGORY_ID = 263 and CI1.ITEM_CODE = :p_system
union
select
CI2.ITEM_CODE itemCode,
CI2.ITEM_VALUE itemValue
from COMMON_GNOC.CAT_ITEM CI2
where CI2.CATEGORY_ID = 262 and CI2.ITEM_CODE = :p_bussiness
