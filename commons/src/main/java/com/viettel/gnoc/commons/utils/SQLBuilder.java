package com.viettel.gnoc.commons.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author TungPV
 */
@Slf4j
public class SQLBuilder {

  public final static String SQL_MODULE_COMMON = "common";

  public final static String SQL_MODULE_EMPLOYEES = "employees";
  public final static String SQL_MODULE_ODTYPE = "odType";
  public final static String SQL_MODULE_OD = "od";
  public final static String SQL_MODULE_OD_CFG_SCHEDULE_CREATE = "odCfgScheduleCreate";
  public final static String SQL_MODULE_OD_FILE = "odCommonFile";
  public final static String SQL_MODULE_OD_CHANGE_STATUS = "odChangeStatus";
  public final static String SQL_MODULE_OD_CFG_BUSINESS = "odCfgBusiness";
  public final static String SQL_MODULE_BASE = "base";
  public static final String SQL_MODULE_PT_CFG_PROBLEM_TIME = "cfgProblemTime";
  public static final String SQL_MODULE_CAT_ITEM = "catItem";
  public static final String SQL_MODULE_WO_CD_TEMP = "woCdTemp";
  public static final String SQL_MODULE_CFG_BUSINESS_CALL_SMS = "cfgBusinessCallSms";
  public static final String SQL_MODULE_CFG_ROLE_DATA = "cfgRoleData";
  /*TuanDS add new PT*/
  public final static String SQL_MODULE_PT_PROBLEMS = "ptProblems";
  public final static String SQL_MODULE_KEDB = "kedb";
  public final static String SQL_MODULE_KEDB_FILES = "kedbFiles";
  public final static String SQL_MODULE_KEDB_RATING = "kedbRating";
  public final static String SQL_MODULE_PROBLEM_FILES = "problemFiles";
  public final static String SQL_MODULE_PROBLEM_CR = "problemCr";
  public final static String SQL_MODULE_PROBLEM_ACTION_LOGS = "problemActionLogs";
  public final static String SQL_MODULE_PROBLEM_NODE = "problemNode";
  public final static String SQL_MODULE_KEDB_ACTION_LOGS = "kedbActionLogs";
  public final static String SQL_MODULE_PT_PROBLEMS_CONFIG_TIME = "problemConfigTime";
  /*AnhLP add new PT, TT*/
  public final static String SQL_MODULE_PT_PROBLEMS_WORKLOG = "problemWorklog";
  public final static String SQL_MODULE_TT_CFG_SMS_OVERDUE = "cfgSmsGoingOverdue";
  public final static String SQL_MODULE_TT_CFG_INFO_SPM = "cfgInfoTtSpm";
  public final static String SQL_MODULE_TROUBLES_WORKLOG = "troublesWorklog";
  public final static String SQL_MODULE_TROUBLES_RELATED_TT = "relatedTT";
  public final static String SQL_MODULE_TROUBLES_PROBLEMS = "problems";
  public final static String SQL_MODULE_TROUBLES_RELATED_TT_POPUP = "troublesPopup";
  public static final String SQL_MODULE_TROUBLES_BRCD = "InfoBRCD";
  public static final String SQL_MODULE_TROUBLES_INFO_TICK_HELP = "infoTickHelp";
  public static final String SQL_MODULE_COMMON_GNOC_LANGUAGE = "gnocLanguage";
  public static final String SQL_MR_SCHEDULE_CD_HIS = "mrScheduleCdHis";
  public static final String SQL_MODULE_MR_SCHEDULE_BTS_HIS = "mrScheduleBtsHis";
  public static final String SQL_MODULE_MR_MATERIAL_DISPLACEMENT = "mrMaterialDisplacement";
  public static final String SQL_MODULE_MR_SCHEDULE_TEL = "mrScheduleTel";
  public static final String SQL_MODULE_MR_HARD_GROUP_CONFIG = "mrHardGroupConfig";
  public static final String SQL_MODULE_MR_HARD_UNIT_CONFIG = "mrHardUnitConfig";
  public static final String SQL_MODULE_MR_CFG_MARKET = "mrCfgMarket";
  public static final String SQL_MODULE_MR_SOFT_CNTT_CAT_MARKET = "mrSoftCNTTCatMarket";
  public static final String SQL_MODULE_MR_IMPACT_NODES = "mrImpactedNodes";
  public final static String SQL_MODULE_TT_CFG_BUSINESS = "TTCfgBusiness";
  public final static String SQL_MODULE_TT_CHANGE_STATUS_BUSINESS = "TTChangeStatus";
  public final static String SQL_MODULE_TT_CHANGE_STATUS_ROLE_BUSINESS = "TTChangeStatusRole";

  /*CuongTM add new PT*/
  public final static String SQL_MODULE_PROBLEMWO = "problemWo";
  public final static String SQL_MODULE_CFG_TIME_TROUBLE_PROCESS = "cfgTimeTroubleProcess";
  public final static String SQL_MODULE_TROUBLES = "troubles";
  public final static String SQL_MODULE_TROUBLES_IBM = "troublesIbm";
  public final static String SQL_MODULE_UNIT_IBM = "unitIbm";
  public final static String SQL_MODULE_PRODUCT_IBM = "productIbm";
  public final static String SQL_MODULE_TROUBLECARD = "troublesCard";
  public final static String SQL_MODULE_TROUBLES_SERVICE_FOR_CC = "troublesServiceForCC";
  public final static String SQL_MODULE_TROUBLES_NODE = "troubleNode";
  public final static String SQL_MODULE_TROUBLES_IT_ACCOUNT = "it_account";

  public final static String SQL_MODULE_CAT_REASON_PROCESS = "catReason";
  public static final String SQL_MODULE_CFG_MAP_NET_LEVEL_INC_TYPE = "cfg_map_net_level_inc_type";
  public final static String SQL_MODULE_CR = "cr";
  public final static String SQL_MODULE_CR_ALARM = "crAlarm";
  public final static String SQL_MODULE_WO = "wo";
  public final static String SQL_MODULE_WO_POST_INSPECTION = "woPostInspection";
  public final static String SQL_MODULE_WO_HISTORY = "woHistory";
  public final static String SQL_MODULE_WO_MATERIAL = "woMaterial";
  public final static String SQL_MODULE_WO_TYPE = "woType";
  public final static String SQL_MODULE_WO_PRIORITY = "woPriority";
  public final static String SQL_MODULE_WO_TYPE_CHECKLIST = "woTypeCheckList";
  public final static String SQL_MODULE_WO_TYPE_CFG_REQUIRED = "woTypeCfgRequired";
  public final static String SQL_MODULE_CFG_FILE_CREATE_WO = "cfgFileCreateWo";
  public final static String SQL_MODULE_CFG_MAP_UNIT_GNOC_NIMS = "cfgMapUnitGnocNims";
  public static final String SQL_MODULE_CR_EMPLOYEE_IMPACT = "employeeImpact";
  public static final String SQL_MODULE_CR_REQUEST_SCHEDULE = "requestSchedule";
  public final static String SQL_MODULE_WO_TYPE_FILES_GUIDE = "woTypeFilesGuide";
  public final static String SQL_MODULE_CFG_SUPPORT_CASE = "cfgSupportCase";
  public final static String SQL_MODULE_WO_CD_GROUP = "woCdGroup";
  public final static String SQL_MODULE_WO_CD = "woCd";
  public final static String SQL_MODULE_MAP_PROVINCE_CD = "mapProvinceCd";
  public final static String SQL_MODULE_WO_CD_GROUP_UNIT = "woCdGroupUnit";
  public final static String SQL_MODULE_WO_UPDATE_SERVICE_INFRA = "woUpdateServiceInfra";
  public static final String SQL_MODULE_CR_EMPLOYEE_DAYOFF = "employeeDayOff";
  public static final String SQL_MODULE_WO_TYPE_GROUP = "woTypeGroup";
  public static final String SQL_MODULE_CR_SHIFT_HANDOVER = "shiftHandOver";
  public static final String SQL_MODULE_CR_ACTION_CODE = "crActionCode";
  public static final String SQL_MODULE_CFG_CHILD_ARRAY = "cfgChildArray";
  public static final String SQL_MODULE_CR_IMPACT_FRAME = "crImpactFrame";
  public static final String SQL_MODULE_CR_RETURN_CODE_CATALOG = "returnCodeCatalog";
  public static final String SQL_MODULE_CR_TEMP_IMPORT_COL = "tempImportCol";
  public static final String SQL_MODULE_CR_TEMP_IMPORT = "tempImport";
  public static final String SQL_MODULE_CR_TEMP_IMPORT_DATA = "tempImportData";
  public static final String SQL_MODULE_CR_TEMP_RELATIONS = "tempRelations";
  public static final String SQL_MODULE_CR_WEBSERVICE = "webService";
  public static final String SQL_MODULE_CR_WEBSERVICE_METHOD = "webServiceMethod";
  public static final String SQL_MODULE_CR_WORKLOG_CATEGORY = "workLogCategory";
  public static final String SQL_MODULE_CR_PROCESS_WO = "crProcess";
  public static final String SQL_MODULE_CR_DETAIL = "crDetail";
  public static final String SQL_MODULE_CR_VMSA_UPDATE_MOP = "crVmsaUpdateMop";
  public static final String SQL_MODULE_CR_GENERAL = "crGeneral";
  public static final String SQL_MODULE_CR_DB_DAO = "crDbDao";
  public static final String SQL_MODULE_CR_SMS = "crSms";
  public static final String SQL_MODULE_CR_SHIFT_HANDOVER_FILE = "shiftHandoverFile";
  public static final String SQL_MODULE_CR_SHIFT_STAFT = "shiftStaft";
  public static final String SQL_MODULE_CR_SHIFT_WORK = "shiftWork";
  public static final String SQL_MODULE_CR_SHIFT_CR = "shiftCr";
  public static final String SQL_MODULE_CR_SHIFT_IT_SERIOUS = "shiftItSerious";
  public static final String SQL_MODULE_CR_SHIFT_IT = "shiftIt";
  public static final String SQL_MODULE_CR_SHIFT_WORK_OTHER = "shiftWorkOther";
  public final static String SQL_MODULE_CFG_WO_HIGH_TEMP = "cfgWoHighTemp";

  public static final String SQL_MODULE_WO_CHECKLIST_DETAIL = "woChecklistDetail";
  public static final String SQL_MODULE_WO_WORKLOG = "woWorklog";
  public final static String SQL_MODULE_COMP_CAUSE = "compCause";
  public final static String SQL_MODULE_WO_MERCHANDISE = "woMerchandise";
  public final static String SQL_MODULE_WO_MATERIAL_DEDUCTE = "woMaterialDeducte";
  public final static String SQL_MODULE_WO_AUTO_CHECK = "woAutoCheck";
  public final static String SQL_MODULE_MATERIAL_THRES = "materialThres";
  public final static String SQL_MODULE_LOG_CALL_IPCC = "logCallIpcc";
  public final static String SQL_MODULE_WO_SUPPORT = "woSupport";
  public final static String SQL_MODULE_WO_TYPE_SERVICE = "woTypeService";
  public static final String SQL_MODULE_WO_UNIT = "unit";
  public static final String SQL_MODULE_WO_SMS = "smsGateway";

  public final static String SQL_MODULE_RISK = "risk";
  public final static String SQL_MODULE_RISK_HISTORY = "riskHistory";
  public final static String SQL_MODULE_RISK_RELATION = "riskRelation";
  public final static String SQL_MODULE_RISK_SYSTEM = "riskSystem";
  public final static String SQL_MODULE_RISK_SYSTEM_DETAIL = "riskSystemDetail";
  public final static String SQL_MODULE_RISK_SYSTEM_HISTORY = "riskSystemHistory";
  public final static String SQL_MODULE_RISK_TYPE = "riskType";
  public final static String SQL_MODULE_RISK_TYPE_DETAIL = "riskTypeDetail";
  public final static String SQL_MODULE_RISK_CHANGE_STATUS = "riskChangeStatus";
  public final static String SQL_MODULE_RISK_CHANGE_STATUS_ROLE = "riskChangeStatusRole";

  /**
   * cr-category-sql
   **/
  public static final String SQL_MODULE_CR_PROCESS_MANAGER = "crProcessManager";
  public static final String SQL_MODULE_CR_Affected_Level = "CrAffectedLevel";
  public static final String SQL_MODULE_CR_Impact_Segment = "ImpactSegment";
  public static final String SQL_MODULE_Cr_Manager_Scopes_Of_Roles = "CrManagerScopesOfRoles";
  public static final String SQL_MODULE_Cr_Manager_Units_Of_Scope = "crManagerUnitsOfScope";
  public static final String SQL_MODULE_Cr_Manager_Roles = "CrManagerRoles";
  public static final String SQL_MODULE_CR_DEVICE_TYPES = "deviceType";
  public final static String SQL_MODULE_CR_IMPACTED_NODES = "crImpactedNodes";
  /**
   * duongnt
   **/
  public static final String SQL_MODULE_CR_APPROVAL_DEPARTMENT = "crApprovalDepartment";
  public static final String SQL_MODULE_CR_HIS = "crHis";
  public static final String SQL_MODULE_INFRA_DEVICE = "infraDevice";
  public static final String SQL_MODULE_CR_FILE_ATTACH = "crFileAttach";
  public static final String SQL_MODULE_TEMPLATE = "template";

  public static final String SQL_MODULE_CrForNocPro = "CrForNocPro";
  public static final String SQL_MODULE_CR_GENERAL_FOR_MOBILE = "crGeneralForMobile";
  public static final String SQL_MODULE_USER_IMPACT_SEGMENT = "userImpactSegmentOfCr";
  public static final String SQL_MODULE_WO_FILE_TEMP = "woFileTemp";
  public static final String SQL_MODULE_Cat_Cfg_Closed_Ticket = "catCfgClosedTicket";
  public static final String SQL_MODULE_CFG_REQUIRE_HAVE_WO = "cfgRequireHaveWo";
  public static final String SQL_MODULE_LANGUAGE_EXCHANGE = "languageExchange";
  public static final String SQL_MODULE_WO_TEST_SERVICE_CONF = "woTestServiceConf";
  public static final String SQL_MODULE_MAP_PROB_TO_KEDB = "mapProbToKedb";

  /**
   * TrungDuong add new coordinationSetting
   */
  public static final String SQL_MODULE_CR_COORDINATION_SETTING = "coordinationSetting";
  public static final String SQL_MODULE_CFG_FOLLOW_WORK_TIME = "cfgFollowWorkTime";


  /**
   * sr-category-sql
   **/
  public static final String SQL_MODULE_SR_CHILD_AUTO = "SRChildAuto";
  public static final String SQL_MODULE_SR = "SR";
  public static final String SQL_MODULE_SR_ROLE_USER = "srRoleUser";
  public static final String SQL_MODULE_SR_SERVICE = "srService";
  public static final String SQL_MODULE_SR_STATUS = "srStatus";
  public static final String SQL_MODULE_SR_ROLE = "srRole";
  public static final String SQL_MODULE_SR_FLOW_EXECUTE = "srFlowExecute";
  public static final String SQL_MODULE_SR_ROLE_ACTIONS = "srRoleActions";

  public static final String SQL_MODULE_SR_CATALOG = "srCatalog";
  public static final String SQL_MODULE_SR_CATALOG_CHILD = "srCatalogChild";
  public static final String SQL_MODULE_SR_MAPPING = "SRMappingProcessCR";
  public static final String SQL_MODULE_SR_CONFIG = "srConfig";
  public static final String SQL_MODULE_SR_HIS = "srHis";
  public static final String SQL_MODULE_SR_FILES = "srFiles";
  public static final String SQL_MODULE_SR_WORKLOG = "srWorkLog";
  public static final String SQL_MODULE_SR_MOP = "srMop";
  public static final String SQL_MODULE_SR_EVALUATE = "srEvaluate";
  public static final String SQL_MODULE_SR_AOM = "srAom";

  /**
   * mr-service
   */
  public static final String SQL_MODULE_MR_DEVICE_CD = "deviceCD";
  public static final String SQL_MODULE_MR_CFG_PROCEDURE_CD = "cfgProcedureCD";
  public static final String SQL_MODULE_MR_CD_WORKITEM = "mrCDWorkItem";
  public static final String SQL_MODULE_MR_HARD_CD = "mrHardCD";
  public static final String SQL_MODULE_MR_SCHEDULE_CD = "scheduleCD";
  public static final String SQL_MODULE_MR_DEVICE_BTS = "mrDeviceBts";
  public static final String SQL_MODULE_MR_SCHEDULE_BTS = "mrScheduleBts";
  public static final String SQL_SEARCH_DEVICE_ON_NIMS = "searchDeviceNIMS";
  public static final String SQL_MR_SERVICE = "mrService";
  public static final String SQL_MR_MAINTENANCE_MNGT = "maintenanceMNGT";
  public static final String SQL_MODULE_MR_CHECKLIST_BTS = "mrChecklistBts";
  public static final String SQL_MODULE_MR_CHECKLIST_BTS_DETAIL = "mrChecklistBtsDetail";
  public static final String SQL_MODULE_MR_CHECKLIST = "mrCheckList";
  public static final String SQL_MODULE_MR_HARD_DEVICES_CHECKLIST = "mrHardDevicesCheckList";
  public static final String SQL_MODULE_MR_CFG_PROCEDURE_BTS = "cfgProcedureBts";
  public static final String SQL_MODULE_MR_SCHEDULE_TEL_HIS = "mrScheduleTelHis";
  public static final String SQL_MODULE_MR_SCHEDULE_TEL_HIS_SOFT = "mrScheduleTelHisSoft";
  public static final String SQL_MR_UCTT = "mrUctt";
  public static final String SQL_MODULE_MR_DEVICE = "mrDevice";
  public static final String SQL_MODULE_MR_CFG_PROCEDURE_TEL = "cfgProcedureTel";
  public static final String SQL_MODULE_MR_CFG_CR_UNIT_TEL = "mrCfgCrUnitTel";
  public static final String MR_APPROVAL_DEPARTMENT = "mrApprovalDepartment";
  public static final String SQL_MR_IMPACTED_NODES = "mrImpactedNodes";
  public static final String SQL_MODULE_MR_DEVICE_SOFT = "mrDeviceSoft";
  public static final String SQL_MODULE_MR_CONFIG = "mrConfig";
  public static final String MR_FILES_ATTACH = "mrFilesAttach";
  public static final String MR_HIS_SEARCH = "mrHisSearch";
  public static final String SQL_MODULE_MR_SCHEDULE_IT_SOFT = "mrScheduleITSoft";
  public static final String SQL_MODULE_MR_SCHEDULE_IT_HARD = "mrScheduleITHard";
  public static final String SQL_MODULE_MR_PROCEDURE_IT_SOFT = "mrITSoftProcedure";
  public static final String SQL_MODULE_MR_SYN_IT_DEVICES_SOFT = "mrSynITDevicesSoft";
  public static final String SQL_MODULE_MR_SYN_IT_DEVICES_HARD = "mrSynITDevicesHard";
  public static final String SQL_MODULE_MR_IT_SOFT_HIS = "mrItSoftHis";
  public static final String SQL_MODULE_MR_CFG_CR_UNIT_IT = "mrCfgCrUnitIT";
  public static final String SQL_MODULE_MR_NODE = "mrNode";
  public static final String SQL_MODULE_MR = "MR";
  public static final String SQL_MODULE_MR_CAUSE_WO_WAS_COMPLETED = "mrCauseWoWasCompleted";

  //dunglv them man testxa
  public static final String SQL_MODULE_MR_CD_BATTERY = "mrCdBattery";

  //tiennv them cau hinh ft truc ca WO
  public static final String SQL_MODULE_WO_CFG_FT_ONTIME = "cfgFtOnTime";

  //tripm them man config test xa
  public static final String SQL_MR_CONFIG_TESTXA = "mrConfigTestXa";

  //TrungDuong nang cap Mr Co dien BTS
  public static final String SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS = "mrUserCfgApprovedSmsBts";

  @Autowired
  static ResourceLoader resourceLoader;

  public static String getSqlQueryById(String module, String queryId) {
    InputStream inputStream = null;
    try {
      String filePath = "sql" + File.separator + module + File.separator + queryId + ".sql";
      log.info("SQL file path:" + filePath);
      Resource resource = new ClassPathResource(filePath);
      inputStream = resource.getInputStream();
      if (inputStream != null) {
        return new String(inputStream.readAllBytes());
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return null;
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return null;
  }

  public static String getSQLPagination(String sql) {
    sql = "\n SELECT * FROM ( SELECT a.*, rownum indexRow FROM ( "
        + "\n SELECT * FROM ( "
        + sql
        + "\n )) a"
        + "\n WHERE rownum < :indexEnd + 1) WHERE indexRow > :indextStart ";
    return sql;
  }

}
