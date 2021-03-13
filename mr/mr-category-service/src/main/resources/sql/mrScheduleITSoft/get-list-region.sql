SELECT DISTINCT dev.region_soft region
FROM MR_SYN_IT_DEVICES dev
WHERE DEV.MARKET_CODE =:p_country
AND dev.region_soft  IS NOT NULL
