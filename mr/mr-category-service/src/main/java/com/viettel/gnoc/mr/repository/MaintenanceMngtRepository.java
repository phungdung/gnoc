package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceMngtRepository {


  List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String valueOf);

  MrInsideDTO findById(Long id);

  List<CrHisDTO> findCrHisByListCrId(List<String> crId);
}
