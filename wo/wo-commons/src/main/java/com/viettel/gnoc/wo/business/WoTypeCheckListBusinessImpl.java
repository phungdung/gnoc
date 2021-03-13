package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.repository.WoChecklistDetailRepository;
import com.viettel.gnoc.wo.repository.WoTypeCheckListRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoTypeCheckListBusinessImpl implements WoTypeCheckListBusiness {

  @Autowired
  protected WoTypeCheckListRepository woTypeCheckListRepository;

  @Autowired
  protected WoChecklistDetailRepository woChecklistDetailRepository;

  @Override
  public ResultInSideDto delete(Long woTypeChecklistId) {
    log.debug("Request to delete : {}", woTypeChecklistId);
    return woTypeCheckListRepository.delete(woTypeChecklistId);
  }

  @Override
  public List<WoTypeCheckListDTO> findAllByWoTypeID(Long woTypeId) {
    log.debug("Request to findAllByWoTypeID : {}", woTypeId);
    return woTypeCheckListRepository.findAllByWoTypeID(woTypeId);
  }


  @Override
  public ResultInSideDto add(WoTypeCheckListDTO woTypeCheckListDTO) {
    log.debug("Request to add : {}", woTypeCheckListDTO);
    return woTypeCheckListRepository.add(woTypeCheckListDTO);
  }

  @Override
  public Datatable getListWoTypeChecklistDTO(WoTypeCheckListDTO woTypeCheckListDTO) {
    log.debug("Request to getListWoTypeChecklistDTO: {}", woTypeCheckListDTO);
    return woTypeCheckListRepository.getListWoTypeChecklistDTO(woTypeCheckListDTO);
  }

  @Override
  public ResultInSideDto updateWoChecklistDetail(
      List<WoChecklistDetailDTO> listWoChecklistDetailDTO) {
    log.debug("Request to updateWoChecklistDetail: {}", listWoChecklistDetailDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoChecklistDetailDTO checkDTO = new WoChecklistDetailDTO();
    checkDTO.setWoId(listWoChecklistDetailDTO.get(0).getWoId());
    List<WoChecklistDetailDTO> listChecklist = woChecklistDetailRepository
        .getListWoChecklistDetailDTO(checkDTO);
    if (listChecklist != null && !listChecklist.isEmpty()) {
      resultInSideDto = deleteListWoChecklistDetailDTO(listChecklist);
    }
    for (WoChecklistDetailDTO woChecklistDetailDTO : listWoChecklistDetailDTO) {
      if (StringUtils.isNotNullOrEmpty(woChecklistDetailDTO.getChecklistValue())) {
        resultInSideDto = woChecklistDetailRepository
            .insertWoChecklistDetailDTO(woChecklistDetailDTO);
      }
    }
    return resultInSideDto;
  }

  @Override
  public List<WoChecklistDetailDTO> getListWoChecklistDetailDTO(
      WoChecklistDetailDTO woChecklistDetailDTO) {
    log.debug("Request to getListWoChecklistDetailDTO: {}", woChecklistDetailDTO);
    return woChecklistDetailRepository.getListWoChecklistDetailDTO(woChecklistDetailDTO);
  }

  private ResultInSideDto deleteListWoChecklistDetailDTO(List<WoChecklistDetailDTO> listChecklist) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (WoChecklistDetailDTO dto : listChecklist) {
      resultInSideDto = woChecklistDetailRepository
          .deleteWoChecklistDetailDTO(dto.getWoChecklistDetailId());
    }
    return resultInSideDto;
  }
}
