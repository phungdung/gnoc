package com.viettel.gnoc.cr.util;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;

public class CrGeneralUtil {

  public static Long generateStateForApproveCr(CrInsiteDTO crDTO, Long actionType) {
    Long status = Constants.CR_STATE.QUEUE;
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    String crTypeStr = crDTO.getCrType();
    if (crTypeStr != null) {
      if (actionType.equals(Constants.CR_ACTION_CODE.APPROVE)) {
        if (crTypeStr.equals(Constants.CR_TYPE.EMERGENCY.toString())) {
          status = Constants.CR_STATE.EVALUATE;
        } else if (crTypeStr.equals(Constants.CR_TYPE.STANDARD.toString())) {
          status = Constants.CR_STATE.APPROVE;
        }
      }
      if (actionType.equals(Constants.CR_ACTION_CODE.INCOMPLETE_APPROVE_STD)
          && crTypeStr.equals(Constants.CR_TYPE.STANDARD.toString())) {
        status = Constants.CR_STATE.INCOMPLETE;
      }
      if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE_APPROVE_STD)
          && crTypeStr.equals(Constants.CR_TYPE.STANDARD.toString())) {
        status = Constants.CR_STATE.RESOLVE;
      }
      if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR_APPROVE_STD)
          && crTypeStr.equals(Constants.CR_TYPE.STANDARD.toString())) {
        status = Constants.CR_STATE.CLOSE;
      }
    }
    return status;
  }

  public static Long generateStateForVerifyCR(CrInsiteDTO crDTO, Long actionType) {
    Long status = Constants.CR_STATE.QUEUE;
    String crType = crDTO.getCrType();
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ACCEPT_BY_MANAGER_PRE)
        && !"4".equals(crDTO.getRisk())
        && !"121".equals(crDTO.getImpactSegment())) {
      status = Constants.CR_STATE.WAIT_CAB;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_TO_CONSIDER)) {
      if (Constants.CR_TYPE.STANDARD.toString().equals(crType)) {
        status = Constants.CR_STATE.APPROVE;
      } else {
        status = Constants.CR_STATE.COORDINATE;
      }

    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CHANGE_TO_CAB)) {
      status = Constants.CR_STATE.WAIT_CAB;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CHANGE_TO_SCHEDULE)) {
      status = Constants.CR_STATE.EVALUATE;
    }
    return status;
  }

  /**
   * Sinh trang thai CR tham dinh
   */
  public static Long generateStateForAppraisalCR(CrInsiteDTO crDTO, Long actionType) {
    Long status = Constants.CR_STATE.WAIT_CAB;
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_APPRAISER)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_APPRAISER)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_APPRAISER)) {
      status = Constants.CR_STATE.QUEUE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.APPRAISE)) {
      if (!"4".equals(crDTO.getRisk()) && !"121".equals(crDTO.getImpactSegment())) {
        status = Constants.CR_STATE.WAIT_CAB;
      } else {
        status = Constants.CR_STATE.EVALUATE;
      }
    }
    return status;
  }

  /**
   * Sinh trang thai Cr kiem tra dau vao
   */
  public static Long generateStateForSchedule(CrInsiteDTO crDTO, Long actionType) {
    Long status = Constants.CR_STATE.EVALUATE;

        /*String crType = "";
         if (crDTO != null) {
         crType = crDTO.getCrType();
         }*/
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CAB_WHEN_SCHEDULE)) {
      status = Constants.CR_STATE.WAIT_CAB;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)) {

      if (String.valueOf(Constants.CR_TYPE.EMERGENCY).equals(crDTO.getCrType()) && "1"
          .equals(crDTO.getIsTracingCr())) {
        status = Constants.CR_STATE.CLOSE;
      } else {
        status = Constants.CR_STATE.APPROVE;
      }
    }
    return status;
  }

  /**
   * SSinh trang thai Cr kiem tra dau vao
   */
  public static Long generateStateForReceive(Long actionType) {
    Long status = Constants.CR_STATE.APPROVE;
    //String crType = crDTO.getCrType();
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL)) {
      status = Constants.CR_STATE.EVALUATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      status = Constants.CR_STATE.APPROVE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ACCEPT)) {
      status = Constants.CR_STATE.ACCEPT;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT_EXCUTE_STD)) {
      status = Constants.CR_STATE.APPROVE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE_WITH_FAILT_STATUS_DUE_TO_WO)) {
      status = Constants.CR_STATE.CLOSE;
    }
    return status;
  }

  public static Long generateStateForResolve(Long actionType) {
    Long status = Constants.CR_STATE.ACCEPT;
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE)) {
      status = Constants.CR_STATE.RESOLVE;
    }
    return status;
  }

  public static Long generateStateForCloseCR(Long actionType) {
    Long status = Constants.CR_STATE.RESOLVE;
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR)) {
      status = Constants.CR_STATE.CLOSE;
    }
    return status;
  }

  public static Long generateStateForCabCR(Long actionType) {
    Long status = Constants.CR_STATE.EVALUATE;
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_CAB)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CONSIDER_WHEN_CAB)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGE_WHEN_CAB)) {
      status = Constants.CR_STATE.WAIT_CAB;
    }
    return status;
  }

  public static Long generateStateForAssignCabCR(Long actionType) {
    Long status = Constants.CR_STATE.CAB;
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_TO_CAB)) {
      status = Constants.CR_STATE.CAB;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_CAB)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_CAB)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_CAB)) {
      status = Constants.CR_STATE.CLOSE;
    }
    return status;
  }

  public static Long generateStateForScheduleClient(Long actionType) {
    Long status = Constants.CR_STATE.EVALUATE;
//        String crType = crDTO.getCrType();
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISE_BY_MANAGER_SCH)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CAB_WHEN_SCHEDULE)) {
      status = Constants.CR_STATE.WAIT_CAB;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.SCHEDULE)) {
      status = Constants.CR_STATE.APPROVE;
    }
    return status;
  }

  public static Long generateStateForReceiveClient(Long actionType) {
    Long status = Constants.CR_STATE.APPROVE;
//        String crType = crDTO.getCrType();
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_MANAGER_BY_IMPL)) {
      status = Constants.CR_STATE.EVALUATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_APPRAISER_BY_IMPL)) {
      status = Constants.CR_STATE.COORDINATE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ASSIGN_EXC_TO_EMPLOYEE)) {
      status = Constants.CR_STATE.APPROVE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.ACCEPT)) {
      status = Constants.CR_STATE.ACCEPT;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.REJECT_EXCUTE_STD)) {
      status = Constants.CR_STATE.APPROVE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_STD)) {
      status = Constants.CR_STATE.CLOSE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.RETURN_TO_CREATOR_WHEN_EXCUTE_STD)) {
      status = Constants.CR_STATE.INCOMPLETE;
    }
    if (actionType.equals(Constants.CR_ACTION_CODE.CLOSE_EXCUTE_EMERGENCY)) {
      status = Constants.CR_STATE.CLOSE;
    }
    return status;
  }

  public static Long generateStateForResolveClient(Long actionType) {
    Long status = Constants.CR_STATE.ACCEPT;
    if (actionType.equals(Constants.CR_ACTION_CODE.RESOLVE)) {
      status = Constants.CR_STATE.RESOLVE;
    } else if (actionType.equals(Constants.CR_ACTION_CODE.CLOSECR)) {
      status = Constants.CR_STATE.CLOSE;
    }
    return status;
  }

}
