select
COMP_CAUSE_ID compCauseId,
name name,
CODE code,
DESCRIPTION description,
ISACTIVE isactive,
SERVICE_TYPE serviceType,
PARENT_ID parentId,
LEVEL_ID levelId,
CFG_TYPE cfgType,
ERROR_TYPE errorType,
LINE_TYPE lineType,
IS_SIGNAL isSignal
from COMMON_GNOC.COMP_CAUSE
WHERE
1 = 1
