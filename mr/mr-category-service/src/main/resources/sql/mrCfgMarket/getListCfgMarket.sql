SELECT t1.ID_MARKET idMarket,
  t1.MARKET_CODE marketCode,
  t1.MARKET_NAME marketName,
  t1.CREATED_USER_SOFT createdUserSoft,
  t1.UPDATED_USER updatedUser,
  t1.UPDATED_TIME updatedTime,
  t1.CREATED_USER_HARD createdUserHard,
  t1.CREATED_USER_IT_SOFT createdUserItSoft,
  t1.CREATED_USER_IT_HARD createdUserItHard
FROM MR_CFG_MARKET t1
WHERE 1 = 1
