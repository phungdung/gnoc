select  cfg.UNIT_NIMS_CODE unitNimsCode, cfg.UNIT_GNOC_CODE unitGnocCode,
cfg.BUSINESS_CODE businessCode, cfg.BUSINESS_NAME businessName  from CFG_MAP_UNIT_GNOC_NIMS cfg
where lower(cfg.UNIT_NIMS_CODE) = lower(:p_unitNimsCode) and
cfg.BUSINESS_NAME is null or cfg.BUSINESS_NAME =:p_businessName
