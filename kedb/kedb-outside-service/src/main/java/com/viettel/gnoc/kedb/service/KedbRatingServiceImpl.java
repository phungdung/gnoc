package com.viettel.gnoc.kedb.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.kedb.business.KedbRatingBusiness;
import com.viettel.gnoc.kedb.dto.KedbRatingDTO;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ITSOL
 */


@Service
@Slf4j
public class KedbRatingServiceImpl implements KedbRatingService {

  @Autowired
  protected KedbRatingBusiness kedbRatingBusiness;

  @Override
  public KedbRatingDTO getKedbRating(KedbRatingDTO kedbRatingDTO) {
    log.debug("Request to getKedbRating : {}", kedbRatingDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingDTO.toModelInSide();
    kedbRatingInsideDTO = kedbRatingBusiness.getKedbRating(kedbRatingInsideDTO);
    return kedbRatingInsideDTO.toModelOutsideSide();
  }

  @Override
  public KedbRatingDTO insertOrUpdateKedbRating(KedbRatingDTO kedbRatingDTO) {
    log.debug("Request to insertOrUpdateKedbRating : {}", kedbRatingDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingDTO.toModelInSide();
    kedbRatingInsideDTO = kedbRatingBusiness.insertOrUpdateKedbRating(kedbRatingInsideDTO);
    return kedbRatingInsideDTO.toModelOutsideSide();
  }

  @Override
  public String updateKedbRating(KedbRatingDTO kedbRatingDTO) {
    log.debug("Request to updateKedbRating : {}", kedbRatingDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingDTO.toModelInSide();
    ResultInSideDto resultInSideDto = kedbRatingBusiness.updateKedbRating(kedbRatingInsideDTO);
    return resultInSideDto.getKey();
  }

  @Override
  public String deleteKedbRating(Long id) {
    log.debug("Request to deleteKedbRating : {}", id);
    ResultInSideDto resultInSideDto = kedbRatingBusiness.deleteKedbRating(id);
    return resultInSideDto.getKey();
  }

  @Override
  public String deleteListKedbRating(List<KedbRatingDTO> kedbRatingListDTO) {
    log.debug("Request to deleteListKedbRating : {}", kedbRatingListDTO);
    List<Long> list = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (kedbRatingListDTO != null && kedbRatingListDTO.size() > 0) {
      for (KedbRatingDTO kedbRatingDTO : kedbRatingListDTO) {
        list.add(Long.valueOf(kedbRatingDTO.getKedbId()));
      }
      resultInSideDto = kedbRatingBusiness.deleteListKedbRating(list);
    } else {
      resultInSideDto.setKey(RESULT.NODATA);
    }

    return resultInSideDto.getKey();
  }

  @Override
  public KedbRatingDTO findKedbRatingById(Long id) {
    log.debug("Request to findKedbRatingById : {}", id);
    if (id != null && id > 0) {
      KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingBusiness.findKedbRatingById(id);
      return kedbRatingInsideDTO.toModelOutsideSide();
    }
    return null;
  }

  @Override
  public List<KedbRatingDTO> getListKedbRatingDTO(KedbRatingDTO kedbRatingDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListKedbRatingDTO : {}", kedbRatingDTO);
    List<KedbRatingDTO> dtoList = new ArrayList<>();
    KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingDTO.toModelInSide();
    if (kedbRatingInsideDTO != null) {
      kedbRatingInsideDTO.setPageSize(maxRow);
      kedbRatingInsideDTO.setPage(rowStart);
      kedbRatingInsideDTO.setSortType(sortType);
      kedbRatingInsideDTO.setSortName(sortFieldList);
      List<KedbRatingInsideDTO> list = kedbRatingBusiness.getListKedbRatingDTO(kedbRatingInsideDTO);
      if (list != null && list.size() > 0) {
        for (KedbRatingInsideDTO dto : list) {
          dtoList.add(dto.toModelOutsideSide());
        }
      }
    }
    return dtoList;
  }

  @Override
  public ResultDTO insertKedbRating(KedbRatingDTO kedbRatingDTO) {
    log.debug("Request to insertKedbRating : {}", kedbRatingDTO);
    KedbRatingInsideDTO kedbRatingInsideDTO = kedbRatingDTO.toModelInSide();
    return kedbRatingBusiness.insertKedbRating(kedbRatingInsideDTO).toResultDTO();
  }

  @Override
  public String insertOrUpdateListKedbRating(List<KedbRatingDTO> kedbRatingDTO) {
    log.debug("Request to insertOrUpdateListKedbRating : {}", kedbRatingDTO);
    List<KedbRatingInsideDTO> list = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (kedbRatingDTO != null && kedbRatingDTO.size() > 0) {
      for (KedbRatingDTO dto : kedbRatingDTO) {
        list.add(dto.toModelInSide());
      }
      resultInSideDto = kedbRatingBusiness.insertOrUpdateListKedbRating(
          list);
    } else {
      resultInSideDto.setKey(RESULT.NODATA);
    }

    return resultInSideDto.getKey();
  }

  @Override
  public List<String> getSequenseKedbRating(String seqName, int... size) {
    log.debug("Request to getSequenseKedbRating : {}", seqName);
    int number = (size[0] > 0 ? size[0] : 1);
    return kedbRatingBusiness.getSequenseKedbRating(number);
  }

  @Override
  public List<KedbRatingDTO> getListKedbRatingByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListKedbRatingByCondition : {}", lstCondition);
    List<KedbRatingDTO> listDto = new ArrayList<>();
    List<KedbRatingInsideDTO> list = kedbRatingBusiness
        .getListKedbRatingByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
    if (list != null && list.size() > 0) {
      for (KedbRatingInsideDTO dto : list) {
        listDto.add(dto.toModelOutsideSide());
      }
    }
    return listDto;
  }
}
