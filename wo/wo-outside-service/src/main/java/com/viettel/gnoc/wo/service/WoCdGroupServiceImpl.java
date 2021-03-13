package com.viettel.gnoc.wo.service;

import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.business.WoCdGroupBusiness;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WoCdGroupServiceImpl implements WoCdGroupService {

  @Autowired
  protected WoCdGroupBusiness woCdGroupBusiness;

  @Override
  public List<WoCdGroupDTO> getListWoCdGroupDTOByWoTypeAndGroupType(Long woTypeId, Long groupTypeId,
      String locale) {
    List<WoCdGroupInsideDTO> lstInside = woCdGroupBusiness
        .getListWoCdGroupDTOByWoTypeAndGroupType(woTypeId, groupTypeId, locale);
    List<WoCdGroupDTO> lstOutSide = new ArrayList<>();
    for (WoCdGroupInsideDTO dto : lstInside) {
      lstOutSide.add(dto.toDtoOutSide());
    }
    return lstOutSide;
  }

  @Override
  public WoCdGroupDTO getCdByStationCode(String stationCode, String woTypeId, String cdGroupType,
      String businessName) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness
        .getCdByStationCode(stationCode, woTypeId, cdGroupType, businessName);
    return woCdGroupInsideDTO.toDtoOutSide();
  }

  @Override
  public WoCdGroupDTO getCdByStationCodeNation(String stationCode, String woTypeId,
      String cdGroupType, String nationCode, String businessName) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness
        .getCdByStationCodeNation(stationCode, woTypeId, cdGroupType, businessName, nationCode);
    if (woCdGroupInsideDTO != null) {
      WoCdGroupDTO woCdGroupDTO = new WoCdGroupDTO();
      woCdGroupDTO = woCdGroupInsideDTO.toDtoOutSide();
      woCdGroupDTO.setDefaultSortField("name");
      return woCdGroupDTO;
    }
    return null;
  }

  @Override
  public WoCdGroupDTO getWoCdGroupWoByCdGroupCode(String woGroupCode) {
    WoCdGroupDTO woCdGroupOut = null;
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness
        .getWoCdGroupWoByCdGroupCode(woGroupCode);
    if (woCdGroupInsideDTO != null) {
      woCdGroupOut = woCdGroupInsideDTO.toDtoOutSide();
    }
    return woCdGroupOut;
  }

  @Override
  public List<UsersDTO> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow) {
    List<UsersDTO> lstOutSide = new ArrayList<>();
    if (rowStart < 0) {
      return lstOutSide;
    }
    if (maxRow <= 0) {
      maxRow = Integer.MAX_VALUE;
    }
    List<UsersInsideDto> lstInside = woCdGroupBusiness
        .getListFtByUser(userId, keyword, rowStart, maxRow);
    for (UsersInsideDto dto : lstInside) {
      lstOutSide.add(dto.toOutSideDto());
    }
    return lstOutSide;
  }

  @Override
  public WoCdGroupDTO findWoCdGroupById(Long id) {
    WoCdGroupDTO woCdGroupDTO = null;
    if (id != null && id > 0) {
      WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupBusiness.findWoCdGroupById(id);
      woCdGroupDTO = woCdGroupInsideDTO.toDtoOutSide();
      woCdGroupDTO.setDefaultSortField("name");
    }
    return woCdGroupDTO;
  }

  @Override
  public List<WoCdGroupDTO> getListCdGroupByUser(Long woTypeId, Long groupTypeId, Long userId,
      String locale) {
    WoCdGroupTypeUserDTO woCdGroupTypeUserDTO = new WoCdGroupTypeUserDTO();
    woCdGroupTypeUserDTO.setWoTypeId(woTypeId);
    woCdGroupTypeUserDTO.setGroupTypeId(groupTypeId);
    woCdGroupTypeUserDTO.setUserId(userId);
    woCdGroupTypeUserDTO.setLocale(locale);
    List<WoCdGroupInsideDTO> lstInside = woCdGroupBusiness
        .getListCdGroupByUser(woCdGroupTypeUserDTO);
    List<WoCdGroupDTO> lstOutSide = new ArrayList<>();
    for (WoCdGroupInsideDTO dto : lstInside) {
      lstOutSide.add(dto.toDtoOutSide());
    }
    return lstOutSide;
  }

  @Override
  public List<WoCdGroupDTO> getListWoCdGroupDTO(WoCdGroupDTO woCdGroupDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupDTO.toDtoInside();
    List<WoCdGroupDTO> lstOutSide = new ArrayList<>();
    List<WoCdGroupInsideDTO> lstInside = woCdGroupBusiness
        .getListWoCdGroupDTO(woCdGroupInsideDTO, rowStart, maxRow, sortType, sortFieldList);
    for (WoCdGroupInsideDTO dto : lstInside) {
      WoCdGroupDTO dtoOut = new WoCdGroupDTO();
      dtoOut = dto.toDtoOutSide();
      dtoOut.setDefaultSortField("name");
      lstOutSide.add(dtoOut);
    }
    return lstOutSide;
  }

  @Override
  public List<WoCdGroupDTO> getListCdGroupByUserDTO(WoCdGroupDTO woCdGroupDTO, Long woTypeId,
      Long groupTypeId, Long userId, String locale) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupDTO.toDtoInside();
    List<WoCdGroupInsideDTO> lstInside = woCdGroupBusiness
        .getListCdGroupByUserDTO(woCdGroupInsideDTO, woTypeId, groupTypeId, userId, locale);

    List<WoCdGroupDTO> lstOutSide = new ArrayList<>();
    for (WoCdGroupInsideDTO dto : lstInside) {
      lstOutSide.add(dto.toDtoOutSide());
    }
    return lstOutSide;
  }

  @Override
  public List<WoCdGroupDTO> getListWoCdGroupActive(WoCdGroupDTO woCdGroupDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    WoCdGroupInsideDTO woCdGroupInsideDTO = woCdGroupDTO.toDtoInside();
    List<WoCdGroupInsideDTO> lstInside = woCdGroupBusiness
        .getListWoCdGroupActive(woCdGroupInsideDTO, rowStart, maxRow, sortType, sortFieldList);

    List<WoCdGroupDTO> lstOutSide = new ArrayList<>();
    for (WoCdGroupInsideDTO dto : lstInside) {
      WoCdGroupDTO dtoOutSide = dto.toDtoOutSide();
      dtoOutSide.setDefaultSortField("name");
      lstOutSide.add(dtoOutSide);
    }
    return lstOutSide;
  }
}
