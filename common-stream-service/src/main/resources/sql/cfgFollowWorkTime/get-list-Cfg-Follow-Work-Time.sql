SELECT a.CONFIG_FOLLOW_WORKTIME_ID as configFollowWorkTimeId,
A.SYSTEM as system,
A.CATEGORY_ID as categoryId,
A.CAT_ITEM_ID as catItemId,
A.STEP_TIME as stepTime,
A.CATEGORY_NAME as categoryName,
A.CAT_ITEM_NAME as catItemName,
A.CR_TYPE as crType,
A.TT_ACTION_CLASS as ttActionClass
FROM OPEN_PM.CONFIG_FOLLOW_WORKTIME  a
Where 1 = 1
