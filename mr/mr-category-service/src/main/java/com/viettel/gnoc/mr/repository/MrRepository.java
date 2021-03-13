package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.sr.dto.InsertFileDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrRepository {

  MrInsideDTO findMrById(Long mrId);

  ResultInSideDto deleteMr(Long mrId);

  ResultInSideDto insertListMr(List<MrInsideDTO> mrDTO);

  List<MrDTO> checkExistCrId(String crId);

  List<MrDTO> checkExistWoId(String woId);

  ResultDTO updateMrStatus(MrDTO dtoUpdate);

  boolean checkCrIdInMrNode(String crId);

  MrDTO getMrForClose(String crId);

  int getCountCrNotClose(String mrId);

  UsersDTO getUsersOfUnit(Long unitId) throws Exception;

  InsertFileDTO getCrFileTelInsert(MrScheduleTelDTO objSchedule) throws Exception;

  List<WoDTO> getListWoOfCr(String crId);

  List<CrDTO> getListCrOfMr(String mrId);

  InsertFileDTO getCrFileInsert(MrITSoftScheduleDTO objSchedule) throws Exception;

  boolean checkCrExists(String mrId, String crId);

  MrClientDetail getMrChartInfoForNOC(MrForNocSearchDTO mrSearchDTO);

  List<MrDTO> getListMrForMobile(MrMobileDTO dto);
}
