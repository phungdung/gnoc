package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import java.util.List;

public interface CrMobileBusiness {

  List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO);

  ObjResponse getListCRBySearchTypePaggingMobile(CrDTO crDTO, int start, int maxResult,
      String locale);

  List<CrDTO> getListPreApprovedCrOutSide(CrDTO crDTO);

  void actionApproveCRAfter(CrDTO crDTO);

  String actionVerifyMobile(CrDTO crDTO, String locale);

  String actionAppraiseCRAfter(CrDTO crDTO);

  String actionReceiveCr(CrDTO crDTO);

  String actionScheduleCr(CrDTO crDTO);

  String actionResolveCr(CrDTO crDTO);

  String actionCloseCr(CrDTO crDTO);

  String actionCab(CrInsiteDTO crDTO);

  String actionEditCr(CrDTO crDTO);

  List<CrDTO> getCrByIdAndResolveStatuṣ̣(List<Long> crIds, Long resolveStatus);

  CrFilesAttachDTO getFileByPath(String path);

  String actionAssignCabMobile(CrDTO crDTO, String locale);

  CrDTO getCrByIdExtends(String crId);
}
