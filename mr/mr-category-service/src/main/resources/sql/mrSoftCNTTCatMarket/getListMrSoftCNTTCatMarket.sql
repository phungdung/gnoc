SELECT
a.MARKET_CODE marketCode,
a.MARKET_NAME marketName,
a.COUNTRY countryCode,
b.LOCATION_NAME countryName
FROM MR_CAT_MARKET a
left join COMMON_GNOC.CAT_LOCATION b on a.COUNTRY = to_char(b.LOCATION_ID)
where 1= 1
