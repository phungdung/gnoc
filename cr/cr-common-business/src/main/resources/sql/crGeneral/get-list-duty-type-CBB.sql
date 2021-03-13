select cife.ife_id valueStr,
       ( nvl(le.LEE_VALUE,cife.ife_name) ||
         ' [' || cife.start_time || ' - ' || cife.end_time || ']') displayStr,
       (cife.start_time || ',' || cife.end_time) secondValue
from open_pm.cr_impact_frame cife
       left join common_gnoc.language_exchange le
         on cife.ife_id=le.BUSSINESS_ID
              and le.applied_system = 2
              and le.applied_bussiness = 4
              and le.lee_locale = :lee_locale
where 1=1 AND NVL(cife.IS_ACTIVE,1) = 1
