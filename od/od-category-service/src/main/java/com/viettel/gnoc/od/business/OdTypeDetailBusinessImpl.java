package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.repository.OdTypeDetailRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NamTN
 */
@Service
@Transactional
@Slf4j
public class OdTypeDetailBusinessImpl implements OdTypeDetailBusiness {

  @Autowired
  protected OdTypeDetailRepository odTypeDetailRepository;

  @Override
  public Datatable getListOdTypeDetailPage(OdTypeDetailDTO odTypeDetailDTO) {
    log.debug("Request to getListOdTypeDetailPage : {}", odTypeDetailDTO);
    return odTypeDetailRepository.getListOdTypeDetailPage(odTypeDetailDTO);
  }

  @Override
  public Datatable getListOdTypeDetail(OdTypeDetailDTO odTypeDetailDTO) {
    log.debug("Request to getListOdTypeDetail : {}", odTypeDetailDTO);
    return odTypeDetailRepository.getListOdTypeDetail(odTypeDetailDTO);
  }

  @Override
  public OdTypeDetailDTO checkOdTypeDetailExist(Long odTypeId, Long priorityId) {
    log.debug("Request to checkOdTypeDetailExist : {}", odTypeId);
    return odTypeDetailRepository.checkOdTypeDetailExist(odTypeId, priorityId);
  }

  @Override
  public List<OdTypeDetailDTO> getListOdTypeDetailByOdTypeId(Long odTypeId) {
    log.debug("Request to getListOdTypeDetail : {}", odTypeId);
    return odTypeDetailRepository.getListOdTypeDetailByOdTypeId(odTypeId);
  }

  @Override
  public ResultInSideDto insertOrUpdateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList) {
    log.debug("Request to insertOrUpdateListOdTypeDetail : {}", detailDTOList);
    return odTypeDetailRepository.insertOrUpdateListOdTypeDetail(detailDTOList);
  }

  @Override
  public ResultInSideDto updateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList) {
    log.debug("Request to updateListOdTypeDetail : {}", detailDTOList);
    return odTypeDetailRepository.updateListOdTypeDetail(detailDTOList);
  }

  @Override
  public ResultInSideDto deleteOdTypeDetail(List<OdTypeDetailDTO> lstOdTypeDetailDTO,
      Long odTypeId) {
    log.debug("Request to deleteOdTypeDetail : {}", odTypeId);
    return odTypeDetailRepository.deleteOdTypeDetail(lstOdTypeDetailDTO, odTypeId);
  }
}
