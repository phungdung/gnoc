SELECT    ROUND(SUM(kr.POINT)/COUNT(*), 2) averagePoint,
          count(*) numComent,
          kr.KEDB_ID kedbId
FROM      ONE_TM.KEDB_RATING kr
WHERE     kr.KEDB_ID = :kedbId
GROUP BY  kr.KEDB_ID
