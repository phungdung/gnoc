SELECT  m.CONFIG_GROUP configGroup,
        m.CONFIG_CODE configCode,
        m.CONFIG_NAME configName,
        m.COUNTRY country,
        m.CONFIG_VALUE configValue
FROM    MR_CONFIG m
WHERE   m.CONFIG_GROUP = :configGroup
ORDER BY m.CONFIG_CODE
