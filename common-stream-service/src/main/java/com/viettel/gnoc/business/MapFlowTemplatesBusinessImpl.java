package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.repository.MapFlowTemplatesRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MapFlowTemplatesBusinessImpl implements MapFlowTemplatesBusiness {

  @Autowired
  MapFlowTemplatesRepository mapFlowTemplatesRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto updateMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    mapFlowTemplatesDTO.setLastUpdateTime(dateFormat.format(date));
    mapFlowTemplatesDTO
        .setUserID((userToken == null) ? null : String.valueOf(userToken.getUserID()));
    return mapFlowTemplatesRepository.updateMapFlowTemplates(mapFlowTemplatesDTO);
  }

  @Override
  public ResultInSideDto deleteMapFlowTemplates(Long id) {
    return mapFlowTemplatesRepository.deleteMapFlowTemplates(id);
  }

  @Override
  public ResultInSideDto deleteListMapFlowTemplates(
      List<MapFlowTemplatesDTO> mapFlowTemplatesListDTO) {
    return mapFlowTemplatesRepository.deleteListMapFlowTemplates(mapFlowTemplatesListDTO);
  }

  @Override
  public MapFlowTemplatesDTO findMapFlowTemplatesById(Long id) {
    return mapFlowTemplatesRepository.findMapFlowTemplatesById(id);
  }

  @Override
  public ResultInSideDto insertMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
    mapFlowTemplatesDTO.setLastUpdateTime(dateFormat.format(date));
    mapFlowTemplatesDTO
        .setUserID((userToken == null) ? null : String.valueOf(userToken.getUserID()));
    return mapFlowTemplatesRepository.insertMapFlowTemplates(mapFlowTemplatesDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListMapFlowTemplates(
      List<MapFlowTemplatesDTO> mapFlowTemplatesDTO) {
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    UserToken userToken = ticketProvider.getUserToken();
    Date date = new Date();
    for (MapFlowTemplatesDTO item : mapFlowTemplatesDTO) {
      item.setLastUpdateTime(dateFormat.format(date));
      item.setUserID((userToken == null) ? null : String.valueOf(userToken.getUserID()));
    }
    return mapFlowTemplatesRepository.insertOrUpdateListMapFlowTemplates(mapFlowTemplatesDTO);
  }

  @Override
  public List<String> getSequenseMapFlowTemplates(String seqName, int... size) {
    return mapFlowTemplatesRepository.getSequenseMapFlowTemplates(seqName, size);
  }

  @Override
  public List<MapFlowTemplatesDTO> getListMapFlowTemplatesByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return null;
  }

  @Override
  public Datatable getListMapFlowTemplatesDTO(MapFlowTemplatesDTO mapFlowTemplatesDTO) {
    return mapFlowTemplatesRepository.getListMapFlowTemplatesDTO(mapFlowTemplatesDTO);
  }

  @Override
  public List<MapFlowTemplatesDTO> getListMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return mapFlowTemplatesRepository
        .getListMapFlowTemplates(mapFlowTemplatesDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}

