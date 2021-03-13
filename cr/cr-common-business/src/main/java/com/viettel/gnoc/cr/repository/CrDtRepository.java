package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrDtRepository {

  ResultDTO insertVMSADT(Long crId, Long validateKey, String systemCode, int createMopSuccess,
      String createMopDetail,
      List<VMSAMopDetailDTO> mopDTOList, String nationCode, String locale);

  // anhlp add
  List<ItemDataCR> getAllActiveAffectedService(String locale) throws Exception;

  // anhlp add
  ResultDTO insertVMSADT(String userService, String passService, String systemCode, Long crId,
      Long validateKey, int createMopSuccess, String createMopDetail,
      List<VMSAMopDetailDTO> mopDTOList, String nationCode,
      String locale);
}
