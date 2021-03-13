SELECT DISTINCT dev.region_hard region
FROM MR_SYN_IT_DEVICES dev
WHERE DEV.MARKET_CODE = :p_country
AND dev.region_hard  IS NOT NULL
