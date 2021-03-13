package com.viettel.gnoc.commons.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.viettel.gnoc.commons.config.I18n;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TungPV
 */
public final class Constants {

  public static final String COMMON_STREAM_API_PATH_PREFIX = "/";

  public static final String OD_API_PATH_PREFIX = "/";
  public static final String TT_API_PATH_PREFIX = "/";
  public static final String PT_API_PATH_PREFIX = "/";
  public static final String KEDB_API_PATH_PREFIX = "/";
  public static final String WO_API_PATH_PREFIX = "/";
  public static final String CR_API_PATH_PREFIX = "/";
  public static final String SR_API_PATH_PREFIX = "/";
  public static final String MR_API_PATH_PREFIX = "/";
  public static final String RISK_API_PATH_PREFIX = "/";

  public static final String formatterDateText = "dd/MM/yyyy";
  public static final String formatterDateTimeText = "dd/MM/yyyy HH:mm:ss";
  public static final String ddMMyyyyHHmmss = "dd/MM/yyyy HH:mm:ss";
  public static final String ddMMyyyy = "dd/MM/yyyy";

  public static final String ITEM_ID = "itemId";
  public static final String ITEM_NAME = "itemName";
  public static final String ITEM_VALUE = "itemValue";
  public static final String ITEM_CODE = "itemCode";
  public static final String RESULT_IMPORT = "RESULT_IMPORT";
  public static final String RESULT_EXPORT = "RESULT_EXPORT";
  public final static String FILE_NULL = "FILE_NULL";

  public static final String GNOC_ALIAS = "GNOC_ALIAS";
  public static final String PT_ALIAS = "PT_ALIAS";
  public static final String GNOC_LANGUAGE = "GNOC_LANGUAGE";
  public static final String PHOTO_REQ_YES = "1";
  public static final String PHOTO_REQ_NO = "0";

  //ca truc
  public static final String GNOC_SHIFT = "GNOC_SHIFT";
  //kpi
  public static final String GNOC_SHIFT_KPI = "GNOC_SHIFT_KPI";

  public static class MR_STATE_NAME_OLD {

    public final static String OPEN = "OPEN";
    public final static String INACTIVE_WAITTING = "INACTIVE_WAITTING";
    public final static String QUEUE = "QUEUE";
    public final static String ACTIVE = "ACTIVE";
    public final static String INACTIVE = "INACTIVE";
    public final static String CLOSE = "CLOSE";
    public final static String REJECT = "REJECT";
    public final static String INCOMPLETE = "INCOMPLETE";
    public final static String CLOSE7 = "CLOSE_PART_COMPLETE";
    public final static String CLOSE8 = "CLOSE_ROLLBACK";
    public final static String CLOSE9 = "CLOSE_FAIL_APPROVE";
    public final static String CLOSE10 = "CLOSE_MR_FAIL_ASSIGNCAB";
    public final static String CLOSE11 = "CLOSE_MR_FAIL_QUEUE";
    public final static String CLOSE12 = "CLOSE_MR_FAIL_SCHEDULE";
    public final static String CLOSE13 = "CLOSE_MR_FAIL_RECEIVE";
    public final static String CLOSE14 = "CLOSE_MR_MISSING";
    public final static String CLOSE15 = "CLOSE_MR_APPROVED";


    private static Map<String, String> getText = new HashMap<>() {
      {
        put(OPEN, I18n.getLanguage("mrMngt.state.open"));
        put(INACTIVE_WAITTING, I18n.getLanguage("mrMngt.state.inactive_waitting"));
        put(QUEUE, I18n.getLanguage("mrMngt.state.queue"));
        put(ACTIVE, I18n.getLanguage("mrMngt.state.active"));
        put(INACTIVE, I18n.getLanguage("mrMngt.state.inactive"));
        put(CLOSE, I18n.getLanguage("mrMngt.state.close"));
        put(CLOSE7, I18n.getLanguage("mrMngt.state.close7"));
        put(CLOSE8, I18n.getLanguage("mrMngt.state.close8"));
        put(CLOSE9, I18n.getLanguage("mrMngt.state.close9"));
        put(CLOSE10, I18n.getLanguage("mrMngt.state.close10"));
        put(CLOSE11, I18n.getLanguage("mrMngt.state.close11"));
        put(CLOSE12, I18n.getLanguage("mrMngt.state.close12"));
        put(CLOSE13, I18n.getLanguage("mrMngt.state.close13"));
        put(CLOSE14, I18n.getLanguage("mrMngt.state.close14"));
        put(CLOSE15, I18n.getLanguage("mrMngt.state.close15"));
      }
    };
    private static Map<String, String> getCode = new HashMap<>() {
      {
        put(I18n.getLanguage("mrMngt.state.open"), OPEN);
        put(I18n.getLanguage("mrMngt.state.inactive_waitting"), INACTIVE_WAITTING);
        put(I18n.getLanguage("mrMngt.state.queue"), QUEUE);
        put(I18n.getLanguage("mrMngt.state.active"), ACTIVE);
        put(I18n.getLanguage("mrMngt.state.inactive"), INACTIVE);
        put(I18n.getLanguage("mrMngt.state.close"), CLOSE);
        put(I18n.getLanguage("mrMngt.state.close7"), CLOSE7);
        put(I18n.getLanguage("mrMngt.state.close8"), CLOSE8);
        put(I18n.getLanguage("mrMngt.state.close9"), CLOSE9);
        put(I18n.getLanguage("mrMngt.state.close10"), CLOSE10);
        put(I18n.getLanguage("mrMngt.state.close11"), CLOSE11);
        put(I18n.getLanguage("mrMngt.state.close12"), CLOSE12);
        put(I18n.getLanguage("mrMngt.state.close13"), CLOSE13);
        put(I18n.getLanguage("mrMngt.state.close14"), CLOSE14);
        put(I18n.getLanguage("mrMngt.state.close15"), CLOSE15);
      }
    };

    public static Map<String, String> getText() {
      return getText;
    }

    public static Map<String, String> getCode() {
      return getCode;
    }

  }

  public interface CATEGORY_NAME {

    public static final String WO_TYPE_GROUP = "WO_TYPE_GROUP";
    public static final String WO_SYSTEM = "WO_SYSTEM";
    public static final String WO_ACTION_GROUP = "WO_ACTION_GROUP";
    public static final String WO_MATERIAL_OVER_REASON = "WO_MATERIAL_OVER_REASON";
  }

  public static class USER_ACTION_LOG {

    public final static String WO_CD = "WO_CD";
    public final static String SR_ROLE_USER = "SR_ROLE_USER";
    public final static String CFG_SMS_GOING_OVERDUE = "CFG_SMS_GOING_OVERDUE";
    public final static String SHIFT_HANDOVER = "SHIFT_HANDOVER";
    public final static String EMPLOYEE_IMPACT = "EMPLOYEE_IMPACT";
    public final static String EMPLOYEE_DAYOFF = "EMPLOYEE_DAYOFF";
    public final static String CR_CAB_USERS = "CR_CAB_USERS";
    public final static String WO_CD_TEMP = "WO_CD_TEMP";
    public final static String CFG_ROLE_DATA = "CFG_ROLE_DATA";

    private static Map<String, String> getActionLogName = new HashMap<>() {
      {
        put(WO_CD, "users.actionLog.WO_CD");
        put(SR_ROLE_USER, "users.actionLog.SR_ROLE_USER");
        put(CFG_SMS_GOING_OVERDUE, "users.actionLog.CFG_SMS_GOING_OVERDUE");
        put(SHIFT_HANDOVER, "users.actionLog.SHIFT_HANDOVER");
        put(EMPLOYEE_IMPACT, "users.actionLog.EMPLOYEE_IMPACT");
        put(EMPLOYEE_DAYOFF, "users.actionLog.EMPLOYEE_DAYOFF");
        put(CR_CAB_USERS, "users.actionLog.CR_CAB_USERS");
        put(WO_CD_TEMP, "users.actionLog.WO_CD_TEMP");
        put(CFG_ROLE_DATA, "users.actionLog.CFG_ROLE_DATA");
      }
    };

    public static Map<String, String> getActionLogName() {
      return getActionLogName;
    }
  }

  public interface DATA_TYPE_LONG {

    public static final Long STRING = 1L;
    public static final Long FLOAT = 2L;
    public static final Long DOUBLE = 3L;
    public static final Long INTEGER = 4L;
    public static final Long LONG = 5L;
    public static final Long BOOLEAN = 6L;
    public static final Long DATE = 7L;
    public static final Long DATE_TIME = 8L;
    public static final Long BYTE_ARRAY = 9L;
  }

  public static class MR_STATE_CODE {

    public final static String OPEN = "1";
    public final static String INACTIVE_WAITTING = "2";
    public final static String QUEUE = "3";
    public final static String ACTIVE = "4";
    public final static String INACTIVE = "5";
    public final static String CLOSE = "6";
    public final static String CLOSE7 = "7";
    public final static String CLOSE8 = "8";
    public final static String CLOSE9 = "9";
    public final static String CLOSE10 = "10";
    public final static String CLOSE11 = "11";
    public final static String CLOSE12 = "12";
    public final static String CLOSE13 = "13";
    public final static String CLOSE14 = "14";
    public final static String CLOSE15 = "15";
    public final static String REJECT = "6";
    public final static String INCOMPLETE = "7";

    public final static String CLOSE_PART_COMPLETE = "7";
    public final static String CLOSE_ROLLBACK = "8";
    public final static String CLOSE_FAIL_APRROVE = "9";
    public final static String CLOSE_MR_FAIL_ASSIGNCAB = "10";
    public final static String CLOSE_MR_FAIL_QUEUE = "11";
    public final static String CLOSE_MR_FAIL_SCHEDULE = "12";
    public final static String CLOSE_MR_FAIL_RECEIVE = "13";
    public final static String CLOSE_MR_MISSING = "14";
    public final static String CLOSE_MR_APPROVED = "15";

    private static Map<String, String> getText = new HashMap<>() {
      {
        put(OPEN, I18n.getLanguage("mrMngt.state.open"));
        put(INACTIVE_WAITTING, I18n.getLanguage("mrMngt.state.inactive_waitting"));
        put(QUEUE, I18n.getLanguage("mrMngt.state.queue"));
        put(ACTIVE, I18n.getLanguage("mrMngt.state.active"));
        put(INACTIVE, I18n.getLanguage("mrMngt.state.inactive"));
        put(CLOSE, I18n.getLanguage("mrMngt.state.close"));
        put(CLOSE_PART_COMPLETE, I18n.getLanguage("mrMngt.state.close7"));
        put(CLOSE_ROLLBACK, I18n.getLanguage("mrMngt.state.close8"));
        put(CLOSE_FAIL_APRROVE, I18n.getLanguage("mrMngt.state.close9"));
        put(CLOSE_MR_FAIL_ASSIGNCAB, I18n.getLanguage("mrMngt.state.close10"));
        put(CLOSE_MR_FAIL_QUEUE, I18n.getLanguage("mrMngt.state.close11"));
        put(CLOSE_MR_FAIL_SCHEDULE, I18n.getLanguage("mrMngt.state.close12"));
        put(CLOSE_MR_FAIL_RECEIVE, I18n.getLanguage("mrMngt.state.close13"));
        put(CLOSE_MR_MISSING, I18n.getLanguage("mrMngt.state.close14"));
        put(CLOSE_MR_APPROVED, I18n.getLanguage("mrMngt.state.close15"));
      }
    };
    private static Map<String, String> getCode = new HashMap<>() {
      {
        put(I18n.getLanguage("mrMngt.state.open"), OPEN);
        put(I18n.getLanguage("mrMngt.state.inactive_waitting"), INACTIVE_WAITTING);
        put(I18n.getLanguage("mrMngt.state.queue"), QUEUE);
        put(I18n.getLanguage("mrMngt.state.active"), ACTIVE);
        put(I18n.getLanguage("mrMngt.state.inactive"), INACTIVE);
        put(I18n.getLanguage("mrMngt.state.close"), CLOSE);
        put(I18n.getLanguage("mrMngt.state.close7"), CLOSE_PART_COMPLETE);
        put(I18n.getLanguage("mrMngt.state.close8"), CLOSE_ROLLBACK);
        put(I18n.getLanguage("mrMngt.state.close9"), CLOSE_FAIL_APRROVE);
        put(I18n.getLanguage("mrMngt.state.close10"), CLOSE_MR_FAIL_ASSIGNCAB);
        put(I18n.getLanguage("mrMngt.state.close11"), CLOSE_MR_FAIL_QUEUE);
        put(I18n.getLanguage("mrMngt.state.close12"), CLOSE_MR_FAIL_SCHEDULE);
        put(I18n.getLanguage("mrMngt.state.close13"), CLOSE_MR_FAIL_RECEIVE);
        put(I18n.getLanguage("mrMngt.state.close14"), CLOSE_MR_MISSING);
        put(I18n.getLanguage("mrMngt.state.close15"), CLOSE_MR_APPROVED);
      }
    };

    public static Map<String, String> getGetText() {
      return getText;
    }

    public static Map<String, String> getGetCode() {
      return getCode;
    }
  }

  public interface TESTXA {

    public static final String ISWOACCUTRUE = "1";
    public static final String ISWOACCUFAIL = "0";
    public static final String ISWOACCUVALIDATE = "2";

    public static final String ISIMPORTFAIL = "0";
    public static final String ISIMPORTTRUE = "1";
  }

  public interface ROLE {

    public static final String GNOC_ROLE = "GNOC_ROLE";
    //  public static final String GNOC_OD_MANAGEMENT = "GNOC_OD_MANAGEMENT";
    public static final String OD_TYPE_MANAGEMENT = "OD_TYPE_MANAGEMENT";
    public static final String OD_SCHEDULE_CREATE = "OD_SCHEDULE_CREATE";
    public static final String OD_CONFIG_BUSINESS = "OD_CONFIG_BUSINESS";
    public static final String OD_WORKFLOW_MANAGEMENT = "OD_WORKFLOW_MANAGEMENT";
    //  public static final String GNOC_PT_MANAGEMENT = "GNOC_PT_MANAGEMENT";
    public static final String PT_PROBLEM = "PT_PROBLEM";
    public static final String PT_CONFIG_TIME_HANDLE_PROBLEM = "PT_CONFIG_TIME_HANDLE_PROBLEM";
    //  public static final String GNOC_UTILITY = "GNOC_UTILITY";
    public static final String UTILITY_VERSION_CATALOG = "UTILITY_VERSION_CATALOG";
    public static final String UTILITY_STATUS_CONFIG = "UTILITY_STATUS_CONFIG";
    public static final String UTILITY_LANGUAGE_CONFIG = "UTILITY_LANGUAGE_CONFIG";
    public static final String UTILITY_CONFIG_EMPLOYEE_IMPACT = "UTILITY_CONFIG_EMPLOYEE_IMPACT";
    public static final String UTILITY_CONFIG_REQUEST_SCHEDULE = "UTILITY_CONFIG_REQUEST_SCHEDULE";
    public static final String UTILITY_CONFIG_EMPLOYEE_DAY_OFF = "UTILITY_CONFIG_EMPLOYEE_DAY_OFF";
    public static final String UTILITY_CONFIG_SHIFT_HANDOVER = "UTILITY_CONFIG_SHIFT_HANDOVER";
    public static final String UTILITY_WORK_LOGS = "UTILITY_WORK_LOGS";
    public static final String UTILITY_WO_CD_TEMP = "UTILITY_WO_CD_TEMP";
    public static final String UTILITY_USER_PERSONAL = "UTILITY_USER_PERSONAL";
    public static final String UTILITY_CAT_ITEM = "UTILITY_CAT_ITEM";
    public static final String UTILITY_CFG_BUSINESS_CALL_SMS = "UTILITY_CFG_BUSINESS_CALL_SMS";
    public static final String UTILITY_WO_TEST_SERVICE = "UTILITY_WO_TEST_SERVICE";
    public static final String UTILITY_CATEGORY = "UTILITY_CATEGORY";
    public static final String UTILITY_UNIT = "UTILITY_UNIT";
    public static final String UTILITY_SMS_GATEWAY = "UTILITY_SMS_GATEWAY";
    public static final String UTILITY_MAPPING_VSA_UNIT = "UTILITY_MAPPING_VSA_UNIT";
    public static final String UTILITY_CFG_MAP_NETWORK_LEVEL = "UTILITY_CFG_MAP_NETWORK_LEVEL";
    public static final String UTILITY_SEARCH_SERVICE_CR = "UTILITY_SEARCH_SERVICE_CR";
    public static final String UTILITY_WO_FILE_TEMP = "UTILITY_WO_FILE_TEMP";
    public static final String UTILITY_CFG_CLOSED_TICKET = "UTILITY_CFG_CLOSED_TICKET";
    public static final String UTILITY_CFG_REQUIRE_HAVE_WO = "UTILITY_CFG_REQUIRE_HAVE_WO";
    public static final String UTILITY_LANGUAGE_EXCHANGE = "UTILITY_LANGUAGE_EXCHANGE";
    public static final String UTILITY_CFG_CREATE_WO = "UTILITY_CFG_CREATE_WO";
    public static final String UTILITY_USER_APPROVE = "UTILITY_USER_APPROVE";
    public static final String UTILITY_CFG_TROUBLE_CALL_IPCC = "UTILITY_CFG_TROUBLE_CALL_IPCC";
    public static final String UTILITY_IPCC_SERVICE = "UTILITY_IPCC_SERVICE";
    public static final String UTILITY_CR_DT_TEMPLATE_FILE = "UTILITY_CR_DT_TEMPLATE_FILE";
    public static final String UTILITY_COORDINATION_SETTING = "UTILITY_COORDINATION_SETTING";
    public static final String UTILITY_CONFIG_WORK_TIME_FOLLOW = "UTILITY_CONFIG_WORK_TIME_FOLLOW";
    //  public static final String GNOC_KEDB = "GNOC_KEDB";
    public static final String KEDB_MANAGEMENT = "KEDB_MANAGEMENT";
    //  public static final String GNOC_TT_MANAGEMENT = "GNOC_TT_MANAGEMENT";
    public static final String TT_TROUBLE = "TT_TROUBLE";
    public static final String TT_CONFIG_TIME = "TT_CONFIG_TIME";
    public static final String TT_CONFIG_RECEIVE_UNIT = "TT_CONFIG_RECEIVE_UNIT";
    public static final String TT_CONFIG_MOP = "TT_CONFIG_MOP";
    public static final String TT_MESSAGE_CONFIG = "TT_MESSAGE_CONFIG";
    public static final String TT_INFO_CONFIG = "TT_INFO_CONFIG";
    //  public static final String GNOC_CR = "GNOC_CR";
    public static final String CR_MANAGEMENT = "CR_MANAGEMENT";
    public static final String CR_ALARM_SETTING = "CR_ALARM_SETTING";
    public static final String CR_GROUP_DEPARTMENT_CONFIG = "CR_GROUP_DEPARTMENT_CONFIG";
    public static final String CR_DEVICE_TYPE_MANAGEMENT = "CR_DEVICE_TYPE_MANAGEMENT";
    public static final String CR_SCOPES_MANAGEMENT = "CR_SCOPES_MANAGEMENT";
    public static final String CR_ROLES_MANAGEMENT = "CR_ROLES_MANAGEMENT";
    public static final String CR_CONFIG_IMPACT_SEGMENT = "CR_CONFIG_IMPACT_SEGMENT";
    public static final String CR_CONFIG_AFFECTED_LEVEL = "CR_CONFIG_AFFECTED_LEVEL";
    public static final String CR_DEPARTMENTS_ROLES_MANAGEMENT = "CR_DEPARTMENTS_ROLES_MANAGEMENT";
    public static final String CR_DEPARTMENTS_SCOPES_MANAGEMENT = "CR_DEPARTMENTS_SCOPES_MANAGEMENT";
    public static final String CR_ROLES_SCOPES_MANAGEMENT = "CR_ROLES_SCOPES_MANAGEMENT";
    public static final String CR_PROCESS_MANAGEMENT = "CR_PROCESS_MANAGEMENT";
    public static final String CR_CAB_USERS = "CR_CAB_USERS";
    public static final String CR_GROUP_UNIT = "CR_GROUP_UNIT";
    public static final String CR_CONFIG_CHILD_ARRAY = "CR_CONFIG_CHILD_ARRAY";
    public static final String CR_ACTION_CODE = "CR_ACTION_CODE";
    public static final String CR_IMPACT_FRAME = "CR_IMPACT_FRAME";
    public static final String CR_CONFIG_TEMP_IMPORT = "CR_CONFIG_TEMP_IMPORT";
    public static final String CR_CONFIG_WEBSERVICE_IMPORT = "CR_CONFIG_WEBSERVICE_IMPORT";
    public static final String CR_CONFIG_RETURN_ACTION = "CR_CONFIG_RETURN_ACTION";
    //  public static final String GNOC_WO_MANAGEMENTS = "GNOC_WO_MANAGEMENTS";
    public static final String WO_MANAGEMENT = "WO_MANAGEMENT";
    public static final String WO_ERROR_CASE_MANAGEMENT = "WO_ERROR_CASE_MANAGEMENT";
    public static final String WO_MAP_PROVINCE_CD_GROUP = "WO_MAP_PROVINCE_CD_GROUP";
    public static final String WO_CD_GROUP_MANAGEMENT = "WO_CD_GROUP_MANAGEMENT";
    public static final String WO_TYPE_MANAGEMENT = "WO_TYPE_MANAGEMENT";
    public static final String WO_MATERIALS_CONFIG = "WO_MATERIALS_CONFIG";
    public static final String WO_CONFIG_WO_HELP_VSMART = "WO_CONFIG_WO_HELP_VSMART";
    public static final String WO_CONFIG_PROPERTY = "WO_CONFIG_PROPERTY";
    public static final String WO_UPDATE_SERVICE_INFRA = "WO_UPDATE_SERVICE_INFRA";
    public static final String WO_CONFIG_MAP_UNIT_GNOC_NIMS = "WO_CONFIG_MAP_UNIT_GNOC_NIMS";
    //  public static final String GNOC_SR_MANAGEMENT = "GNOC_SR_MANAGEMENT";
    public static final String SR_MANAGEMENT = "SR_MANAGEMENT";
    public static final String SR_CATALOG = "SR_CATALOG";
    public static final String SR_ROLE = "SR_ROLE";
    public static final String SR_ROLE_USER = "SR_ROLE_USER";
    public static final String SR_STATUS = "SR_STATUS";
    public static final String SR_ROLE_ACTION = "SR_ROLE_ACTION";
    public static final String SR_SERVICE_ARRAY = "SR_SERVICE_ARRAY";
    public static final String SR_SERVICE_GROUP = "SR_SERVICE_GROUP";
    public static final String SR_MAPPING_PROCESS_CR = "SR_MAPPING_PROCESS_CR";
    public static final String SR_REASON_REJECT = "SR_REASON_REJECT";
    public static final String SR_CONFIGURATION = "SR_CONFIGURATION";
    //  public static final String GNOC_MR_MANAGEMENTS = "GNOC_MR_MANAGEMENTS";
    public static final String MR_MANAGEMENTS = "MR_MANAGEMENTS";
    public static final String MR_SOFT_HISTORY = "MR_SOFT_HISTORY";
    public static final String MR_SOFT_IT = "MR_SOFT_IT";
    public static final String MR_SOFT_IT_SCHEDULE = "MR_SOFT_IT_SCHEDULE";
    public static final String MR_SOFT_IT_GENERATE_CR = "MR_SOFT_IT_GENERATE_CR";
    public static final String MR_SOFT_IT_PROCEDURE = "MR_SOFT_IT_PROCEDURE";
    public static final String MR_SOFT_IT_UNIT = "MR_SOFT_IT_UNIT";
    public static final String MR_SOFT_IT_CAT_DEVICE_TYPE = "MR_SOFT_IT_CAT_DEVICE_TYPE";
    public static final String MR_SOFT_IT_CAT_CONTENT = "MR_SOFT_IT_CAT_CONTENT";
    public static final String MR_SOFT_IT_CAT_MARKET = "MR_SOFT_IT_CAT_MARKET";
    public static final String MR_SOFT_TELECOM = "MR_SOFT_TELECOM";
    public static final String MR_SOFT_TELECOM_DEVICE_LIST = "MR_SOFT_TELECOM_DEVICE_LIST";
    public static final String MR_SOFT_TELECOM_UNIT = "MR_SOFT_TELECOM_UNIT";
    public static final String MR_SOFT_TELECOM_PROCEDURE = "MR_SOFT_TELECOM_PROCEDURE";
    public static final String MR_SOFT_TELECOM_SCHEDULE = "MR_SOFT_TELECOM_SCHEDULE";
    public static final String MR_HARD_IT = "MR_HARD_IT";
    public static final String MR_HARD_IT_DEVICE_LIST = "MR_HARD_IT_DEVICE_LIST";
    public static final String MR_HARD_IT_PROCEDURE = "MR_HARD_IT_PROCEDURE";
    public static final String MR_HARD_IT_UNIT = "MR_HARD_IT_UNIT";
    public static final String MR_HARD_IT_CHECKLIST = "MR_HARD_IT_CHECKLIST";
    public static final String MR_HARD_IT_CONTENT = "MR_HARD_IT_CONTENT";
    public static final String MR_HARD_IT_WO_TIME = "MR_HARD_IT_WO_TIME";
    public static final String MR_HARD_IT_WORK_ITEM = "MR_HARD_IT_WORK_ITEM";
    public static final String MR_HARD_IT_SCHEDULE = "MR_HARD_IT_SCHEDULE";
    public static final String MR_HARD_IT_HISTORY = "MR_HARD_IT_HISTORY";
    public static final String MR_HARD_ELECTRONIC = "MR_HARD_ELECTRONIC";
    public static final String MR_HARD_ELECTRONIC_DEVICE_LIST = "MR_HARD_ELECTRONIC_DEVICE_LIST";
    public static final String MR_HARD_ELECTRONIC_GROUP = "MR_HARD_ELECTRONIC_GROUP";
    public static final String MR_HARD_ELECTRONIC_PROCEDURE = "MR_HARD_ELECTRONIC_PROCEDURE";
    public static final String MR_HARD_ELECTRONIC_CYCLE = "MR_HARD_ELECTRONIC_CYCLE";
    public static final String MR_HARD_ELECTRONIC_WORK_ITEM = "MR_HARD_ELECTRONIC_WORK_ITEM";
    public static final String MR_HARD_ELECTRONIC_SCHEDULE = "MR_HARD_ELECTRONIC_SCHEDULE";
    public static final String MR_HARD_ELECTRONIC_HISTORY = "MR_HARD_ELECTRONIC_HISTORY";
    public static final String MR_HARD_ELECTRONIC_BTS = "MR_HARD_ELECTRONIC_BTS";
    public static final String MR_HARD_ELECTRONIC_BTS_CHECKLIST = "MR_HARD_ELECTRONIC_BTS_CHECKLIST";
    public static final String MR_HARD_ELECTRONIC_BTS_MATERIAL_REPLACE = "MR_HARD_ELECTRONIC_BTS_MATERIAL_REPLACE";
    public static final String MR_HARD_ELECTRONIC_BTS_DEVICE_LIST = "MR_HARD_ELECTRONIC_BTS_DEVICE_LIST";
    public static final String MR_HARD_ELECTRONIC_BTS_CYCLE = "MR_HARD_ELECTRONIC_BTS_CYCLE";
    public static final String MR_HARD_ELECTRONIC_BTS_PROCEDURE = "MR_HARD_ELECTRONIC_BTS_PROCEDURE";
    public static final String MR_HARD_ELECTRONIC_BTS_SCHEDULE = "MR_HARD_ELECTRONIC_BTS_SCHEDULE";
    public static final String MR_HARD_ELECTRONIC_BTS_HISTORY = "MR_HARD_ELECTRONIC_BTS_HISTORY";
    public static final String MR_VIBAS = "MR_VIBAS";
    public static final String MR_VIBA_LISTS = "MR_VIBA_LISTS";
    public static final String MR_VIBA_UNITS = "MR_VIBA_UNITS";
    public static final String MR_VIBA_HISTORY = "MR_VIBA_HISTORY";
    public static final String MR_UCTT = "MR_UCTT";
    public static final String MR_DEVICE_NIMS = "MR_DEVICE_NIMS";
    public static final String MR_SCHEDULE_DISCHARGE = "MR_SCHEDULE_DISCHARGE";
    //  public static final String GNOC_RISK_MANAGEMENT = "GNOC_RISK_MANAGEMENT";
    public static final String RISK_MANAGEMENT = "RISK_MANAGEMENT";
    public static final String RISK_SYSTEMS = "RISK_SYSTEMS";
    public static final String RISK_TYPES = "RISK_TYPES";
    public static final String RISK_CFG_BUSINESS = "RISK_CFG_BUSINESS";

    //tiennv them role MR MTS
    public static final String ROLE_CNKTT_VTNET = "CNKTT_VTNET";

    //    ROLE_USER
    public static final String ROLE_USER_ADMIN = "ADMIN";
    public static final String ROLE_USER_GNOC_EDIT_USER = "GNOC_EDIT_USER";
    public static final String ROLE_USER_ROLE_TP = "TP";

    //ROLE VSA
    public static final String ROLE_VSA_GNOC_ADMIN = "GNOC_ADMIN";
    public static final String ROLE_VSA_GNOC_EDIT_USER = "GNOC_EDIT_USER";
  }

  public interface PERMISSION {

    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";
    public static final String DELETE = "DELETE";
    public static final String IMPORT = "IMPORT";
    public static final String EXPORT = "EXPORT";
    public static final String THE_SIGN = "THE_SIGN";
  }

  public static class MR_CONFIG {

    public static final String TEST_XA_ACQUY_BAN_TU_DONG = "TEST_XA_ACQUY_BAN_TU_DONG";
    public static final String TEST_XA_ACQUY_TU_DONG = "TEST_XA_ACQUY_TU_DONG";


    public static final String TEST_XA_MR_ARRAY_CODE = "TEST_XA_MR_ARRAY_CODE";
    public static final String TEST_XA_MR_CREATE_USER = "TEST_XA_MR_CREATE_USER";
    public static final String TEST_XA_MR_TITLE = "TEST_XA_MR_TITLE";
    public static final String TEST_XA_MR_TIME = "TEST_XA_MR_TIME";
    public static final String TEST_XA_MR_PRIORITY = "TEST_XA_MR_PRIORITY";
    public static final String TEST_XA_MR_IMPACT = "TEST_XA_MR_IMPACT";
    public static final String TEST_XA_MR_WORKS = "TEST_XA_MR_WORKS";
    public static final String TEST_XA_MR_TYPE = "TEST_XA_MR_TYPE";
    public static final String TEST_XA_MR_IS_SV_AFFECTED = "TEST_XA_MR_IS_SV_AFFECTED";
    public static final String TEST_XA_MR_TECHNICHCAL = "TEST_XA_MR_TECHNICHCAL";

    public static final String TEST_XA_WO_CONTENT = "TEST_XA_WO_CONTENT";
    public static final String TEST_XA_WO_DES = "TEST_XA_WO_DES";
    public static final String TEST_XA_WO_TYPE_ID = "TEST_XA_WO_TYPE_ID";
    public static final String TEST_XA_WO_CREATE_USER = "TEST_XA_WO_CREATE_USER";
    public static final String TEST_XA_WO_PRIORITY = "TEST_XA_WO_PRIORITY";
    public static final String TEST_XA_WO_STATUS = "TEST_XA_WO_STATUS";
  }

  public static class LANGUAGES {

    public static final String ENGLISH = "2";
    public static final String VIETNAMESE = "1";
  }

  public interface RESULT {

    String EXIST = "EXIST";
    String SUCCESS = "SUCCESS";
    String ERROR = "ERROR";
    String DUPLICATE = "DUPLICATE";
    String FILE_IS_NULL = "FILE_IS_NULL";
    String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    String NODATA = "NODATA";
    String FILE_ERROR = "FILE_ERROR";
    String FILE_INVALID = "FILE_INVALID";
    String FILE_TYPE_INVALID = "FILE_TYPE_INVALID";
    String DATA_OVER = "DATA_OVER";
    String NOT_ACCESS = "NOT_ACCESS";
    String NO_CAN_DELETE = "NO_CAN_DELETE";
    String FILE_INVALID_FORMAT = "FILE_INVALID_FORMAT";
    String FAIL = "FAIL";
    String ERROR_SYSTEM = "ERROR_SYSTEM";
    String SUCCESS_NOT_WO = "SUCCESS_NOT_WO";
    String DELETE_PROCESS = "FAIL: NULL PROCESS_ID OR TABLE_NAME";
    String DELETE_ATTACH_FILE = "FAIL: TEM_IMPORT_ID NOT NULL";
    String DELETE_REQUIRE_EXIST = "DELETE_REQUIRE_EXIST";
    String ERROR_UPDATE = "ERROR_UPDATE";
    String FILE_INVALID_FORM = "FILE_INVALID_FORM";
  }

  public final static class OD_MASTER_CODE {

    public static final String OD_STATUS = "OD_STATUS";
    public static final String OD_PRIORITY = "OD_PRIORITY";
    public static final String OD_CFG_BUSINESS_COLUMN = "OD_CFG_BUSINESS_COLUMN";
    public static final String OD_SCHEDULE = "OD_SCHEDULE";
    public static final String OD_MASTER_DATA = "6";
    public static final String OD_MASTER_DATA_STATUS = "1";
    public static final String OD_MASTER_DATA_PRIORITY = "3";
  }

  public final static class TT_MASTER_CODE {

    public static final String TT_STATUS = "TT_STATE";
    public static final String TT_PRIORITY = "TT_PRIRORITY";
    public static final String TT_CFG_BUSINESS_COLUMN = "TT_CFG_BUSINESS_COLUMN";
    public static final String TT_CHANGE_STATUS_ROLE = "TT_CHANGE_STATUS_ROLE";
    public static final String TT_CONTROL = "TT_CONTROL";
    public static final String CAT_REASON = "CAT_REASON";
    public static final String ALARM_GROUP = "ALARM_GROUP";
    public static final String PT_SUB_CATEGORY = "PT_SUB_CATEGORY";
    public static final String TT_ROLE_NVT = "1";
    public static final String TT_ROLE_NVXL = "2";
    public static final String TT_ROLE_DVXL = "3";
    public static final String TT_ROLE_DVT = "4";
    public static final String TT_ROLE_LDDVT = "5";
  }

  public interface CATEGORY {

    public static final String WO_ACTION_GROUP = "WO_ACTION_GROUP";
    public static final String WO_MATERIAL_OVER_REASON = "WO_MATERIAL_OVER_REASON";
    public static final String WO_GROUP_TYPE = "WO_GROUP_TYPE";
    public static final String REASON_OVERDUE_WO = "REASON_OVERDUE_WO";
    public static final String CFG_MAP_GNOC_NIMS_BUSINESS = "CFG_MAP_GNOC_NIMS_BUSINESS";

    public static final String OD_STATUS = "OD_STATUS";
    public static final String OD_PRIORITY = "OD_PRIORITY";
    public static final String OD_SCHEDULE = "OD_SCHEDULE";
    public static final String OD_TYPE = "OD_TYPE";
    public static final String OD_CHANGE_STATUS_ROLE = "OD_CHANGE_STATUS_ROLE";

    public static final String OD_GROUP_TYPE = "OD_GROUP_TYPE";
    public static final String OD_CLEAR_CODE = "OD_CLEAR_CODE";
    public static final String OD_CLOSE_CODE = "OD_CLOSE_CODE";
    public static final String OD_CFG_BUSINESS_COLUMN = "OD_CFG_BUSINESS_COLUMN";
    public static final String OD_INSERT_SOURCE = "OD_INSERT_SOURCE";

    public static final String PROCESS_STATUS_OD = "175";
    public static final String GNOC_OD_PRIORITY = "GNOC_OD_PRIORITY";
    public static final String GNOC_UNIT = "GNOC_UNIT";
    public static final String GNOC_OD_TYPE = "GNOC_OD_TYPE";

    public static final String OD_REASON_GROUP = "OD_REASON_GROUP";
    public static final String OD_SOLUTION_GROUP = "OD_SOLUTION_GROUP";

    public static final String VENDOR = "VENDOR";
    public static final String ARRAY_BHKN = "ARRAY_BHKN";
    public static final String PT_TYPE = "PT_TYPE";
    public static final String PT_SUB_CATEGORY = "PT_SUB_CATEGORY";
    public static final String KEDB_STATE = "KEDB_STATE";
    public static final String GNOC_ALIAS = "GNOC_ALIAS";
    public static final String PT_RELATED_TYPE = "PT_RELATED_TYPE";
    public static final String PT_STATE = "PT_STATE";
    public static final String GNOC_SHIFT_KPI = "GNOC_SHIFT_KPI";
    public static final String GNOC_SHIFT = "GNOC_SHIFT";
    public final static String CR_FILE_REQUIRED = "CR_FILE_REQUIRED";

    public static final String RISK_STATUS = "RISK_STATUS";
    public static final String RISK_PRIORITY = "RISK_PRIORITY";
    public static final String RISK_EFFECT = "RISK_EFFECT";
    public static final String RISK_FREQUENCY = "RISK_FREQUENCY";
    public static final String RISK_REDUNDANCY = "RISK_REDUNDANCY";
    public static final String RISK_SYSTEM_PRIORITY = "RISK_SYSTEM_PRIORITY";
    public static final String RISK_GROUP_TYPE = "RISK_GROUP_TYPE";
    public static final String RISK_CHANGE_STATUS_ROLE = "RISK_CHANGE_STATUS_ROLE";
    public static final String CFG_FT_ON_TIME_BUSINESS = "CFG_FT_ON_TIME_BUSINESS";
  }

  public interface MASTER_DATA {

    public static final String OD = "6";
    public static final String OD_STATUS = "1";
    public static final String OD_TYPE = "2";
    public static final String OD_PRIORITY = "3";
    public static final String OD_GROUP_TYPE = "4";
    public static final String OD_CLEAR_CODE = "5";
    public static final String OD_CLOSE_CODE = "6";
    public static final String OD_CFG_BUSINESS_COLUMN = "7";
    public static final String OD_INSERT_SOURCE = "8";
    public static final String OD_SCHEDULE = "9";
    public final static Long YES = 1L;
    public static final Integer SIZE = 1;
    public static final Integer PAGE_SIZE = 10;

  }

  public interface LANGUAGUE_EXCHANGE_SYSTEM {

    public static final String SR = "2";
    public static final String COMMON = "1";
    public static final String CR_MR = "2";
    public static final String PT_TT = "3";
    public static final String WO = "4";
    public static final String OTHER = "5";
    public static final String RISK = "7";
  }

  public interface GNOC_FILE_BUSSINESS {

    public static final String OD = "OD";
    public static final String OD_CFG_SCHEDULE_CREATE = "OD_CFG_SCHEDULE_CREATE";
    public static final String PROBLEMS = "PROBLEMS";
    public static final String KEDB = "KEDB";
    public static final String SHIFT_HANDOVER = "SHIFT_HANDOVER";
    public static final String TROUBLES_IBM = "TROUBLE_FILE_IBM";
    public static final String TROUBLES = "TROUBLE_FILE";
    public static final String TROUBLE_MOP_DT = "TROUBLE_MOP_DT";
    public static final String CFG_FILE_CREATE_WO = "CFG_FILE_CREATE_WO";
    public static final String WO_TYPE_FILES_GUIDE = "WO_TYPE_FILES_GUIDE";
    public static final String CFG_WO_HELP_VSMART = "CFG_WO_HELP_VSMART";
    public static final String WO = "WO";
    public static final String CR = "CR_FILES_ATTACH";
    public static final String CR_TEMP_IMPORT = "TEMP_IMPORT";
    public static final String WO_POST_INSPECTION = "WO_POST_INSPECTION";
    public static final String WO_SUPPORT = "WO_SUPPORT";
    public static final String SR = "SR";
    public static final String SR_CATALOG = "SR_CATALOG";
    public static final String SR_CREATE_AUTO_CR = "SR_CREATE_AUTO_CR";
    public static final String SR_CREATE_AUTO_CR_PROCESS = "SR_CREATE_AUTO_CR_PROCESS";
    public static final String RISK = "RISK";
    public static final String RISK_CONFIG = "RISK_CONFIG";
    public static final String TT_CONFIG = "TT_CONFIG";
    public static final String RISK_SYSTEM = "RISK_SYSTEM";
    public static final String MR_CFG_PROCEDURE_TEL = "MR_CFG_PROCEDURE_TEL_FILES";
    public static final String MR_UCTT = "MR_UCTT_FILES";
    public static final String MR = "MR_FILES_ATTACH";
    public static final String MR_CFG_PROCEDURE_IT_SOFT = "MR_CFG_PROCEDURE_FILES";
    public static final String MR_SCHEDULE_BTS_HIS_FILE = "MR_SCHEDULE_BTS_HIS_FILE";
    public static final String MR_NODE_CHECKLIST = "MR_NODE_CHECKLIST_FILES";
    public static final String COMMON = "COMMON";
    public static final String GNOC_FILE_SR_DEL = "SR_DEL";
  }

  public interface APPLIED_BUSSINESS {

    public static final String WO_TYPE = "1";
    public static final String GROUP_TYPE = "2";
    public static final String WO_PRIORITY = "3";
    public static final String SERVICE_TYPE = "4";
    public static final String REASON_OVERDUE = "5";
    public static final String IMPACT_SEGMENT = "10";
    public static final String CR_PROCESS = "7";
    public static final String SUBCATEGORY = "13";
    public static final String RETURN_CODE_CATALOG = "12";
    public static final String CONFIG2_TYPE = "22";
    public static final String WORKLOG_TYPE = "23";
    public static final String RISK_TYPE = "8";
    public static final String RISK_PRIORITY = "3";
    /*TuanDS add new PT*/
    public static final Long ROLES = 1L;
    public static final Long CAT_DEVICE_TYPE = 2L;
    public static final Long CAT_ITEM = 3L;
    public static final Long CATEGORY = 4L;
    public static final Long CONTACT = 5L;
    public static final Long NOTIFY = 6L;
    public static final Long TRANSITION_STATE_CONFIG = 7L;
    public static final Long CAT_SERVICE = 8L;
    public static final Long COMP_CAUSE = 9L;
    public static final Long CONFIG_PROPERTY = 10L;
    public static final Long DEVICE_TYPE_VERSION = 11L;
    public static final Long LOCALE_MESSAGE = 12L;
    public static final Long DEVICE_TYPE_VERSION_SOFTWARE_VER = 13L;
    public static final Long DEVICE_TYPE_VERSION_HARDWARE_VER = 14L;
    public static final Long DEVICE_TYPE_VERSION_TEMP = 15L;
    public static final Long RISK = 7L;
    public static final Long CATEGORY_DES = 16L;
    public static final String CAT_REASON = "1";
    public static final String CFG_PROBLEM_TIME_PROCESS = "2";
    public static final String CR_MANAGER_ROLE = "5";
    public static final String CR_MANAGER_SCOPE = "6";
    public static final String GROUP_UNIT = "9";
    public static final String DEVICE_TYPES = "8";
    public static final String TEMP_IMPORT = "14";
    public static final String CHECKLIST_BTS = "51";
  }

  public interface APPLIED_BUSSINESS_CLOUMN {

    public static final String TEMP_IMPORT_NAME = "1";
    public static final String TEMP_IMPORT_TITLE = "2";

  }

  public interface COMMON_TRANSLATE_BUSINESS {

    public static final Long ROLES = 1L;
    public static final Long CAT_DEVICE_TYPE = 2L;
    public static final Long CAT_ITEM = 3L;
    public static final Long CATEGORY = 4L;
    public static final Long CONTACT = 5L;
    public static final Long NOTIFY = 6L;
    public static final Long TRANSITION_STATE_CONFIG = 7L;
    public static final Long CAT_SERVICE = 8L;
    public static final Long COMP_CAUSE = 9L;
    public static final Long CONFIG_PROPERTY = 10L;
    public static final Long DEVICE_TYPE_VERSION = 11L;
    public static final Long LOCALE_MESSAGE = 12L;
    public static final Long DEVICE_TYPE_VERSION_SOFTWARE_VER = 13L;
    public static final Long DEVICE_TYPE_VERSION_HARDWARE_VER = 14L;
    public static final Long DEVICE_TYPE_VERSION_TEMP = 15L;
    public static final Long CATEGORY_DES = 16L;
  }

  public interface PRIORITY_BRCD {

    public static final String NORMAL = "1";
    public static final String HOT = "3";
    public static final String VIP = "4";
    public static final String PRI = "5";
  }

  public interface Global {

    public static final String ADD = "ADD";
    public static final String EDIT = "EDIT";
  }

  public final static class OD_STATUS {

    public static final Long NEW = 1L;
    public static final Long DEFERED = 9L;
    public static final Long CLOSE = 15L;
    public static final Long REJECT = 0L;

  }

  public final static class OD_NEXT_ACTION {

    public static final String FORCE_CLOSED = "FORCE_CLOSED";
    public static final String COMPLETE_NIMS_KDT_TK = "COMPLETE_NIMS_KDT_TK";
    public static final String CHECK_NIMS_DOC_DAO = "CHECK_NIMS_DOC_DAO";
    public static final String UPDATE_NIMS_DOC_DAO_BKK = "UPDATE_NIMS_DOC_DAO_BKK";
    public static final String CHANGE_STATUS_TO = "CHANGE_STATUS_TO";
    public static final String CHANGE_STATUS_TO_X = "CHANGE_STATUS_TO_";
    public static final String CHECK_UNIT_IS_CREATE_UNIT = "CHECK_UNIT_IS_CREATE_UNIT";
    public static final String CHECK_FINISHED_TIME = "CHECK_FINISHED_TIME";
//        public static final String DEFERED = 9;
//        public static final String CLOSE = 15;
//        public static final String REJECT = 0;

  }

  public final static class OD_TYPE_CODE {

    public static final String OD_TYPE_KDT_NP = "OD_TYPE_KDT_NP";
    public static final String OD_TYPE_KDT_ND = "OD_TYPE_KDT_ND";
    public static final String OD_TYPE_KDT_TK = "OD_TYPE_KDT_TK";
    public static final String OD_SENDER_VOFFICE = "OD_SENDER_VOFFICE";
    public static final String OD_REGISTER_VOFFICE = "OD_REGISTER_VOFFICE";
    public static final String OD_FORM_DOCUMENT_VOFFICE = "OD_FORM_DOCUMENT_VOFFICE";
    public static final String OD_AREA_VOFFICE = "OD_AREA_VOFFICE";

    public static final String OD_TYPE_UPDATE_VIBA = "OD_TYPE_UPDATE_VIBA";
    public static final String OD_TYPE_KPI_SUY_GIAM_NPMSV2 = "OD_TYPE_KPI_SUY_GIAM_NPMSV2";

  }

  public static class AP_PARAM {

    public final static String TYPE_BACKUP_STYLE = "BACKUP_STYLE";
    public final static String TYPE_DOWNLOAD_TYPE = "DOWNLOAD_TYPE";
    public final static String CALCULATED_BY = "CALCULATED_BY";
    public final static String COMMON_STATUS = "COMMON_STATUS";
    public final static String CHARGE_REFERENCE = "CHARGE_REFERENCE";
    public final static String PROM_DEPENDENCE = "PROM_DEPENDENCE";
    public final static String PROM_CHARGE_BY = "PROM_CHARGE_BY";
    public final static String PROMOTION_TYPE = "PROMOTION_TYPE";
    public final static String PROMOTION_LEVEL = "PROMOTION_LEVEL";
    public final static String BILL_ITEM_TABLE = "BILL_ITEM_TABLE";
    public final static String ADJUSTMENT_TYPE = "ADJUSTMENT_TYPE";
    public final static String ADJUSTMENT_POLICY = "ADJUSTMENT_POLICY";
    public final static String PENALTY = "PENALTY";
    public final static String COMPENSATION = "COMPENSATION";
    public final static String RATE_EQUAL = "RATE_EQUAL";
    public final static String WO_TYPE_QLTS = "WO.TYPE.CHECK.QLTS";
    public final static String WO_TYPE_QLTS_NC = "WO.TYPE.CHECK.QLTS.NC"; // nang cap / nang cap UCTT
    public final static String WO_TYPE_QLTS_NC_THUE = "WO.TYPE.CHECK.QLTS.NC.THUE"; // nang cap / nang cap UCTT
    public final static String WO_TYPE_QLTS_HC = "WO.TYPE.CHECK.QLTS.HC";
    public final static String WO_TYPE_QLTS_NC_UCTT = "WO.TYPE.CHECK.QLTS.NC.UCTT";
    public final static String WO_TYPE_QLTS_NC_UCTT_THUE = "WO.TYPE.CHECK.QLTS.NC.UCTT.THUE";
    public final static String WO_TYPE_QLTS_THUHOI = "WO.TYPE.CHECK.QLTS.THUHOI";
    public final static String WO_TYPE_QLTS_DC_NC = "WO.TYPE.CHECK.QLTS.DC.NC";
    public final static String WO_TYPE_QLTS_DC_HC = "WO.TYPE.CHECK.QLTS.DC.HC";
    //-------------
    public final static String WO_TYPE_QLTS_BAO_MAT = "WO.TYPE.CHECK.QLTS.BAO.MAT";
    public final static String WO_TYPE_QLTS_BAO_HONG = "WO.TYPE.CHECK.QLTS.BAO.HONG";
    public final static String WO_TYPE_QLTS_BAO_KXD = "WO.TYPE.CHECK.QLTS.BAO.KXD";
    public final static String WO_TYPE_QLTS_DD_CH = "WO.TYPE.CHECK.QLTS.DD.CH"; // di doi chuyen huong

    public final static String WO_TYPE_NAME_NC = "TS_NANG_CAP_TRAM";
    public final static String WO_TYPE_NAME_NC_THUE = "TS_NANG_CAP_TRAM_THUE_DOI_TAC";
    public final static String WO_TYPE_NAME_NC_UCTT = "TS_NANG_CAP_TRAM_UCTT";
    public final static String WO_TYPE_NAME_NC_UCTT_THUE = "TS_NANG_CAP_TRAM_UCTT_THUE_DOI_TAC";
    public final static String WO_TYPE_NAME_THUHOI = "TS_THU_HOI_BAO_HANH";
    public final static String WO_TYPE_NAME_HC = "TS_HA_CAP_TRAM";
    public final static String WO_TYPE_NAME_DC_NC = "TS_DIEU_CHUYEN_NANG_CAP";
    public final static String WO_TYPE_NAME_DC_HC = "TS_DIEU_CHUYEN_HA_CAP";
    //--------------
    public final static String WO_TYPE_NAME_BAO_MAT = "TS_BAO_MAT";
    public final static String WO_TYPE_NAME_BAO_HONG = "TS_THU_HOI_BAO_HONG";
    public final static String WO_TYPE_NAME_BAO_KXD = "TS_THU_HOI_KHONG_SU_DUNG";
    public final static String WO_TYPE_NAME_DD_CH = "TS_DI_DOI_CHUYEN_HUONG"; // di doi chuyen huong <=> nang cap uctt_thue ngoai

    public final static String WO_TYPE_XLSCVT = "WO.TYPE.XLSCVT";
    public final static String WO_TYPE_XLSCCD = "WO.TYPE.XLSCCD";
    public final static String WO_TYPE_CDBR = "WO_TYPE_CDBR";
    public final static String WO_TYPE_KBDV_QLCTKT = "WO_TYPE_KBDV_QLCTKT";

    public final static String CATEGORY_OTHER_CODE = "OTHER";
    public final static String USER_CREATE_KBDV_QLCTKT = "USER_CREATE_KBDV_QLCTKT";
    public final static String WO_TEST_SERVICE = "WO.TEST.SERVICE";

    public final static String COMP_GROUP_DEPLOY_NEW = "COMP_GROUP_DEPLOY_NEW";
    public final static String WO_TYPE_REQUIRED_STATION = "WO_TYPE_REQUIRED_STATION";

    public final static String WO_TYPE_AMI_ONE = "WO_TYPE_AMI_ONE";
    public final static String WO_TYPE_THUHOI_CAP_NIMS = "WO_TYPE_THUHOI_CAP_NIMS";

    public final static String PRIORITY_QLTS_HC = "PRIORITY_QLTS_HC";
    public final static String PRIORITY_QLTS_THUHOI = "PRIORITY_QLTS_THUHOI";

    public final static String WO_TYPE_BD_VIBA = "WO_TYPE_BD_VIBA";
    public final static String OD_SCHEDULE = "OD_SCHEDULE";

    public final static String NIMS_TYPE_STATION = "1";
    public final static String NIMS_TYPE_VIBA = "2";

    public final static String NPMSV2_TYPE_CLOSE = "closeOrder";
    public final static String NPMSV2_TYPE_CAN_CLOSE = "canCloseOrder";

    public final static String WO_TYPE_BHHT = "WO_TYPE_BHHT";
    public final static String WO_TYPE_BHHTTC = "WO_TYPE_BHHTTC";
    public final static String WO_TYPE_KTNT = "WO_TYPE_KTNT";

    public final static String WO_TYPE_NOT_CALL_MR = "WO_TYPE_NOT_CALL_MR";

    public final static String WO_TYPE_BDCD_TRAM = "WO_TYPE_BDCD_TRAM";
    public final static String WO_TYPE_DO_XANG_DAU = "WO_TYPE_DO_XANG_DAU";
    public final static String WO_TYPE_XU_LY_CAN_NHIEU = "WO_TYPE_XU_LY_CAN_NHIEU";
    public final static String WO_TYPE_NIMS_HUY_TRAM = "WO_TYPE_NIMS_HUY_TRAM";
    public final static String WO_PRIORITY_MINOR = "WO_PRIORITY_MINOR";
    public final static String WO_TYPE_NIMS_DI_DOI_TRAM = "WO_TYPE_NIMS_DI_DOI_TRAM";
    public final static String WO_TYPE_KET_THUC_XU_LY_CAN_NHIEU = "WO_TYPE_KET_THUC_XU_LY_CAN_NHIEU";
    public final static String NUM_DATE_PROCESS_XL_CAN_NHIEU = "NUM_DATE_PROCESS_XL_CAN_NHIEU";
    public final static String USED_MAJOR_CODE = "usedMajorCode";
    public final static String GET_DATA_JSON_STATION = "GNOC_GET_DEPT_STATION";
    public final static String NIMS_CABINET_STATUS_HUY = "Hủy";
    public final static String WO_TYPE_CDBR_FOR_STL = "WO_TYPE_CDBR_FOR_STL";
    public final static String WO_TYPE_SCTBCD_ACCESS = "WO_TYPE_SCTBCD_ACCESS";
    public final static String WO_TYPE_HOAN_CONG_OS = "WO_TYPE_HOAN_CONG_OS";
    public final static String WO_TYPE_NGHIEM_THU_OS = "WO_TYPE_NGHIEM_THU_OS";
    public final static String WO_TYPE_THI_CONG_OS = "WO_TYPE_THI_CONG_OS";
    public final static String WO_TYPE_NIMS_KTDL = "WO_TYPE_NIMS_KTDL";
    public final static String SERVICE_CLOSE_WHEN_COMPLETE = "SERVICE_CLOSE_WHEN_COMPLETE";
    public final static String NUM_DATE_CHECK_ACC_SPM = "NUM_DATE_CHECK_ACC_SPM";

    public final static String IS_USED_CHECK_UDATE_SUPORT_WO = "isUsedCheckUpdateSupportWO";
    public final static String WO_TYPE_NAME_HKSC = "HKSC";
    public final static String WO_TYPE_NAME_HKTK = "HKTK";
    public final static String POINT_OK_HKSC = "POINT_OK_HKSC";
    public final static String POINT_OK_HKTK = "POINT_OK_HKTK";
    public final static String VTT_KHAC_PHUC_HAU_KIEM = "VTT_KHAC_PHUC_HAU_KIEM";
    public final static String PRIORITY_HAU_KIEM = "PRIORITY_HAU_KIEM";
    public final static String PRIORITY_COLOR_CODE = "PRIORITY_COLOR_CODE";
    public final static String WO_TYPE_DO_XANG_DAU_MYT = "WO_TYPE_DO_XANG_DAU_MYT";
    public final static String WO_TYPE_NIMS_BAO_CAO_DI_DOI = "WO_TYPE_NIMS_BAO_CAO_DI_DOI";
    public final static String NIMS_HUY_TRAM = "revokeCabinet";
    public final static String NIMS_HUY_VI_TRI = "revokeStation";
    public final static String NIMS_GIU_TRAM = "keepStation";

    public final static String WO_TYPE_NPMS_XLTTUT = "WO_TYPE_NPMS_XLTTUT";
    public final static String USER_ROLE_TP = "USER_ROLE_TP";

    public final static String RISK_STATUS_PROPERTY = "Risk_status_property";
    public final static String CHECK_UNIQUE_LANE_OD = "CHECK_UNIQUE_LANE_OD";

    public final static String WO_TYPE_BDNT = "WO_TYPE_BDNT";
    public final static String MR_BDC_BTS_MFD_DH = "MR_BDC_BTS_MFD_DH";
    public final static String IS_CALL_TT_SCVT = "IS_CALL_TT_SCVT";
    public final static String WO_TYPE_XLSCC = "WO_TYPE_XLSCC";
    public final static String WO_TYPE_CCKPC = "WO_TYPE_CCKPC";
    public final static String WO_TYPE_NOT_ALLOW_REJECT = "WO_TYPE_NOT_ALLOW_REJECT";
    public final static String IS_CALL_IMES = "IS_CALL_IMES";
    public final static String WO_TYPE_CLOSE_WHEN_COMPLETE = "WO_TYPE_CLOSE_WHEN_COMPLETE";
  }

  public final static class WO_IS_SEND_FT {

    public static final String SEND_FT = "1";
    public static final String SEND_CD = "0";
  }

  public interface CONFIG_PROPERTY_KEY {

    String AMI_ONE_CC_SERVICE_ID = "AMI_ONE_CC_SERVICE_ID";
    String AMI_ONE_TEL_SERVICE_ID = "AMI_ONE_TEL_SERVICE_ID";
    String AMI_ONE_SERVICE_ID = "AMI_ONE_SERVICE_ID";
    String AMI_ONE_INFRA_TYPE_ID = "AMI_ONE_INFRA_TYPE_ID";

    String CAMERA_CC_SERVICE_ID = "CAMERA_CC_SERVICE_ID";
    String CAMERA_TEL_SERVICE_ID = "CAMERA_TEL_SERVICE_ID";
    String CAMERA_INFRA_TYPE_ID = "CAMERA_INFRA_TYPE_ID";
    String CC_PRIORITY_HOT = "CC_PRIORITY_HOT";
  }

  public final static class OD_CHANGE_STATUS_ROLE {

    public static final String NVT = "1";
    public static final String DVT = "2";
    public static final String NVXL = "3";
    public static final String DVXL = "4";
    public static final String LD_DVXL = "5";
    public static final String NVPD = "6";
  }

  public static class WS_USERS {

    public final static String SYSTEM = "system";
    public final static String SALT = "wfm#2016";
  }

  public final static class OD_PROPERTY_CODE {

    public static final String OD_VOFFICE_INPUT = "OD_VOFFICE_INPUT";
    public static final String OD_APP_CODE_VOFFICE = "OD_APP_CODE_VOFFICE";
    public static final String OD_APP_PASS_VOFFICE = "OD_APP_PASS_VOFFICE";
    public static final String OD_SENDER_VOFFICE = "OD_SENDER_VOFFICE";
    public static final String OD_REGISTER_VOFFICE = "OD_REGISTER_VOFFICE";
    public static final String OD_FORM_DOCUMENT_VOFFICE = "OD_FORM_DOCUMENT_VOFFICE";
    public static final String OD_AREA_VOFFICE = "OD_AREA_VOFFICE";
    public static final String OD_INSERT_SOURCE_WO_HELP = "OD_INSERT_SOURCE_WO_HELP";

  }

  public static class BUSINESS_UNIT_NAME {

    public final static String SCTBCD_ACCESS = "SCTBCD_ACCESS"; // loai cong viec sua chua may phat dien
    public final static String BHHT = "BHHT"; // Bao hong ha tang
    public final static String KTNT = "KTNT"; // Kiem tra nha tram
    public final static String BHHT_KV = "BHHT_KV"; // Bao hong ha tang
    public final static String BHHT_TINH_INSOURCE = "BHHT_TINH_INSOURCE"; // Bao hong ha tang
    public final static String BHHT_TINH_OUTSOURCE = "BHHT_TINH_OUTSOURCE"; // Bao hong ha tang
    public final static String BHHT_HUYEN = "BHHT_HUYEN"; // Bao hong ha tang

  }

  public static final String vsaUserTokenAttribute = "vsaUserTokenAttribute";

  public final class PROBLEM {

    public final static String STATE = "PT_STATE";
    public final static String ALARM_GROUP = "ALARM_GROUP";
    public final static String PT_TYPE = "PT_TYPE";
    public final static String PRIORITY = "PT_PRIORITY";
    public final static String PT_PARENT = "PT_PARENT";
    public final static String PT_PRIMARY = "PT_PRIMARY";
    public final static String PT_CHILDREN = "PT_CHILDREN";
    public final static String PT_SECONDARY = "PT_SECONDARY";
    public final static String PT_DUPLICATED = "PT_DUPLICATED";
    public final static String PT_REDO = "PT_REDO";
    public final static String OPEN = "PT_OPEN";
    public final static String OPEN_2 = "PT_OPEN_2";
    public final static String UNASSIGN = "PT_UNASSIGNED";
    public final static String CLOSED = "PT_CLOSED";
    public final static String ABNORMALLY_CLOSED = "PT_ABNORMALLY_CLOSED";//dong dot xuat
    public final static String PT_CANCELED = "PT_CANCELED";
    public final static String PT_CLEAR_INCOMPLETED = "PT_CLEAR_INCOMPLETED";
    public final static String QUEUE = "PT_QUEUED";
    public final static String DEFERRED = "PT_DEFERRED";
    public final static String REQ_DEFERRED = "PT_REQ_DEFERRED";
    public final static String DIAGNOSED = "PT_DIAGNOSED";
    public final static String WORKAROUND_FOUND = "PT_WA_FOUND";
    public final static String SOLUTION_FOUND = "PT_SL_FOUND";
    public final static String WORKAROUND_IMPL = "PT_WA_IMPL";
    public final static String PT_REJECTED = "PT_REJECTED";
    public final static String ROOT_CAUSE_PROPOSAL = "PT_ROOT_CAUSE_PROPOSAL";  //Đề xuất nguyên nhân gốc
    public final static String WORKARROUND_PROPOSAL = "PT_WORKARROUND_PROPOSAL";  //Đề xuất giải pháp tạm thời
    public final static String SOLUTION_PROPOSAL = "PT_SOLUTION_PROPOSAL";//Đề xuất giải pháp triệt để
    public final static String RECEIVE_CALL_IPCC = "RECEIVE_CALL_IPCC";
    public final static String LEVEL_CALL_IPCC = "LEVEL_CALL_IPCC";
    public final static String SOLUTION_IMPL = "PT_SL_IMPL";
    public final static String PT_SUBCAT = "PT_SUB_CATEGORY";
    public final static String AFFECT_SERVICE = "PT_AFFECT_SERVICE";
    public final static String VENDOR = "VENDOR";
  }

  public final static class KEDB_MASTER_CODE {

    public static final String VENDOR = "VENDOR";
    public static final String ARRAY_BHKN = "ARRAY_BHKN";
    public static final String PT_TYPE = "PT_TYPE";
    public static final String PT_SUB_CATEGORY = "PT_SUB_CATEGORY";
    public static final String KEDB_STATE = "KEDB_STATE";
    public static final String KEDB_CLOSED = "KEDB_CLOSED";
    public static final String KEDB_UPDATE_APPROVE = "KEDB_UPDATE_APPROVE";
    public static final String KEDB_CREATE_APPROVE = "KEDB_CREATE_APPROVE";
    public static final String KEDB_CANCELED = "KEDB_CANCELED";
    public static final String ADMIN_KEDB = "ADMIN_KEDB";
    public static final String SUB_ADMIN_KEDB = "SUB_ADMIN_KEDB";
    public static final String USER_KEDB = "USER_KEDB";
    public static final String UNIT_CHECK_KEDB = "UNIT_CHECK_KEDB";
    public static final String MAX_IMPORT_KEDB = "MAX_IMPORT_KEDB";
  }

  public final static class CFG_TIME_TROUBLE_PROCESS_MASTER_CODE {

    public static final String PT_TYPE = "PT_TYPE";
    public static final String TT_PRIORITY = "TT_PRIORITY";
    public static final String GNOC_COUNTRY = "GNOC_COUNTRY";
    public static final String ALARM_GROUP = "ALARM_GROUP";
  }

  public final static class WO_CD_GROUP_MASTER_CODE {

    public static final String WO_CD_GROUP_TYPE = "WO_CD_GROUP_TYPE";
    public static final String GNOC_COUNTRY = "GNOC_COUNTRY";
  }

  public final static class RISK_MASTER_CODE {

    public static final String GNOC_COUNTRY = "GNOC_COUNTRY";
  }

  public final static class WO_MASTER_CODE {

    public static final String WO_SYSTEM_ARRAY = "WO_SYSTEM_ARRAY";
    public static final String WO_SYSTEM_WFM = "WFM";
    public static final String WO_FT = "FT";
    public static final String WO_CD = "CD";
    public static final String WO_SPM = "SPM";
    public static final String WO_MR = "MR";
    public static final String WO_OK = "OK";
    public static final String WO_NOK = "NOK";
    public static final String WO_CR = "CR";
    public static final String WO_BCCS_CC = "BCCS_CC";
    public static final String WO_TT = "TT";
    public static final String WO_CC_SCVT = "CC_SCVT";
    public static final String WO_HAU_KIEM = "HAU_KIEM";
    public static final String WO_RESULT_STATUS = "WO_RESULT_STATUS";
    public static final String WO_NIMS = "NIMS";
    public static final String GNOC_WO_MR = "GNOC_WO_MR";
  }

  public static class WS_RESULT {

    public static final String OK = "OK";
    public static final String NOK = "NOK";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String N_A = "N/A";
  }

  public interface SPM_ACTION_TYPE {

    String UPDATE = "1";
    String PENDING = "2";
    String OPEN_PENDING = "3";
    String UPDATE_PENDING = "1";
  }

  public interface TT_STATE {

    String OPEN = "OPEN";
    String CANCELED = "CANCELED";
    String Waiting_Receive = "WAITING RECEIVE";
    String REJECT = "REJECT";
    String QUEUE = "QUEUE";
    String Wait_for_Deferred = "WAIT FOR DEFERRED";
    String DEFERRED = "DEFERRED";
    String SOLUTION_FOUND = "SOLUTION FOUND";
    String CLEAR = "CLEAR";
    String CLOSED_Not_KEDB = "CLOSED NOT KEDB";
    String CLOSED = "CLOSED";
  }


  public final class TROUBLE {

    public final static String TT_WORK_ARROUND = "TT_WORK_ARROUND";
    public final static String TT_STATE = "TT_STATE";
    public final static String TT_RISK = "TT_RISK";
    public final static String TT_IMPACT = "TT_IMPACT";
    public final static String PRIORITY = "TT_PRIORITY";
    public final static String VENDOR = "VENDOR";
    public final static String TT_REJECT_CODE = "TT_REJECT_CODE";
    public final static String TT_CLOSE_CODE = "TT_CLOSE_CODE";
    public final static String TT_SOLUTION_TYPE = "TT_SOLUTION_TYPE";
    public final static String PT_TYPE = "PT_TYPE";
    public final static String PT_SUBCAT = "PT_SUB_CATEGORY";
    public final static String AFFECT_SERVICE = "PT_AFFECT_SERVICE";
    public final static String NETWORK_LEVEL = "NETWORK_LEVEL";
    public final static String TT_TRANS_NETWORK_TYPE = "TT_TRANS_NW_TYPE";
    public final static String CFG_TARGET_TYPE = "CFG_TARGET_TYPE";
    public final static String GNOC_COUNTRY = "GNOC_COUNTRY";
    public final static String INSERT_SOURCE = "INSERT_SOURCE";
    public final static String ROLE_LIST = "ROLE_LIST";
    public final static String WARN_LEVEL = "WARN_LEVEL";
    public final static String isTKTU = "99";
  }

  public final class TT_IMPACT {

    public final static String YES = "71";
    public final static String NO = "72";
  }

  public final class INSERT_SOURCE {

    public final static String TT = "TT";
    public final static String PT = "PT";
    public final static String NOC = "NOC";
    public final static String SPM_RADIO = "SPM_RADIO";
    public final static String SIRC = "SIRC";
    public final static String BCCS = "BCCS";
    public final static String SPM = "SPM";
    public final static String SPM_VTNET = "SPM_VTNET";

  }

  public final class TT_ARRAY {

    public final static String DD_MOBILE = "DD_MOBILE"; //mang di dong
    public final static String DD_CNTT = "DD_CNTT"; //mang cntt
    public final static String SPM_PAKH_CDBR = "SPM_PAKH_CDBR"; //mang bang rong co dinh

  }

  public interface TRANSLATE_OPTION {

    public static final String ID = "ID";
    public static final String CODE = "CODE";
  }

  public final class OPERATOR_SQL {

    public static final String OP_LIKE = " like ";
    public static final String OP_IN = " in ";
    public static final String LOGIC_OR = " or ";
    public static final String LOGIC_AND = " and ";
    public static final String OP_EQUAL = " = ";
    public static final String OP_NOT_EQUAL = " != ";
    public static final String OP_GREATER = " > ";
    public static final String OP_GREATER_EQUAL = " >= ";
    public static final String OP_LESS = " < ";
    public static final String OP_LESS_EQUAL = " <= ";
    public static final int DEFAULT_VALUE = -99;
  }

  public final class CONFIG_PROPERTY {

    public final static String TT_TYPE_TRANS = "TT.TYPE.TRANS";
    public final static String TT_TYPE_TRANS_NODE = "TT.TYPE.TRANS.NODE";
    public final static String TT_TYPE_TRAN_REASON = "TT.TYPE.TRAN.REASON";
    public final static String TT_REASON_DEVICE_ERR = "TT.REASON.DEVICE.ERR";
    public final static String TT_REASON_DEVICE_ERR_RESET = "TT.REASON.DEVICE.ERR.RESET";
    public final static String WO_TYPE_BDCD = "WO_TYPE_BDCD";
    //ThanhLV12_R13237_start
    public final static String WO_TYPE_BDNT = "WO_TYPE_BDNT";
    public final static String WO_TYPE_XLDTT = "WO_TYPE_XLDTT";
    //ThanhLV12_R13237_end
    public final static String WO_TYPE_CHECK_QLTS = "WO.TYPE.CHECK.QLTS";
    public final static String TS_DIEU_CHUYEN_NANG_CAP_CODE = "TS_DIEU_CHUYEN_NANG_CAP";
    public final static String TS_DIEU_CHUYEN_HA_CAP_CODE = "TS_DIEU_CHUYEN_HA_CAP";
    public final static String TS_NANG_CAP_TRAM_CODE = "TS_NANG_CAP_TRAM";
    public final static String TS_HA_CAP_TRAM_CODE = "TS_HA_CAP_TRAM";
    public final static String TS_THU_HOI_BAO_HANH_CODE = "TS_THU_HOI_BAO_HANH";
    public final static String TS_NANG_CAP_TRAM_UCTT_CODE = "TS_NANG_CAP_TRAM_UCTT";
    public static final String WO_TYPE_IP_PORT = "WO.TYPE.IP.PORT";

    public static final String SUB_ADMIN_EDIT = "SUB_ADMIN_EDIT";
    public static final String CR_PROCESS_DANGER = "CR_PROCESS_DANGER";
    public static final String WO_TEST_SERVICE = "WO.TEST.SERVICE";
    public final static String WO_TYPE_QLTS = "WO.TYPE.CHECK.QLTS";
    public final static String WO_TYPE_QLTS_NC = "WO.TYPE.CHECK.QLTS.NC";
    public final static String WO_TYPE_QLTS_NC_THUE = "WO.TYPE.CHECK.QLTS.NC.THUE";
    public final static String WO_TYPE_QLTS_NC_UCTT = "WO.TYPE.CHECK.QLTS.NC.UCTT";
    public final static String WO_TYPE_QLTS_NC_UCTT_THUE = "WO.TYPE.CHECK.QLTS.NC.UCTT.THUE";
    public final static String WO_TYPE_QLTS_THUHOI = "WO.TYPE.CHECK.QLTS.THUHOI";
    public final static String WO_TYPE_NOT_ALLOW_DELETE = "WO_TYPE_NOT_ALLOW_DELETE";
    public final static String WO_TYPE_QLTS_DC_HC = "WO.TYPE.CHECK.QLTS.DC.HC";
  }

  public static class SUB_ADMIN_EDIT_VIEW {

    public final static String AFFECTED_LEVEL = "affectedLevel";
    public final static String CONFIG_CHILD_ARRAY = "configChildArray";
    public final static String CONFIG_RETURN_ACTION = "configReturnAction";
    public final static String CONFIG_TEMP_IMPORT = "configTempImport";
    public final static String CONFIG_WEBSERVICE_IMPORT = "configWebserviceImport";
    public final static String CR_ACTION_CODE = "crActionCode";
    public final static String CR_ALARM_SETTING = "crAlarmSetting";
    public final static String CFG_CREATE_WO = "cfgCreateWo";
    public final static String CR_CAB_USERS = "crCabUsers";
    public final static String CR_IMPACT_FRAME = "crImpactFrame";
    public final static String DEPARTMENTS_ROLES_MANAGEMENT = "departmentsRolesManagement";
    public final static String DEPARTMENTS_SCOPE_MANAGEMENT = "CrManagerUnitScopeView";
    public final static String DEVICE_TYPE_MANAGEMENT = "deviceTypeManagement";
    public final static String GROUP_DEPARTMENT_CONFIG = "groupDepartmentConfig";
    public final static String GROUP_UNIT = "groupUnit";
    public final static String IMPACT_SEGMENT = "impactSegment";
    public final static String PROCESS_MANAGEMENT = "processManagement";
    public final static String ROLES_MANAGEMENT = "rolesManagement";
    public final static String ROLES_SCOPE_MANAGEMENT = "rolesScopeManagement";
    public final static String SCOPES_MANAGEMENT = "scopesManagement";
    public final static String SR_ROLE_USER = "srRoleUser";
    public final static String WO_FILE_TEMP = "woFileTemp";
    public final static String CFG_CLOSED_TICKET = "cfgClosedTicket";
    public final static String REQUIRE_HAVE_WO = "cfgRequireHaveWo";
    public final static String CFG_TROUBLE_CALL_IPCC = "CfgTroubleCallIpcc";
    public final static String IPCC_SERVICE = "IpccService";
    public final static String CAT_ITEM = "catItem";


    private static Map<String, String> getView = new HashMap<String, String>() {
      {
        put("AFFECTED_LEVEL", AFFECTED_LEVEL);
        put("CONFIG_CHILD_ARRAY", CONFIG_CHILD_ARRAY);
        put("CONFIG_RETURN_ACTION", CONFIG_RETURN_ACTION);
        put("CONFIG_TEMP_IMPORT", CONFIG_TEMP_IMPORT);
        put("CONFIG_WEBSERVICE_IMPORT", CONFIG_WEBSERVICE_IMPORT);
        put("CR_ACTION_CODE", CR_ACTION_CODE);
        put("CR_ALARM_SETTING", CR_ALARM_SETTING);
        put("CR_CAB_USERS", CR_CAB_USERS);
        put("CR_IMPACT_FRAME", CR_IMPACT_FRAME);
        put("DEPARTMENTS_ROLES_MANAGEMENT", DEPARTMENTS_ROLES_MANAGEMENT);
        put("DEPARTMENTS_SCOPE_MANAGEMENT", DEPARTMENTS_SCOPE_MANAGEMENT);
        put("DEVICE_TYPE_MANAGEMENT", DEVICE_TYPE_MANAGEMENT);
        put("GROUP_DEPARTMENT_CONFIG", GROUP_DEPARTMENT_CONFIG);
        put("GROUP_UNIT", GROUP_UNIT);
        put("IMPACT_SEGMENT", IMPACT_SEGMENT);
        put("PROCESS_MANAGEMENT", PROCESS_MANAGEMENT);
        put("ROLES_MANAGEMENT", ROLES_MANAGEMENT);
        put("ROLES_SCOPE_MANAGEMENT", ROLES_SCOPE_MANAGEMENT);
        put("SCOPES_MANAGEMENT", SCOPES_MANAGEMENT);
        put("SR_ROLE_USER", SR_ROLE_USER);
        put("CAT_ITEM", CAT_ITEM);
      }
    };

    public static Map<String, String> getGetView() {
      return getView;
    }

    public static void setGetView(Map<String, String> getView) {
      SUB_ADMIN_EDIT_VIEW.getView = getView;
    }

  }

  public static final String SUB_CATEGORY = "SUB_CATEGORY";
  public static final String NAME_IN = "NAME_IN";
  public static final String NUMBER = "NUMBER";
  public static final String EMAIL = "EMAIL";
  public static final String SMS = "SMS";
  public static final String KEDB_ALIAS = "KEDB_ALIAS";
  public static final String NAME_EQUAL = "NAME_EQUAL";
  public static final String NAME_LIKE = "NAME_LIKE";
  public static final String STRING = "STRING";
  public static final String ASC = "asc";
  public static final String TYPE_NUMBER = "LONG,INTEGER,SHORT,BYTE,INT,DOUBLE,FLOAT";
  public static final String NUMBER_DOUBLE = "DOUBLE";
  public static final String TYPE_DATE = "DATE";
  public static final String NAME_LESS_EQUAL = "NAME_LESS_EQUAL";
  public static final String NAME_GREATER_EQUAL = "NAME_GREATER_EQUAL";
  public static final String NAME_NOT_EQUAL = "NAME_NOT_EQUAL";
  public static final String NAME_LESS = "NAME_LESS";
  public static final String NAME_GREATER = "NAME_GREATER";

  public final class TT_PRIORITY {

    public final static String TT_Minor = "TT_Minor";
    public final static String TT_Critical = "TT_Critical";
    public final static String TT_Major = "TT_Major";
  }

  public static class PROCESS {

    public final static String WO_STATE = "1";
    public final static String TT_STATE = "2";
    public final static String PT_STATE = "3";
    public final static String CR_STATE = "4";
    public final static String MR_STATE = "5";
    public final static String ORDER_STATE = "175";
  }

  public static class CR_STATE {

    public final static Long DRAFT = 0L;
    public final static Long OPEN = 1L;
    public final static Long QUEUE = 2L;
    public final static Long COORDINATE = 3L;
    public final static Long EVALUATE = 4L;
    public final static Long APPROVE = 5L;
    public final static Long ACCEPT = 6L;
    public final static Long RESOLVE = 7L;
    public final static Long INCOMPLETE = 8L;
    public final static Long CLOSE = 9L;
    public final static Long CANCEL = 10L;
    public final static Long WAIT_CAB = 11L;
    public final static Long CAB = 12L;
    private static Map<Long, String> getStateName = new HashMap<Long, String>() {
      {
        put(DRAFT, "cr.state.draft");
        put(OPEN, "cr.state.open");
        put(QUEUE, "cr.state.queue");
        put(COORDINATE, "cr.state.coordinate");
        put(EVALUATE, "cr.state.evaluate");
        put(APPROVE, "cr.state.approve");
        put(ACCEPT, "cr.state.accept");
        put(RESOLVE, "cr.state.resolve");
        put(INCOMPLETE, "cr.state.incomplete");
        put(CLOSE, "cr.state.close");
        put(CANCEL, "cr.state.cancel");
        put(WAIT_CAB, "cr.state.wait_cab");
        put(CAB, "cr.state.cab");
      }

    };

    public static Map<Long, String> getGetStateName() {
      return getStateName;
    }

    public static void setGetStateName(Map<Long, String> getStateName) {
      CR_STATE.getStateName = getStateName;
    }
  }

  public static class WO_STATE {

    public final static Long DRAFT = 7L;
    public final static Long UNASSIGNED = 0L;
    public final static Long ASSIGNED = 1L;
    public final static Long REJECT = 2L;
    public final static Long DISPATCH = 3L;
    public final static Long ACCEPT = 4L;
    public final static Long INPROCESS = 5L;
    public final static Long CLOSED_FT = 6L;
    public final static Long CLOSED_CD = 8L;
    public final static Long PENDING = 9L;

    private static Map<Long, String> getStateName = new HashMap<>() {
      {
        put(DRAFT, "message.wo.status.DRAFT");
        put(UNASSIGNED, "message.wo.status.UNASSIGNED");
        put(ASSIGNED, "message.wo.status.ASSIGNED");
        put(REJECT, "message.wo.status.REJECT");
        put(DISPATCH, "message.wo.status.DISPATCH");
        put(ACCEPT, "message.wo.status.ACCEPT");
        put(INPROCESS, "message.wo.status.INPROCESS");
        put(CLOSED_FT, "message.wo.status.CLOSED_FT");
        put(CLOSED_CD, "message.wo.status.CLOSED_CD");
        put(PENDING, "message.wo.status.PENDING");
      }
    };

    public static Map<Long, String> getStateName() {
      return getStateName;
    }

    public static void setStateName(Map<Long, String> getStateName) {
      WO_STATE.getStateName = getStateName;
    }

  }

  public static class CR_ACTION_CODE {

    public final static Long ADDNEW = 0L;
    public final static Long UPDATE = 1L;
    public final static Long APPROVE = 2L;
    public final static Long REJECT = 3L;
    //        public final static Long CONSIDER = 4L;
    public final static Long CLOSE = 5L;
    public final static Long CLOSE_BY_MANAGER = 6L;
    public final static Long RETURN_TO_CREATOR_BY_MANAGER = 7L;
    public final static Long ASSIGN_TO_CONSIDER = 8L;
    public final static Long CHANGE_CR_TYPE = 9L;
    public final static Long CLOSE_BY_APPRAISER = 10L;
    public final static Long RETURN_TO_CREATOR_BY_APPRAISER = 11L;
    public final static Long RETURN_TO_MANAGER_BY_APPRAISER = 12L;
    public final static Long APPRAISE = 14L;
    public final static Long ASSIGN_TO_EMPLOYEE_APPRAISAL = 15L;
    public final static Long ACCEPT = 16L;
    public final static Long RETURN_TO_MANAGER_BY_IMPL = 17L;
    public final static Long RETURN_TO_APPRAISER_BY_IMPL = 18L;
    public final static Long RETURN_TO_CREATOR_BY_MANAGER_SCH = 19L;
    public final static Long RETURN_TO_APPRAISE_BY_MANAGER_SCH = 20L;
    public final static Long CLOSE_BY_MANAGER_SCH = 21L;
    public final static Long SCHEDULE = 22L;
    public final static Long ASSIGN_EXC_TO_EMPLOYEE = 23L;//giao thuc thi CR
    public final static Long RESOLVE = 24L;
    public final static Long CLOSECR = 25L;
    public final static Long CLOSECR_APPROVE_STD = 26L; // Đóng CR lúc duyệt Standard – phục vụ thằng (16)
    public final static Long RESOLVE_APPROVE_STD = 27L;// Resolve CR lúc duyệt Standard phục vụ thằng (13)(14)(15)
    public final static Long INCOMPLETE_APPROVE_STD = 28L;// Incomplate CR lúc duyệt Standard phục vụ thằng (16)
    public final static Long CLOSE_EXCUTE_STD = 29L;// phục vụ thằng (13)(14)(15)
    public final static Long ACCEPT_BY_MANAGER_PRE = 30L;
    public final static Long UPDATE_CR_WHEN_APPROVE_STD = 31L;// Sửa thông tin CR STANDARD khi phê duyệt
    public final static Long SAVEDRAFT = 32L;
    public final static Long CANCELCR = 33L;
    public final static Long UPDATE_CR_WHEN_RECEIVE_STD = 34L;// sua thong tin CR STANDARD khi tiep nhan
    public final static Long REJECT_EXCUTE_STD = 35L;// nhan vien tu choi nhan CR STANDARD
    public final static Long RETURN_TO_CREATOR_WHEN_EXCUTE_STD = 36L;// Tra ve dv tao khi tiep nhan thu thi CR
    public final static Long CLOSE_EXCUTE_EMERGENCY = 37L;// Tu choi CR khan, Dong CR
    public final static Long CHANGE_TO_CAB = 38L;
    public final static Long ASSIGN_TO_CAB = 39L;
    public final static Long CAB = 40L;
    public final static Long RETURN_TO_CREATOR_WHEN_CAB = 41L;
    public final static Long RETURN_TO_CONSIDER_WHEN_CAB = 42L;
    public final static Long RETURN_TO_MANAGE_WHEN_CAB = 43L;
    public final static Long EDIT_CR_BY_QLTD = 44L;
    public final static Long CHANGE_TO_SCHEDULE = 45L;
    public final static Long RETURN_TO_CAB_WHEN_SCHEDULE = 46L;
    public final static Long RETURN_TO_CREATOR_BY_MANAGER_CAB = 47L;
    public final static Long RETURN_TO_APPRAISE_BY_MANAGER_CAB = 48L;
    public final static Long CLOSE_BY_MANAGER_CAB = 49L;
    public final static Long RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO = 50L;

    private static Map<Long, String> getActionCodeName = new HashMap<Long, String>() {
      {

        put(ADDNEW, "cr.actioncode.addnew");
        put(UPDATE, "cr.actioncode.update");
        put(APPROVE, "cr.actioncode.approve");
        put(REJECT, "cr.actioncode.reject");
        put(CLOSE_BY_MANAGER, "cr.actioncode.close.by.manager");
        put(RETURN_TO_CREATOR_BY_MANAGER, "cr.actioncode.return.to.creator.by.manager");
        put(ASSIGN_TO_CONSIDER, "cr.actioncode.assign.to.consider");
        put(CHANGE_CR_TYPE, "cr.actioncode.change.type");
        put(CLOSE_BY_APPRAISER, "cr.actioncode.close.by.appraisal");
        put(RETURN_TO_CREATOR_BY_APPRAISER, "cr.actioncode.return.to.creator.by.appraisal");
        put(RETURN_TO_MANAGER_BY_APPRAISER, "cr.actioncode.return.to.manager.by.appraisal");
        put(APPRAISE, "cr.actioncode.consider");
        put(ASSIGN_TO_EMPLOYEE_APPRAISAL, "cr.actioncode.assign.to.empl");
        put(ACCEPT, "cr.actioncode.accept");
        put(RETURN_TO_MANAGER_BY_IMPL, "cr.actioncode.return.to.manager.by.empl");
        put(RETURN_TO_APPRAISER_BY_IMPL, "cr.actioncode.return.to.consider.by.empl");
        put(RETURN_TO_CREATOR_BY_MANAGER_SCH, "cr.actioncode.return.to.creator.by.manager.sch");
        put(RETURN_TO_APPRAISE_BY_MANAGER_SCH, "cr.actioncode.return.to.consider.by.manager.sch");
        put(CLOSE_BY_MANAGER_SCH, "cr.actioncode.close.by.manager.sch");
        put(SCHEDULE, "cr.actioncode.schedule");
        put(ASSIGN_EXC_TO_EMPLOYEE, "cr.actioncode.assign.excute.to.empl");
        put(RESOLVE, "cr.actioncode.resolve");
        put(CLOSECR, "cr.actioncode.close");
        put(CLOSECR_APPROVE_STD, "cr.actioncode.close");
        put(RESOLVE_APPROVE_STD, "cr.actioncode.resolve");
        put(INCOMPLETE_APPROVE_STD, "cr.actioncode.return.to.creator.by.approve");
        put(CLOSE_EXCUTE_STD, "cr.actioncode.close");
        put(ACCEPT_BY_MANAGER_PRE, "cr.actioncode.accept.by.manage.preapprove");
        put(UPDATE_CR_WHEN_APPROVE_STD, "cr.actioncode.updatecr.when.approve.std");
        put(SAVEDRAFT, "cr.actioncode.savedraft");
        put(CANCELCR, "cr.actioncode.cancel");
        put(UPDATE_CR_WHEN_RECEIVE_STD, "cr.actioncode.updatecr.when.receive.std");
        put(REJECT_EXCUTE_STD, "cr.actioncode.reject.when.receive.std");
        put(RETURN_TO_CREATOR_WHEN_EXCUTE_STD, "cr.actioncode.return.to.creator.by.excutor");
        put(CLOSE_EXCUTE_EMERGENCY, "cr.actioncode.close.emergency");
        put(CHANGE_TO_CAB, "cr.actioncode.change.to.cab");
        put(ASSIGN_TO_CAB, "cr.actioncode.assign.to.cab");
        put(CAB, "cr.actioncode.cab");
        put(RETURN_TO_CREATOR_WHEN_CAB, "cr.actioncode.return.to.creator.by.cap");
        put(RETURN_TO_CONSIDER_WHEN_CAB, "cr.actioncode.return.to.consider.by.cap");
        put(RETURN_TO_MANAGE_WHEN_CAB, "cr.actioncode.return.to.manage.by.cap");
        put(EDIT_CR_BY_QLTD, "cr.actioncode.edit.by.manage");
        put(CHANGE_TO_SCHEDULE, "cr.actioncode.change.to.schedule");
        put(RETURN_TO_CAB_WHEN_SCHEDULE, "cr.actioncode.return.to.cab.when.schedule");
        put(RETURN_TO_CREATOR_BY_MANAGER_CAB, "cr.actioncode.return.to.creator.by.manager.cab");
        put(RETURN_TO_APPRAISE_BY_MANAGER_CAB, "cr.actioncode.return.to.consider.by.manager.cab");
        put(CLOSE_BY_MANAGER_CAB, "cr.actioncode.close.by.manager.cab");
        put(RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO, "cr.actioncode.ResolveCrWithFailStatus.by.user");
      }
    };

    public static Map<Long, String> getGetActionCodeName() {
      return getActionCodeName;
    }

    public static void setGetActionCodeName(
        Map<Long, String> getActionCodeName) {
      CR_ACTION_CODE.getActionCodeName = getActionCodeName;
    }
  }

  public static class CR_SEARCH_TYPE {

    public final static Long LOOKUP = 0L;
    public final static Long APPROVE = 1L;
    public final static Long VERIFY = 2L;
    public final static Long CONSIDER = 3L;
    public final static Long SCHEDULE = 4L;
    public final static Long CREATE_EDIT = 5L;
    public final static Long EXCUTE = 6L;
    public final static Long RESOLVE = 7L;
    public final static Long CLOSE = 8L;
    public final static Long WAIT_CAB = 9L; // Cho cab
    public final static Long CAB = 10L; //Dang cab
    public final static Long Z78 = 11L; //Dieu hanh tac dong
    public final static Long QLTD = 12L; //Quan ly tac dong sua CR
    public final static Long QLTD_RENEW = 13L; // Gia han sau tiep nhan
  }

  public static class CR_ROLE {

    public final static String roleTP = "TP";
    public final static String roleTB = "TB";
    public final static String roleNV = "NV";
    public final static String roleCDK = "CDK";
    public final static String roleUQ = "UQ";
  }

  public static class CR_TYPE {

    public final static Long NORMAL = 0L;
    public final static Long EMERGENCY = 1L;
    public final static Long STANDARD = 2L;
  }

  public static interface CR_OUTDATE_TYPE {

    public final static String ONTIME = "ontime";
    public final static String OUTOFDATE = "outofdate";
    public final static String TOTAL = "total";
  }

  public static class CR_ACTION {

    public final static String IS_EDIT = "IS_EDIT";
    public final static String IS_VIEW = "IS_VIEW";

  }

  public static class CR_APPROVAL_STATUS {

    public final static Long WAITTING = 0L;
    public final static Long APPROVED = 1L;
    public final static Long REJECTED = 2L;
    Map<Long, String> getApprovalStatus = new HashMap<>() {
      {
        put(WAITTING, "cr.approval.status.waitting");
        put(APPROVED, "cr.approval.status.approved");
        put(REJECTED, "cr.approval.status.rejected");
      }
    };
  }


  public static class CR_ACTION_RIGHT {

    public final static String LOOKUP_ONLY = "0";
    public final static String CAN_EDIT = "1";
    public final static String CAN_APPROVE = "2";
    public final static String CAN_VERIFY = "3";
    public final static String CAN_CONSIDER = "4";
    public final static String CAN_SCHEDULE = "5";
    public final static String CAN_RECEIVE = "6";
    public final static String CAN_RESOLVE = "7";
    public final static String CAN_CLOSE = "8";
    public final static String CAN_CONSIDER_NO_APPRAISE = "9";
    public final static String CAN_CONSIDER_NO_ASSIGNEE = "10";
    public final static String CAN_RECEIVE_NO_ACCEPT = "11";
    public final static String CAN_RECEIVE_NO_ASSIGNEE = "12";
    public final static String CAN_RECEIVE_STANDARD = "13"; // được: đóng CR/Tiếp nhận CR/Giao nhân viên CR | Tiếp nhận CR chuẩn mức trưởng đơn vị full quyền (1)
    public final static String CAN_RECEIVE_STANDARD_NO_ACCEPT = "14";//được: đóng CR/Giao nhân viên CR| Tiếp nhận CR chuẩn mức trưởng đơn vị không tiếp nhận (2)
    public final static String CAN_RECEIVE_STANDARD_NO_ASSIGNEE = "15";//được: đóng CR/Tiếp nhận CR| Tiếp nhận CR chuẩn mức nhân viên không giao cho ai (3)
    public final static String CAN_APPROVE_STANDARD = "16";// Được: duyệt CR/ Cập nhật CR/CLose CR/Resolve CR/Incomplete CR | Phê duyệt CR Chuẩn (4)
    public final static String CAN_RECEIVE_EMR = "17";//Được giao/nhận CR/YC sắp lịch lại| Tiếp nhận CR khẩn mức trưởng đơn vị full quyền (5)
    public final static String CAN_RECEIVE_EMR_NO_ACCEPT = "18";//Giao thực thi/YC sắp lịch lại| Tiếp nhận CR khẩn mức trưởng đơn vị không tiếp nhận(6)
    public final static String CAN_RECEIVE_EMR_NO_ASSIGNEE = "19";//Nhận thực thi CR/YC sắp lịch lại| Tiếp nhận CR khẩn mức nhân viên không giao cho ai(7)
    public final static String CAN_SCHEDULE_EMR = "20";//Được sắp lịch thực hiện CR (không cho phép trả về đơn vị nào) | Sắp lịch CR khẩn(8)
    public final static String CAN_SCHEDULE_PREAPPROVE = "21";//Được sắp lịch thực hiện CR preApprove (không cho phép trả về đơn vị tham dinh)
    public final static String CAN_RECEIVE_PREAPPROVE = "22";//Tiếp nhận CR, giao nhân viên, trả về đơn vị QLTD, không trả về đơn vị thẩm định
    public final static String CAN_RECEIVE_PREAPPROVE_NO_ACCEPT = "23";// Giao nhân viên, trả về đơn vị QLTD, không trả về đơn vị thẩm định, không tiếp nhận
    public final static String CAN_RECEIVE_PREAPPROVE_NO_ASSIGNEE = "24";// Tiếp nhận CR, tiếp nhận, trả về đơn vị QLTD, không trả về đơn vị thẩm định, không giao việc
    public final static String CAN_VERIFY_PREAPPROVE = "25";// CLose CR, trả CR về đơn vị tạo/Chuyển loại CR/ Xác nhận thực thi CR (chuyển CR về EVALUATE)
    public final static String CAN_ONLY_ADDWORKLOG = "26";// CLose CR, trả CR về đơn vị tạo/Chuyển loại CR/ Xác nhận thực thi CR (chuyển CR về EVALUATE)
    public final static String CAN_ONLY_REASSIGN = "27";//Khi da co nhan vien tiep nhan va thoi gian hien tai < start_time CR thi cho phep truong phong re-assign viec cho nhan vien
    public final static String CAN_ASSIGN_CAB = "28";
    public final static String CAN_CAB = "29";
    public final static String CAN_EDIT_CR_BY_QLTD = "30";
    public final static String RENEW_CR_BY_QLTD = "31";
  }

  public static class CR_FILE_TYPE {

    public final static String DT_EXECUTE = "1";
    public final static String DT_ROLLBACK = "2";
    public final static String DT_SCRIPT = "3";
    public final static String FORM_TEST_SERVICE = "4";
    public final static String CHECK_KPI = "5";
    public final static String PLAN = "6";
    public final static String GUILINE = "7";
    public final static String AFFECTED = "8";
    public final static String RESULT = "9";
    public final static String LOG_IMPACT = "10";
    public final static String OTHER = "100";
    public final static String IMPORT_BY_PROCESS_IN = "101";
    public final static String IMPORT_BY_PROCESS_OUT = "102";
    public final static String IMPORT_RESULT = "103";
    private final static Map<String, String> getText = new HashMap<>() {
      {
        put(DT_EXECUTE, "cr.file.dtExecute");
        put(DT_ROLLBACK, "cr.file.dtRollback");
        put(DT_SCRIPT, "cr.file.dtDtScript");
        put(FORM_TEST_SERVICE, "cr.file.formTestService");
        put(CHECK_KPI, "cr.file.formCheckKpi");
        put(PLAN, "cr.file.plan");
        put(GUILINE, "cr.file.guiline");
        put(AFFECTED, "cr.file.affected");
        put(RESULT, "cr.file.result");
        put(LOG_IMPACT, "cr.file.impact.log");
        put(OTHER, "cr.file.other");
        put(IMPORT_BY_PROCESS_IN, "cr.file.form.by.process");
        put(IMPORT_BY_PROCESS_OUT, "cr.file.form.by.process");
      }
    };

    public static Map<String, String> getGetText() {
      return getText;
    }
  }


  public interface IPCC_CODE {

    String VNM = "IPCC_VN";
    String VTP = "IPCC_VTP";
    String VTZ = "IPCC_VTZ";
    String STL = "IPCC_STL";
    String MVT = "IPCC_MVT";
  }

  public static interface CR_RETURN_MESSAGE {

    public final static String OK = "OK";
    public final static String NOK = "NOK";
    public final static String SUCCESS = "SUCCESS";
    public final static String ERROR = "ERROR";
    public final static String ERRORFORMAT = "ERRORFORMAT";
    public final static String MUSTCLOSEALLWO = "MUSTCLOSEALLWO";

    public final static String APPROVEINFIRSTPLACE = "APPROVEINFIRSTPLACE";
    public final static String APPROVECRINFIRSTPLACE = "APPROVECRINFIRSTPLACE";
    public final static String BLANKFILE = "BLANKFILE";
    public final static String WARNING = "WARNING";
  }

  public static class VSMART_NEED_SUPPORT {

    public final static Long YES = 1L;
    public final static Long NO = 0L;
  }

  public final class WO_STATUS {

    public final static String DRAFT = "7";
    public final static String UNASSIGNED = "0";
    public final static String ASSIGNED = "1";
    public final static String REJECT = "2";
    public final static String DISPATCH = "3";
    public final static String ACCEPT = "4";
    public final static String INPROCESS = "5";
    public final static String CLOSED_FT = "6";
    public final static String CLOSED_CD = "8";
    public final static String PENDING = "9";

    public final static String OVERDUE = "100";
    public final static String GOING_OVERDURE = "101";

    public final static String HOTVIP = "102";
    public final static String GOING_OVERTIME = "103";
    public final static String BUSINESS_CUSTOMER = "105";
  }

  public interface WO_STATUS_NAME {

    String DRAFT = I18n.getString("message.wo.status.DRAFT");
    String UNASSIGNED = I18n.getString("message.wo.status.UNASSIGNED");
    String ASSIGNED = I18n.getString("message.wo.status.ASSIGNED");
    String REJECT = I18n.getString("message.wo.status.REJECT");
    String ACCEPT = I18n.getString("message.wo.status.ACCEPT");
    String DISPATCH = I18n.getString("message.wo.status.DISPATCH");
    String INPROCESS = I18n.getString("message.wo.status.INPROCESS");
    String CLOSED = I18n.getString("message.wo.status.CLOSED");
    String CLOSED_CD = I18n.getString("message.wo.status.CLOSED_CD");
    String CLOSED_FT = I18n.getString("message.wo.status.CLOSED_FT");
    String PENDING = I18n.getString("message.wo.status.PENDING");
  }

  public interface WO_RESULT {

    String NOK_CLOSE = "0";
    String NOK_DISPATCH = "1";
    String OK = "2";
    String NOT_APPROVED = "3";
  }

  public interface CREATE_WO_TYPE {

    public String ASSIGN_STAFF = "0";
    public String ASSIGN_OUTSOURCE = "1";
    public String ASSIGN_BOTH = "2";
  }

  public interface WO_BUSINESS_CHECK {

    String AUTO_CHECKED_WHEN_APPROVE_SPM = "AUTO_CHECKED_WHEN_APPROVE_SPM";
    String COMPLETED_FIRST_NEED_SUPPORT = "COMPLETED_FIRST_NEED_SUPPORT";
    String CHECK_TS_THU_HOI = "CHECK_TS_THU_HOI";
    String TIME_APPROVE_WO_SPM = "TIME_APPROVE_WO_SPM";
    String CHECK_STATUS_APPROVE_WO_SPM = "CHECK_STATUS_APPROVE_WO_SPM";
    String CHECK_WO_TYPE_NOT_ALLOW_APPROVE = "CHECK_WO_TYPE_NOT_ALLOW_APPROVE";
  }

  public final static class WO_STATUS_LABEL {

    public static final String UNASSIGNED = "UNASSIGNED";
    public static final String ASSIGNED = "ASSIGNED";
    public static final String REJECT = "REJECT";
    public static final String DISPATCH = "DISPATCH";
    public static final String ACCEPT = "ACCEPT";
    public static final String INPROCESS = "INPROCESS";
    //    public static final String CLOSED = "CLOSED";
    public static final String CLOSED_FT = "CLOSED_FT";
    public static final String CLOSED_CD = "CLOSED_CD";
    public static final String DRAFT = "DRAFT";
    public static final String PENDING = "PENDING";
  }

  public static abstract class WO_AUTO_CHECK_STATUS {

    public static final Long WAITING = 1L;
    public static final Long SENT = 2L;
    public static final Long OK = 3L;
    public static final Long NOK = 4L;
    public static final Long UNABLE_TO_CHECK = 5L;
    public static final Long CLOSE = 6L;
  }

  public final class WO_STATUS_NEW {

    public static final String UNASSIGNED = "0";
    public static final String ASSIGNED = "1";
    public static final String REJECT = "2";
    public static final String DISPATCH = "3";
    public static final String ACCEPT = "4";
    public static final String INPROCESS = "5";
    public static final String CLOSED_FT = "6";
    public static final String CLOSED_CD = "8";
    public final static String DRAFT = "7";
    public static final String PENDING = "9";
  }

  public static class SYSTEM {

    public static final Long COMMON = 0L;
    public static final Long CR = 1L;
    public static final Long MR = 2L;
    public static final Long PT = 3L;
    public static final Long TT = 4L;
    public static final Long WO = 5L;
  }

  /**
   *
   */
  public static class CAB {

    public static final Long CAB = 1L;
    public static final Long Z78 = 2L;
  }

  //AnhLP add
  public static class CR_PRIORITY {

    public final static Long IMEDIATELY = 3L;
    public final static Long HIGH = 0L;
    public final static Long MEDIUM = 1L;
    public final static Long LOW = 2L;
    private static Map<Long, String> getPriorityName = new HashMap<Long, String>() {
      {
        put(HIGH, "cr.priority.high");
        put(MEDIUM, "cr.priority.medium");
        put(LOW, "cr.priority.low");
        put(IMEDIATELY, "cr.priority.imediately");
      }
    };

    public static Map<Long, String> getGetPriorityName() {
      return getPriorityName;
    }
  }


  public interface WO_SYSTEM_CODE {

    public final static String WFM_FT = "WFM-FT";
    public final static String WFM_OTHERS = "WFM-OTHERS";
    public final static String SPM = "SPM";
    public final static String CR = "CR";
  }

  // duongnt
  public static interface CR_NODE_TYPE {

    public final static String IMPACTED = "0";//Node mang tac dong
    public final static String AFFECTED = "1";//Node mang anh huong
    public final static String EFFECT = "0";//Node mang tac dong
  }

  public interface INFRA_TYPE {

    String CCN = "CCN";
    String FCN = "FCN";
    String CATV = "CATV";
    String GPON = "GPON";
    String AON = "AON";
    String N_A = "N/A";
  }

  public interface INFRA_TYPE_ID {

    Long CCN = 1L;
    Long FCN = 3L;
    Long CATV = 2L;
    Long GPON = 4L;
    Long N_A = 0L;
  }

  public static interface ACTION_TYPE {

    public static final String VIEW = "1";
    public static final String UPDATE = "2";
    public static final String ADDNEW = "3";
    public static final String CLONE = "4";
  }

  public static class CR_RELATED {

    public final static String NON_RELATED = "0";
    public final static String PRIMARY = "1";
    public final static String SECONDARY = "2";
    public final static String PRE_APPROVE = "3";
    public final static String IS_PRE_APPROVE = "4";
  }

  public final class WORK_LOG_CAT {

    public final static String WLAY_ID = "wlayId";
    public final static String WLAY_NAME = "wlayName";
    public final static String WLAY_CODE = "wlayCode";
    public final static String WLAY_TYPE = "wlayType";
    public final static String WLAY_DESCRIPTION = "wlayDescription";
    public final static String WLAY_IS_ACTIVE = "wlayIsActive";
    public final static String WLAY_ID_HTTD = "79";
  }

  public final class WORK_LOG_SYSTEM {

    public final static String MR = "1";
    public final static String CR = "2";

  }

  public final class CR_FILE_ATTACH {

    public final static String FILE_KPI = "5";
    public final static String FILE_DT = "1";
    public final static String FILE_TEST = "4";
    public final static String FILE_ROLL = "2";
    public final static String FILE_PLANT = "6";
    public final static String FILE_IMPACT = "3";
    public final static String FILE_FORM = "7";
    public final static String FILE_MUTIL_FILE = "8";
    public final static String FILE_TXT = "11";
    public final static String FILE_OTHER = "100";
    public final static String LOG_IMPACT = "10";
    public static final String CR_IMPACTED_NODE_TXT = "CR_IMPACTED_NODE_TXT";
  }

  public static class TEMP_IMPORT_UTILS {

    public static final Long STRING = 3L;
    public static final Long INTEGER = 3L;
    public static final Long LONG = 3l;
    public static final Long DOUBLE = 4l;
    public static final Long FLOAT = 5l;
    public static final Long BOOLEAN = 6l;
    public static final Long CHAR = 7l;
    public static final String ID_KEY = "1.taskId_fileId_fileNumber_row";
    public static final String RETURN_VALUE_KEY = "2.validate_cr_ws";
  }

  public class EXCEL_PARAM {

    public static final int ROW_TITLE = 0;
    public static final int ROW_HEADER = 3;
    public static final int ROW_DATA = 4;
    public static final int ROW_ALERT = 2;
  }

  public static class IMPORT_FILE_TYPE {

    public static final Long INPUT = 0L;
    public static final Long IS_VALIDATE_INPUT = 1L;
    public static final Long IS_VALIDATE_OUTPUT = 2L;

  }

  public interface NATION_CODES {

    String STL = "STL";
    String VTP = "VTP";
    String NCM = "NCM";
    String VCR = "VCR";
    String MVT = "MVT";
    String VTB = "VTB";
    String VTZ = "VTZ";
    String MYT = "MYT";
    String VTC = "VTC";
    String VNM = "VNM";
    String VTL = "VTL";
  }

  public static class NATION_CODE {

    public final static Long NAT_NCM = 289729L;
    public final static Long NAT_NCM_NEW = 2000289729L;
    public final static Long MVT = 289724L;
    public final static Long MVT_NEW = 3000289724L;
    public final static Long VTB = 289726L;
    public final static Long VTB_NEW = 3500289726L;
    public final static Long VTL = 289723L;
    public final static Long VTL_NEW = 6000289723L;
    public final static Long MYT = 300656L;
    public final static Long MYT_NEW = 4500000001L;
    public final static Long VTZ = 289727L;
    public final static Long VTZ_NEW = 4001000000L;
    public final static Long VNM = 281L;
    public final static Long VTP = 289728L;
    public final static Long VTP_NEW = 1500289728L;
    public final static Long VTC = 289721L;
    public final static Long VTC_NEW = 1000014581L;
    public final static Long STL = 289722L;
    public final static Long STL_NEW = 5000289722L;
    public final static Long VCR = 289725L;

    private static Map<Long, String> getText = new HashMap<Long, String>() {
      {
        put(NAT_NCM, "NCM");
        put(NAT_NCM_NEW, "NCM");
        put(MVT, "MVT");
        put(MVT_NEW, "MVT");
        put(VTB, "VTB");
        put(VTB_NEW, "VTB");
        put(VTL, "VTL");
        put(VTL_NEW, "VTL");
        put(MYT, "MYT");
        put(MYT_NEW, "MYT");
        put(VTZ, "VTZ");
        put(VTZ_NEW, "VTZ");
        put(VNM, "VNM");
        put(VTP, "VTP");
        put(VTP_NEW, "VTP");
        put(VTC, "VTC");
        put(VTC_NEW, "VTC");
        put(STL, "STL");
        put(STL_NEW, "STL");
        put(VCR, "VCR");
      }

    };

    public static Map<Long, String> getGetText() {
      return getText;
    }

    public static void setGetText(Map<Long, String> getText) {
      NATION_CODE.getText = getText;
    }
  }

  public static interface CR_AFFECTED_SERVICE {

    public final static String UDCNTT_CODE = "COMMON_Udcntt";

  }

  public final static String FILE_WO_ID = "1";
  public final static String FILE_CR_ID = "2";
  public final static String SPLIT2 = "|:|";

  public final class CAT_ITEM {

    public final static String FILE_KPI_CR_REQUIRED = "FILE_KPI_CR_REQUIRED";
  }

  public static interface WORKLOG_CAT {

    public final static String CR_RESOLVE_CODE = "W00015";
    public final static String CR_CAB_CODE = "WC007";
    public final static String CR_NO_WO_CODE = "W00019";
    public final static String CR_FO_OVERDUE = "W000152";
  }

  public static class WO_TYPE_SEARCH {

    public static final int IS_FT = 1;
    public static final int IS_CD = 2;
    public static final int IS_PROVINCE = 3;
  }

  public static class NOTIFY_STATE {

    public static final Long UNREAD = 0L;
    public static final Long READ = 1L;
  }

  public static class NOTIFY_TYPE {

    public static final String APPROVE_DEPT = "approve_dept";
    public static final String VERIFY_DEPT = "verify_dept";
    public static final String WAIT_FOR_CONSIDER_DEPT = "wait_for_consider_dept";
    public static final String WAIT_FOR_CONSIDER_USER = "wait_for_consider_user";
    public static final String WAIT_FOR_SCHEDULE_DEPT = "wait_for_schedule_dept";
    public static final String WAIT_FOR_SCHEDULE_USER = "wait_for_schedule_user";
    public static final String WAIT_FOR_RECEIVE_DEPT = "wait_for_receive_dept";
    public static final String WAIT_FOR_RECEIVE_USER = "wait_for_receive_user";
    public static final String RECEIVED_DEPT = "received_dept";
    public static final String RECEIVED_USER = "received_user";
    public static final String WAIT_FOR_CLOSE_DEPT = "wait_for_close_dept";
    public static final String WAIT_FOR_CLOSE_USER = "wait_for_close_user";
    public static final String INCOMPLETE_DEPT = "incomplete_dept";
    public static final String INCOMPLETE_USER = "incomplete_user";
    public static final String APPROVE_OUT_OF_DATE = "approve_out_of_date";
    public static final String APPROVE_STILL_IN_TIME = "approve_still_intime";
    public static final String VERIFY_IN_TIME = "verify_in_time";
    public static final String VERIFY_OUT_OF_DATE = "verify_out_of_date";
    public static final String WAITING_FOR_CONSIDER_INTIME = "waiting_for_consider_intime";
    public static final String WAITING_FOR_CONSIDER_OUT_OF_DATE = "waiting_for_consider_out_of_date";
    public static final String WAITING_FOR_SCHEDULE_INTIME = "waiting_for_schedule_intime";
    public static final String WAITING_FOR_SCHEDULE_OUT_OF_DATE = "waiting_for_schedule_out_of_date";
    public static final String WAITING_FOR_RECEIVE = "waiting_for_receive";
    public static final String WAITING_FOR_RESOLVE = "waiting_for_resolve";
    public static final String WAITING_FOR_CLOSE = "waiting_for_close";
    public static final String APPROVE_OUT_OF_DATE_STANDARD = "approve_out_of_date_standard";
    public static final String APPROVE_STILL_IN_TIME_STANDARD = "approve_still_intime_standard";
    public static final String VERIFY_IN_TIME_STANDARD = "verify_in_time_standard";
    public static final String VERIFY_OUT_OF_DATE_STANDARD = "verify_out_of_date_standard";
    public static final String WAITING_FOR_CONSIDER_INTIME_STANDARD = "waiting_for_consider_intime_standard";
    public static final String WAITING_FOR_CONSIDER_OUT_OF_DATE_STANDARD = "waiting_for_consider_out_of_date_standard";
    public static final String WAITING_FOR_SCHEDULE_INTIME_STANDARD = "waiting_for_schedule_intime_standard";
    public static final String WAITING_FOR_SCHEDULE_OUT_OF_DATE_STANDARD = "waiting_for_schedule_out_of_date_standard";
    public static final String WAITING_FOR_RECEIVE_STANDARD = "waiting_for_receive_standard";
    public static final String WAITING_FOR_RESOLVE_STANDARD = "waiting_for_resolve_standard";
    public static final String WAITING_FOR_CLOSE_STANDARD = "waiting_for_close_standard";
    public static final String APPROVE_OUT_OF_DATE_EMG = "approve_out_of_date_emg";
    public static final String APPROVE_STILL_IN_TIME_EMG = "approve_still_intime_emg";
    public static final String VERIFY_IN_TIME_EMG = "verify_in_time_emg";
    public static final String VERIFY_OUT_OF_DATE_EMG = "verify_out_of_date_emg";
    public static final String WAITING_FOR_CONSIDER_INTIME_EMG = "waiting_for_consider_intime_emg";
    public static final String WAITING_FOR_CONSIDER_OUT_OF_DATE_EMG = "waiting_for_consider_out_of_date_emg";
    public static final String WAITING_FOR_SCHEDULE_INTIME_EMG = "waiting_for_schedule_intime_emg";
    public static final String WAITING_FOR_SCHEDULE_OUT_OF_DATE_EMG = "waiting_for_schedule_out_of_date_emg";
    public static final String WAITING_FOR_RECEIVE_EMG = "waiting_for_receive_emg";
    public static final String WAITING_FOR_RESOLVE_EMG = "waiting_for_resolve_emg";
    public static final String WAITING_FOR_CLOSE_EMG = "waiting_for_close_emg";
    public static final String TOTAL_CAB = "TOTAL_CAB";
    public static final String TOTAL_CAB_STANDARD = "TOTAL_CAB_STANDARD";
    public static final String TOTAL_CAB_EMG = "TOTAL_CAB_EMG";

    public static final String TOTAL_APPROVE = "TOTAL_APPROVE";
    public static final String TOTAL_APPROVE_STANDARD = "TOTAL_APPROVE_STANDARD";
    public static final String TOTAL_APPROVE_EMG = "TOTAL_APPROVE_EMG";
    public static final String TOTAL_CONSIDER = "TOTAL_CONSIDER";
    public static final String TOTAL_CONSIDER_STANDARD = "TOTAL_CONSIDER_STANDARD";
    public static final String TOTAL_CONSIDER_EMG = "TOTAL_CONSIDER_EMG";
    public static final String TOTAL_VERIFY = "TOTAL_VERIFY";
    public static final String TOTAL_VERIFY_STANDARD = "TOTAL_VERIFY_STANDARD";
    public static final String TOTAL_VERIFY_EMG = "TOTAL_VERIFY_EMG";
    public static final String TOTAL_SCHEDULE = "TOTAL_SCHEDULE";
    public static final String TOTAL_SCHEDULE_STANDARD = "TOTAL_SCHEDULE_STANDARD";
    public static final String TOTAL_SCHEDULE_EMG = "TOTAL_SCHEDULE_EMG";
    public static final String TOTAL_RECEIVE = "TOTAL_RECEIVE";
    public static final String TOTAL_RECEIVE_STANDARD = "TOTAL_RECEIVE_STANDARD";
    public static final String TOTAL_RECEIVE_EMG = "TOTAL_RECEIVE_EMG";
    public static final String TOTAL_RESOLVE = "TOTAL_RESOLVE";
    public static final String TOTAL_RESOLVE_STANDARD = "TOTAL_RESOLVE_STANDARD";
    public static final String TOTAL_RESOLVE_EMG = "TOTAL_RESOLVE_EMG";
    public static final String TOTAL_CLOSE = "TOTAL_CLOSE";
    public static final String TOTAL_CLOSE_STANDARD = "TOTAL_CLOSE_STANDARD";
    public static final String TOTAL_CLOSE_EMG = "TOTAL_CLOSE_EMG";

    public static final String TOTAL_WAIT_CAB = "TOTAL_WAIT_CAB";
    public static final String TOTAL_WAIT_CAB_STANDARD = "TOTAL_WAIT_CAB_STANDARD";
    public static final String TOTAL_WAIT_CAB_EMG = "TOTAL_WAIT_CAB_EMG";

  }

  public interface SERVICE {

    public static final Long MULTICREEN = 18L;
    public static final String MST_1C = "MST_1C";
    public static final String MST_2C = "MST_2C";
    public static final String PRODUCT_NOT_CHECK_QR = "PRODUCT_NOT_CHECK_QR_CODE";
  }

  public final class SR_CATALOG {

    public static final String SERVICE_STATUS = "A";
    public static final String SERVICE_ARRAY = "SERVICE_ARRAY";
    public static final String SERVICE_GROUP = "SERVICE_GROUP";
    public static final String DICH_VU_DAC_BIET = "DICH_VU_DAC_BIET";
    public static final String PARENT_GROUP_DICH_VU_TRUNG_KE = "DICH_VU_TRUNG_KE";
    public static final String PARENT_GROUP_AUTO_CREATE_CR = "CR_MAPPING_SR";
    public static final String PARENT_GROUP_AUTO_CREATE_CR_MAPPING = "CR_MAPPING_SR_AUTO";
    public static final String DICH_VU_TRUNG_KE_SDH = "DICH_VU_TRUNG_KE_SDH";
    public static final String DICH_VU_TRUNG_KE_METRO = "DICH_VU_TRUNG_KE_METRO";
    public static final String DICH_VU_WO_HELP = "DICH_VU_WO_HELP";
    public static final String PARENT_GROUP_AUTO_CREATE_WO = "AUTO_CREATE_WO";


    public static final String DV_TRUNG_KE_CR1_TITLE = "DV_TRUNG_KE_CR1_TITLE";
    public static final String DV_TRUNG_KE_CR1_DESCRIPTION = "DV_TRUNG_KE_CR1_DESCRIPTION";
    public static final String DV_TRUNG_KE_CR1_TYPE = "DV_TRUNG_KE_CR1_TYPE";
    public static final String DV_TRUNG_KE_CR1_IMPACT_SEGMENT = "DV_TRUNG_KE_CR1_IMPACT_SEGMENT";
    public static final String DV_TRUNG_KE_CR1_SUBCATEGORY = "DV_TRUNG_KE_CR1_SUBCATEGORY";
    public static final String DV_TRUNG_KE_CR1_PROCESS_TYPE_ID = "DV_TRUNG_KE_CR1_PROCESS_TYPE_ID";
    public static final String DV_TRUNG_KE_CR1_RISK = "DV_TRUNG_KE_CR1_RISK";
    public static final String DV_TRUNG_KE_CR1_DEVICE_TYPE = "DV_TRUNG_KE_CR1_DEVICE_TYPE";
    public static final String DV_TRUNG_KE_CR1_PRIORITY = "DV_TRUNG_KE_CR1_PRIORITY";
    public static final String DV_TRUNG_KE_CR1_DUTY_TYPE = "DV_TRUNG_KE_CR1_DUTY_TYPE";
    public static final String DV_TRUNG_KE_CR1_SERVICE_AFFECTING = "DV_TRUNG_KE_CR1_SERVICE_AFFECTING";
    public static final String DV_TRUNG_KE_CR1_IMPACT_AFFECT = "DV_TRUNG_KE_CR1_IMPACT_AFFECT";
    public static final String DV_TRUNG_KE_CR1_STATE = "DV_TRUNG_KE_CR1_STATE";
    public static final String DV_TRUNG_KE_CR1_CHANGE_ORGINATOR = "DV_TRUNG_KE_CR1_CHANGE_ORGINATOR";
    public static final String DV_TRUNG_KE_CR1_CHANGE_ORGINATOR_UNIT = "DV_TRUNG_KE_CR1_CHANGE_ORGINATOR_UNIT";
    public static final String DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV1 = "DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV1";
    public static final String DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV2 = "DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV2";
    public static final String DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV3 = "DV_TRUNG_KE_CR1_CHANGE_RESPONSIBLE_UNIT_KV3";

    public static final String DV_TRUNG_KE_CR2_TITLE = "DV_TRUNG_KE_CR2_TITLE";
    public static final String DV_TRUNG_KE_CR2_DESCRIPTION = "DV_TRUNG_KE_CR2_DESCRIPTION";
    public static final String DV_TRUNG_KE_CR2_DESCRIPTION2 = "DV_TRUNG_KE_CR2_DESCRIPTION2";
    public static final String DV_TRUNG_KE_CR2_TYPE = "DV_TRUNG_KE_CR2_TYPE";
    public static final String DV_TRUNG_KE_CR2_IMPACT_SEGMENT = "DV_TRUNG_KE_CR2_IMPACT_SEGMENT";
    public static final String DV_TRUNG_KE_CR2_SUBCATEGORY = "DV_TRUNG_KE_CR2_SUBCATEGORY";
    public static final String DV_TRUNG_KE_CR2_PROCESS_TYPE_ID = "DV_TRUNG_KE_CR2_PROCESS_TYPE_ID";
    public static final String DV_TRUNG_KE_CR2_RISK = "DV_TRUNG_KE_CR2_RISK";
    public static final String DV_TRUNG_KE_CR2_DEVICE_TYPE = "DV_TRUNG_KE_CR2_DEVICE_TYPE";
    public static final String DV_TRUNG_KE_CR2_PRIORITY = "DV_TRUNG_KE_CR2_PRIORITY";
    public static final String DV_TRUNG_KE_CR2_DUTY_TYPE = "DV_TRUNG_KE_CR2_DUTY_TYPE";
    public static final String DV_TRUNG_KE_CR2_SERVICE_AFFECTING = "DV_TRUNG_KE_CR2_SERVICE_AFFECTING";
    public static final String DV_TRUNG_KE_CR2_IMPACT_AFFECT = "DV_TRUNG_KE_CR2_IMPACT_AFFECT";
    public static final String DV_TRUNG_KE_CR2_STATE = "DV_TRUNG_KE_CR2_STATE";
    public static final String DV_TRUNG_KE_CR2_CHANGE_ORGINATOR = "DV_TRUNG_KE_CR2_CHANGE_ORGINATOR";
    public static final String DV_TRUNG_KE_CR2_CHANGE_ORGINATOR_UNIT = "DV_TRUNG_KE_CR2_CHANGE_ORGINATOR_UNIT";
    public static final String DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV1 = "DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV1";
    public static final String DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV2 = "DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV2";
    public static final String DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV3 = "DV_TRUNG_KE_CR2_CHANGE_RESPONSIBLE_UNIT_KV3";

    public static final String DV_TRUNG_KE_CR3_TITLE = "DV_TRUNG_KE_CR3_TITLE";
    public static final String DV_TRUNG_KE_CR3_DESCRIPTION = "DV_TRUNG_KE_CR3_DESCRIPTION";
    public static final String DV_TRUNG_KE_CR3_TYPE = "DV_TRUNG_KE_CR3_TYPE";
    public static final String DV_TRUNG_KE_CR3_IMPACT_SEGMENT = "DV_TRUNG_KE_CR3_IMPACT_SEGMENT";
    public static final String DV_TRUNG_KE_CR3_SUBCATEGORY = "DV_TRUNG_KE_CR3_SUBCATEGORY";
    public static final String DV_TRUNG_KE_CR3_PROCESS_TYPE_ID = "DV_TRUNG_KE_CR3_PROCESS_TYPE_ID";
    public static final String DV_TRUNG_KE_CR3_RISK = "DV_TRUNG_KE_CR3_RISK";
    public static final String DV_TRUNG_KE_CR3_DEVICE_TYPE = "DV_TRUNG_KE_CR3_DEVICE_TYPE";
    public static final String DV_TRUNG_KE_CR3_PRIORITY = "DV_TRUNG_KE_CR3_PRIORITY";
    public static final String DV_TRUNG_KE_CR3_DUTY_TYPE = "DV_TRUNG_KE_CR3_DUTY_TYPE";
    public static final String DV_TRUNG_KE_CR3_SERVICE_AFFECTING = "DV_TRUNG_KE_CR3_SERVICE_AFFECTING";
    public static final String DV_TRUNG_KE_CR3_IMPACT_AFFECT = "DV_TRUNG_KE_CR3_IMPACT_AFFECT";
    public static final String DV_TRUNG_KE_CR3_STATE = "DV_TRUNG_KE_CR3_STATE";
    public static final String DV_TRUNG_KE_CR3_CHANGE_ORGINATOR = "DV_TRUNG_KE_CR3_CHANGE_ORGINATOR";
    public static final String DV_TRUNG_KE_CR3_CHANGE_ORGINATOR_UNIT = "DV_TRUNG_KE_CR3_CHANGE_ORGINATOR_UNIT";
    public static final String DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV1 = "DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV1";
    public static final String DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV2 = "DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV2";
    public static final String DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV3 = "DV_TRUNG_KE_CR3_CHANGE_RESPONSIBLE_UNIT_KV3";

    public static final String AUTO_CR_SUBCATEGORY = "AUTO_CR_SUBCATEGORY";
    public static final String AUTO_CR_INPUT_CHECK = "AUTO_CR_INPUT_CHECK";
    public static final String AUTO_CR_EXECUTION = "AUTO_CR_EXECUTION";
    public static final String AUTO_CR_TITLE = "AUTO_CR_TITLE";
    public static final String AUTO_CR_DESCRIPTION = "AUTO_CR_DESCRIPTION";
    public static final String AUTO_CR_PRIORITY = "AUTO_CR_PRIORITY";
    public static final String AUTO_CR_IMPACT_AFFECT = "AUTO_CR_IMPACT_AFFECT";

    public static final String WO_DESCRIPTION = "WO_DESCRIPTION";
    public static final String WO_PRIORITY_ID = "WO_PRIORITY_ID";

    public static final String ACTIVE = "A";
    public static final String INACTIVE = "I";
    public static final String MOP_FILE_TXT = "MOP_FILE_TXT";
  }

  public final class SR_CONFIG {

    public static final String STATUS_CREATE_CR_DV_MKN = "STATUS_CREATE_CR_DV_MKN";
    public static final String VIPA_HISTORY_COMMENTS = "VIPA_HISTORY_COMMENTS";
    public static final String VIPA_HISTORY_COMMENTS_STATUS_DRAFT = "VIPA_HISTORY_COMMENTS_STATUS_DRAFT";
    public static final String STATUS_AUTO_PLANNED = "STATUS_AUTO_PLANNED";
    public static final String STATUS_AUTO_CONCLUDED = "STATUS_AUTO_CONCLUDED";
    public static final String STATUS_AUTO_EXECUTION_HALTED = "STATUS_AUTO_EXECUTION_HALTED";
    public static final String DICH_VU_NIMS = "DICH_VU_NIMS";
    public static final String DICH_VU_AOM = "DICH_VU_AOM";
    public static final String STATUS_RECEIVE_NIMS = "STATUS_RECEIVE_NIMS";
    public static final String STATUS_WAITING_NIMS = "STATUS_WAITING_NIMS";
    public static final String STATUS_RENEW = "STATUS_RENEW";
    public static final String WS_NIMS_URL = "WS_NIMS_URL";
    public static final String WS_NIMS_P = "WS_NIMS_PASSWORD";
    public static final String WS_NIMS_USERNAME = "WS_NIMS_USERNAME";

    public static final String CONFIG_GROUP_SMS_SRF = "SMS_SRF";
    public static final String CONFIG_GROUP_SMS = "SMS";

    public static final String CONFIG_CODE_SMS_RENEW = "SMS_RENEW";
    public static final String CONFIG_CODE_SMS_RENEW_APPROVE = "SMS_RENEW_APPROVE";
    public static final String CONFIG_CODE_SMS_RENEW_REJECT = "SMS_RENEW_REJECT";
    public static final String CONFIG_CODE_SMS_NIMS = "SMS_NIMS";

    public static final String WS_GATE_URL = "WS_GATE_URL";
    public static final String WS_GATE_USERNAME = "WS_GATE_USERNAME";
    public static final String WS_GATE_P = "WS_GATE_PASSWORD";

    public static final String WS_VMSA_URL = "WS_VMSA_URL";
    public static final String WS_VMSA_P = "WS_VMSA_PASSWORD";
    public static final String WS_VMSA_USERNAME = "WS_VMSA_USERNAME";

    public static final String WS_VIPA_URL = "WS_VIPA_URL";
    public static final String WS_VIPA_P = "WS_VIPA_PASSWORD";
    public static final String WS_VIPA_USERNAME = "WS_VIPA_USERNAME";
    public static final String CHANGE_RESPONSIBLE_UNIT_CR = "CHANGE_RESPONSIBLE_UNIT";

    public static final String WS_AAM_URL = "WS_AAM_URL";
    public static final String WS_AAM_P = "WS_AAM_PASSWORD";
    public static final String WS_AAM_USERNAME = "WS_AAM_USERNAME";

    public static final String MAP_COUNTRY = "MAP_COUNTRY";

    public static final String VMSA = "VMSA";
    public static final String AAM = "AAM";
    public static final String VIPA = "VIPA";

    public static final String AUTO_MATION = "AUTO_MATION";

    public static final String WS_CR_SR_P = "WS_CR_SR_PASSWORD";
    public static final String WS_CR_SR_USERNAME = "WS_CR_SR_USERNAME";

    public static final String WO_TYPE_CODE_TEST_SERVICE = "WO_TYPE_CODE_TEST_SERVICE";
    public static final String WO_TYPE_CODE_FT = "WO_TYPE_CODE_FT";
    public static final String CREATED_USER_SYSTEM = "CREATED_USER_SYSTEM";
    public static final String OTHER_SYS_SERVICE = "OTHER_SYS_SERVICE";

    public static final String VIPA_FILE_NAME = "VIPA_FILE_NAME";
    public static final String PATH_IMPORT_SR = "PATH_IMPORT_SR";
    public static final String FILE_TYPE_OTHER = "OTHER";

    public static final String FILE_GROUP_SR = "SR";
    public static final String FILE_GROUP_SC = "SC";
    public static final String FILE_GROUP_SR_AUTO = "SR_AUTO";

    public static final String STATUS = "STATUS";
    public static final String DICH_VU_NOC = "DICH_VU_NOC";
    public static final String DICH_VU_ADD_ON = "DICH_VU_ADD_ON";
    public static final String DICH_VU_WO_HELP = "DICH_VU_WO_HELP";
    public static final String DICH_VU_VSMART = "DICH_VU_VSMART";
    public static final String SERVICE_ARRAY = "SERVICE_ARRAY";
    public static final String SERVICE_GROUP = "SERVICE_GROUP";
    public static final String REVIEW_CLOSE_SR = "REVIEW_CLOSE_SR";
    public static final String STATUS_ATTACH_FILE_SERVICE = "STATUS_ATTACH_FILE_SERVICE";
    public static final String STATUS_NOT_EXECUTE = "STATUS_NOT_EXECUTE";

    public static final String BCCS_AUTO_CREATE_CR = "BCCS_AUTO_CREATE_CR";
    public static final String WS_BCCS_AUTO_CREATE_CR_URL = "WS_BCCS_URL";
    public static final String WS_BCCS_AUTO_CREATE_CR_PASS = "WS_BCCS_PASSWORD";
    public static final String WS_BCCS_AUTO_CREATE_CR_USERNAME = "WS_BCCS_USERNAME";
    public static final String WS_BCCS_AUTO_CREATE_CR_CODE = "SQL_GNOC_GET_MOP_BY_SR";

    public static final String SR_ALIAS = "GNOC_SR";

    public static final String AUTO_CREATE_SR_CHILD = "AUTO_CREATE_SR_CHILD";
    public static final String CFG_STATUS_SR_DELETE_SR_CATALOG = "CFG_STATUS_SR_DELETE_SR_CATALOG";
    public static final String CFG_TIME_OUT_SR_OPEN_CONNECT = "CFG_TIME_OUT_SR_OPEN_CONNECT";
  }

  public final class SR_STATUS {

    public static final String STATUS = "STATUS";
    public static final String DRAFT = "Draft";
    public static final String CLOSED = "Closed";
    public static final String NEW = "New";
    public static final String REJECTED = "Rejected";
    public static final String CANCELLED = "Cancelled";
    public static final String ASSIGNED_EVALUATION = "Assigned_Evaluation";
    public static final String UNDER_EVALUATION = "Under_Evaluation";
    public static final String EVALUATED = "Evaluated";
    public static final String UNDER_APPROVAL = "Under_Approval";
    public static final String APPROVED = "Approved";
    public static final String ASSIGNED_PLANNING = "Assigned_Planning";
    public static final String PLANNED = "Planned";
    public static final String READY_EXECUTION = "Ready_Execution";
    public static final String UNDER_EXECUTION = "Under_Execution";
    public static final String EXECUTED = "Executed";
    public static final String EXECUTION_HALTED = "Execution_Halted";
    public static final String CONCLUDED = "Concluded";
  }

  public final class SR_CONFIG2 {

    public static final String SR_CODE = "SR_CODE";
    public static final String WORKLOG_CONTENT = "WORKLOG_CONTENT";
  }

  public static class SR_ROLE_UPDATE {

    public final static String R = "R";
    public final static String U = "U";
    public final static String D = "D";
    public final static String WORKLOG = "WORKLOG";
    public final static String ADD_FILE = "ADD_FILE";
    public final static String DELETE_FILE = "DELETE_FILE";
    public final static String CR = "CR";
    public final static String WO = "WO";
    public final static String OD = "OD";

    public final static String FILE_OPEN_CONNECT = "161";
    public final static String FILE_TYPE_OPEN_CONNECT = "OPEN_CONNECT";
    public final static String FILE_TYPE_TOOL = "TOOL";
    public final static String FILE_TYPE_FILE_KY = "FILE_KY";
    public final static String VIPA_WS_USER = "VIPA_WS_USER";
    public final static String VIPA_WS_PASS = "VIPA_WS_PASS";
    public final static String VIPA_WS_LINK = "VIPA_WS_LINK";
    public final static String FILE_TYPE_FORM = "FORM";
    public final static String FILE_TYPE_OTHER = "OTHER";
    public final static String FILE_TYPE = "FILE_TYPE";
    public final static String NIMS = "NIMS";
    public final static String AOM = "AOM";
    public final static String FILE_TYPE_TRINH_KY = "AOM_TK";
  }

  public final class RESULT_SR_VIPA {

    public static final String OTHER = "OTHER";
    public static final String DICH_VU_MO_KET_NOI = "DICH_VU_MO_KET_NOI";
  }

  public final class SR_ROLE {

    public static final String ORIGINATOR = "Originator";
    public static final String IMPLEMENTOR = "Implementor";
    public static final String MANAGER = "Manager";
  }

  public final class SR_SERVICE_OTHER_SYS {

    public static final String DICH_VU_SIP_TRUNK_KHAI_BAO = "DICH_VU_SIP_TRUNK_KHAI_BAO";
    public static final String DICH_VU_SIP_TRUNK_HUY = "DICH_VU_SIP_TRUNK_HUY";
    public static final String DICH_VU_SIP_TRUNK_THAY_DOI = "DICH_VU_SIP_TRUNK_THAY_DOI";
    public static final String DICH_VU_SIP_TRUNK_CHAN = "DICH_VU_SIP_TRUNK_CHAN";
  }

  public final static class RISK_STATUS {

    public static final Long REQUEST_TO_OPEN = 1L;
    public static final String REQUEST_TO_OPEN_NAME = "REQUEST_TO_OPEN";
    public static final String ACCEPT = "ACCEPT";
    public static final String CLOSE = "CLOSE";
    public static final String REJECT = "REJECT";
    public static final String RECEIVE = "RECEIVE";
    public static final String OPEN = "OPEN";
    public static final String REQUEST_TO_CLOSE = "REQUEST_TO_CLOSE";
    public static final String NEW = "NEW";
    public static final String CANCEL = "CANCEL";

  }

  public interface USER_ROLE {

    String TP = "TP";
    String CVQLRR = "CVQLRR";
    String BGDTT = "BGDTT";
    String CVQLRRTT = "CVQLRRTT";
  }

  public interface RISK_ROLE {

    public static final Long NVT = 1L;
    public static final Long NVXL = 2L;
    public static final Long LDPQLRR = 3L;
    public static final Long LDPXLRR = 4L;
    public static final Long NVQLRR = 5L;
    public static final Long BGDTT = 6L;
    public static final Long CVQLRRTT = 7L;
    public static final Long LD_CREATE_UNIT = 8L;

  }

  public final class MR_ITEM_NAME {
//        public final static String MR_TECH = "32";
//        public final static String MR_TYPE = "33";
//        public final static String MR_SUBCATEGORY = "34";
//        public final static String MR_LIST_WORKS = "MR_LIST_WORKS";
//        public final static String MR_IMPACT = "35";
//        public final static String MR_PRIORITY = "36";
//        public final static String MR_INTERVAL = "37";
//        public final static String MR_ACCEPT = "MR_ACCEPT";
//        public final static String MR_WORKS = "38";
//        public final static String MR_AFFECTED_SERVICES = "39";

    public final static String MR_TECH = "MR_TECH";
    public final static String MR_TYPE = "MR_TYPE";
    public final static String MR_SUBCATEGORY = "MR_SUBCATEGORY";
    public final static String MR_LIST_WORKS = "MR_LIST_WORKS";
    public final static String MR_IMPACT = "MR_IMPACT";
    public final static String MR_PRIORITY = "MR_PRIORITY";
    public final static String MR_INTERVAL = "MR_INTERVAL";
    public final static String MR_ACCEPT = "MR_ACCEPT";
    public final static String MR_WORKS = "MR_WORKS";
    public final static String MR_AFFECTED_SERVICES = "MR_AFFECTED_SERVICES";
    //QuangDX_start
    public final static String MR_ARRAY = "MR_ARRAY";
    public final static String MR_CYCLE = "MR_CYCLE";
    //QuangDX_end
    public final static String MR_BGW = "BGW";
    public final static String BTS_REASON_WO_NOT_COMPLETE = "BTS_REASON_WO_NOT_COMPLETE";
  }

  //MR BTS
  public static class DEVICE_TYPE_MAP_MULTI_LANG {

    public final static String REFRESHER_CD = "DH";
    public final static String GENERATOR_CD = "MPD";

    private static Map<String, String> getDeviceName = new HashMap<>() {
      {
        put(REFRESHER_CD, "device.type.refresher");
        put(GENERATOR_CD, "device.type.generator");
      }
    };

    public static Map<String, String> getDeviceName() {
      return getDeviceName;
    }

    public static void setDeviceName(Map<String, String> getDeviceName) {
      DEVICE_TYPE_MAP_MULTI_LANG.getDeviceName = getDeviceName;
    }
  }

  public static class FUEL_TYPE_MAP_MULTI_LANG {

    public final static String OIL_CD = "D";
    public final static String GAS_CD = "X";

    private static Map<String, String> getMaterialTypeName = new HashMap<>() {
      {
        put(OIL_CD, "fuel.type.oil");
        put(GAS_CD, "fuel.type.gas");
      }
    };

    public static Map<String, String> getMaterialTypeName() {
      return getMaterialTypeName;
    }

  }

  public final class MR_CHECKLIST_BTS_BUSSINESS_CODE {

    public static final String CONTENT = "1";
    public static final String CAPTURE_GUIDE = "2";
  }

  public final class MR_UCTT {

    public static final String REFRESHER_CD = "DH";
    public static final String GENERATOR_CD = "MPD";

    public static final String OIL_CD = "D";
    public static final String GAS_CD = "X";
    public static final String R410a = "R410a";
    public static final String R410A = "R410A";
    public static final String O = "0";
    public static final String R32 = "R32";

    public static final String PHOTO_REQ_YES = "1";
    public static final String PHOTO_REQ_NO = "0";

    public static final String MR_CHECKLIST_BTS_CYCLE = "MR_CHECKLIST_BTS_CYCLE";
    public final static String MR_UCTT = "UCTT";
    public final static String WO_SYSTEM = "WO_SYSTEM";
    public final static String WO_STATUS = "WO_STATUS";
    public final static String WO_PRIORITY = "WO_PRIORITY";
    public final static String WO_TYPE_ID = "WO_TYPE_ID";
    public final static String WO_CREATE_USER = "WO_CREATE_USER";
  }

  public final class MR_CONFIG_GROUP {

    public static final String LY_DO_KO_BD = "LY_DO_KO_BD";
    public static final String MR_BD = "MR_BD";
  }

  public static final String OIL_CD = "D";
  public static final String GAS_CD = "X";
  public static final ImmutableList<String> CYCLE_LST = ImmutableList.of("1", "3", "6", "12", "24");
  public static final ImmutableList<String> DEVICE_TYPE_LST = ImmutableList
      .of(DEVICE_TYPE_MAP_MULTI_LANG.REFRESHER_CD, DEVICE_TYPE_MAP_MULTI_LANG.GENERATOR_CD);
  public static final ImmutableList<String> FUEL_TYPE_LST = ImmutableList.of(OIL_CD, GAS_CD);
  public static final ImmutableMap<String, String> FUEL_TYPE_MAP_MULTI_LANG = ImmutableMap.<String, String>builder()
      .put(OIL_CD, "fuel.type.oil")
      .put(GAS_CD, "fuel.type.gas")
      .build();

  public static class MARKET_CODE {

    public static final String HAITI = "2000289729";
  }

  public final class MR_MANAGEMENT_SQL {

    public static final String MR_MANAGEMENT = "MR_MANAGEMENT";
    public static final String SQL_BDM = "SQL_BDM";
    public static final String SQL_BDC = "SQL_BDC";
  }

  public interface MR_STATE_NAME {

    //        public final static String OPEN = "OPEN";
//        public final static String INACTIVE_WAITTING = "INACTIVE_WAITTING";
//        public final static String QUEUE = "QUEUE";
//        public final static String ACTIVE = "ACTIVE";
//        public final static String INACTIVE = "INACTIVE";
//        public final static String CLOSE = "CLOSE";
//        public final static String REJECT = "REJECT";
//        public final static String INCOMPLETE = "INCOMPLETE";
    public final static String OPEN = I18n.getLanguage("mrMngt.state.open");
    public final static String INACTIVE_WAITTING = I18n
        .getLanguage("mrMngt.state.inactive_waitting");
    public final static String ACTIVE = I18n.getLanguage("mrMngt.state.active");
    public final static String INACTIVE = I18n.getLanguage("mrMngt.stateCbb.inactive");
    public final static String CLOSE = I18n.getLanguage("mrMngt.stateCbb.close");
    public final static String QUEUE = I18n.getLanguage("mrMngt.stateCbb.queue");
//        public final static String QUEUE = BundleUtils.getString("mrMngt.state.queue");

    //changed by namtn
//        public final static String REJECT = "REJECT";
//        public final static String INCOMPLETE = "INCOMPLETE";
  }

  public final class MR_HIS_ACTION_CODE {

    public final static String CREATE = "1";
    public final static String APPROVE = "2";
    public final static String EDIT = "3";
    public final static String EXECUTE = "4";
    public final static String CLOSE = "5";
  }

  public interface REST_FUNC {

    String CC_GET_CAUSE_EXPIRE = "CC_GET_CAUSE_EXPIRE";
    String IM_TRU_KHO = "IM_TRU_KHO";
    String IM_ROLLBACK = "IM_ROLLBACK";
    String IM_CHECK_KHO = "IM_CHECK_KHO";
    String MVT = "MVT";
    String VTB = "VTB";
    String VTZ = "VTZ";
    String MYT = "MYT";
    String VTC = "VTC";
    String VNM = "VNM";
    String VTL = "VTL";

    String AMI_ONE_CHECK_ONLINE = "AMI_ONE_CHECK_ONLINE";
  }

  public final class WSIPCC {

    public final static String P_DEFAULT = "Ipcc@2016#";
  }

  public static class CFG_ROLE_DATA_SYSTEM {

    public static final Long COMMON = 0L;
    public static final Long CR = 1L;
    public static final Long MR = 2L;
    public static final Long PT = 3L;
    public static final Long TT = 4L;
    public static final Long WO = 5L;
    public static final Long SR = 6L;
  }

  public static class CR_ROLE_ACTION {

    public static final Long ADMIN = 0L;
    public static final Long FO = 1L;
    public static final Long Audit = 2L;
  }

  public static final String CR_LINK_TOOL = "CR_LINK_TOOL";
}
