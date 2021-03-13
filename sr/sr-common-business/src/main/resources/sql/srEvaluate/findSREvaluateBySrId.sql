SELECT
  a.EVALUATE_ID evaluateId,
  d.config_name review,
  a.evaluate evaluate,
  a.evaluate_reason evaluateReason,
  a.create_date createdTime,
  a.create_user createdUser,
  c.sr_id srId
FROM OPEN_PM.SR_EVALUATE a
LEFT JOIN OPEN_PM.sr c
ON a.sr_id = c.sr_id
LEFT JOIN OPEN_PM.sr_config d
ON c.review_id     = d.config_code
AND d.config_group = 'REVIEW_CLOSE_SR'
WHERE c.sr_id      = :srId
