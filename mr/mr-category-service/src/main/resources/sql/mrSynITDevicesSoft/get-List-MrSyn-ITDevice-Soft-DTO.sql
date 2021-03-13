SELECT
  T1.ID id,
  T1.OBJECT_ID objectId ,
  CASE
    WHEN T2.COUNTRY IS NOT NULL
    THEN T2.COUNTRY
    ELSE ''
  END marketCode,
  T1.REGION_SOFT regionSoft,
  T1.REGION_HARD regionHard,
  T1.CREATE_USER_SOFT createUserSoft,
  T1.CREATE_USER_HARD createUserHard,
  T1.IP_NODE ipNode,
  T1.NODE_AFFECTED nodeAffected,
  T1.CD_ID cdId,
  T1.MR_HARD mrHard,
  T1.MR_SOFT mrSoft,
  T1.ARRAY_CODE arrayCode,
  T1.DEVICE_TYPE deviceType,
  T1.GROUP_CODE groupCode,
  TO_CHAR(T1.UPDATE_DATE, 'dd/MM/yyyy') updateDate,
  T1.UPDATE_USER updateUser,
  T1.OBJECT_NAME objectName,
  T1.OBJECT_CODE objectCode,
  T1.USER_MR_HARD userMrHard,
  T1.IS_COMPLETE_1M isComplete1m,
  T1.IS_COMPLETE_3M isComplete3m,
  T1.IS_COMPLETE_6M isComplete6m,
  T1.IS_COMPLETE_12M isComplete12m,
  T1.IS_COMPLETE_SOFT isCompleteSoft,
  TO_CHAR(T1.LAST_DATE_SOFT, 'dd/MM/yyyy') lastDateSoft,
  TO_CHAR(T1.LAST_DATE_1M, 'dd/MM/yyyy') lastDate1m,
  TO_CHAR(T1.LAST_DATE_3M, 'dd/MM/yyyy') lastDate3m,
  TO_CHAR(T1.LAST_DATE_6M, 'dd/MM/yyyy') lastDate6m,
  TO_CHAR(T1.LAST_DATE_12M, 'dd/MM/yyyy') lastDate12m,
  T1.VENDOR vendor,
  T1.MR_CONFIRM_HARD mrConfirmHard,
  T1.MR_CONFIRM_SOFT mrConfirmSoft,
  T1.STATUS status,
  T1.NOTES notes,
  TO_CHAR(T1.SYN_DATE,'dd/MM/yyyy') synDate,
  T1.DB db,
  T1.UD ud,
  T1.STATION station,
  T1.LEVEL_IMPORTANT levelImportant,
  T1.BO_UNIT boUnit,
  T1.APPROVE_STATUS approveStatus,
  T1.APPROVE_REASON approveReason,
  T1.UP_TIME upTime,
  CASE
    WHEN
      (
        T1.BO_UNIT          = role.unit_id
      AND T1.APPROVE_STATUS = 0
      )
    THEN 1
    ELSE 0
  END isApproveAble,
  T3.IMPLEMENT_UNIT implementUnit,
  T3.CHECKING_UNIT checkingUnit,
  T4.UNIT_NAME implementUnitName,
  T5.UNIT_NAME checkingUnitName
FROM
  OPEN_PM.MR_SYN_IT_DEVICES T1
LEFT JOIN OPEN_PM.MR_CAT_MARKET T2
ON
  T1.MARKET_CODE = T2.MARKET_CODE
LEFT JOIN OPEN_PM.MR_CFG_CR_IMPL_UNIT T3
ON
  T1.DEVICE_TYPE = T3.DEVICE_TYPE_ID
AND T2.COUNTRY   = T3.MARKET_CODE
AND
  (
    (
      CASE {soft} {hard}
      END
    )
    = 1
  )
LEFT JOIN COMMON_GNOC.UNIT T4
ON
  T3.IMPLEMENT_UNIT = T4.UNIT_ID
LEFT JOIN COMMON_GNOC.UNIT T5
ON
  T3.CHECKING_UNIT = T5.UNIT_ID
LEFT JOIN
  (
    SELECT
      username,
      b1.unit_id
    FROM
      (
        SELECT
          username,
          user_id,
          unit_id
        FROM
          COMMON_GNOC.USERS
        WHERE
          USERNAME  = :userLogin
        AND unit_id = :unitLogin
      )
      b1
    JOIN COMMON_GNOC.UNIT b4
    ON
      b1.UNIT_ID        = b4.UNIT_ID
    AND b4.IS_COMMITTEE = 0
    JOIN COMMON_GNOC.ROLE_USER b2
    ON
      b1.user_id = b2.USER_ID
    JOIN COMMON_GNOC.ROLES b3
    ON
      b2.ROLE_ID     = b3.ROLE_ID
    AND b3.ROLE_CODE = 'TP'
  )
  role ON t1.BO_UNIT = role.unit_id
  WHERE 1 = 1
