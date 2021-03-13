package com.viettel.gnoc.kedb.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import com.viettel.gnoc.kedb.model.KedbRatingEntity;
import com.viettel.gnoc.kedb.repository.KedbRatingRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class KedbRatingBusinessImpl implements KedbRatingBusiness {

  @Autowired
  KedbRatingRepository kedbRatingRepository;

  private final static String KEDB_RATING_SEQ = "KEDB_RATING_SEQ";

  @Override
  public List<KedbRatingInsideDTO> getListKedbRatingDTO(KedbRatingInsideDTO kedbRatingInsideDTO) {
    log.debug("Request to getListKedbRatingDTO: {}");
    List<KedbRatingInsideDTO> list = (List<KedbRatingInsideDTO>) kedbRatingRepository
        .getListKedbRatingDTO(kedbRatingInsideDTO).getData();
    return list;
  }

  @Override
  public KedbRatingInsideDTO findKedbRatingById(Long id) {
    log.debug("Request to findKedbRatingById: {}", id);
    return kedbRatingRepository.findKedbRatingById(id);
  }

  @Override
  public KedbRatingInsideDTO getKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    log.debug("Request to getKedbRating: {}", kedbRatingInsideDTO);
    return kedbRatingRepository.getKedbRating(kedbRatingInsideDTO);
  }

  @Override
  public ResultInSideDto insertKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    log.debug("Request to insertKedbRating: {}", kedbRatingInsideDTO);
    return kedbRatingRepository.insertKedbRating(kedbRatingInsideDTO);
  }

  @Override
  public ResultInSideDto updateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    log.debug("Request to updateKedbRating: {}", kedbRatingInsideDTO);
    return kedbRatingRepository.updateKedbRating(kedbRatingInsideDTO);
  }

  @Override
  public KedbRatingInsideDTO insertOrUpdateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO) {
    log.debug("Request to insertOrUpdateKedbRating: {}", kedbRatingInsideDTO);
    KedbRatingInsideDTO dto = new KedbRatingInsideDTO();
    try {
      if (kedbRatingInsideDTO != null) {
        if (kedbRatingInsideDTO.getKedbId() == null) {
          dto.setDescription("KEDB is required ");
          dto.setResult(RESULT.FAIL);
          return dto;
        } else {
          dto.setKedbId(kedbRatingInsideDTO.getKedbId());
        }
        if (!StringUtils.isNotNullOrEmpty(kedbRatingInsideDTO.getUserName())) {
          dto.setDescription("UserName is required ");
          dto.setResult(RESULT.FAIL);
          return dto;
        } else {
          dto.setUserName(kedbRatingInsideDTO.getUserName());
        }
        if (kedbRatingInsideDTO.getPoint() == null) {
          dto.setDescription("Point is required ");
          dto.setResult(RESULT.FAIL);
          return dto;
        }
        if (!StringUtils.isNotNullOrEmpty(kedbRatingInsideDTO.getNote())) {
          dto.setDescription("Note is required ");
          dto.setResult(RESULT.FAIL);
          return dto;
        }
        List<KedbRatingEntity> listEntity = kedbRatingRepository
            .findKedbRatingEntityByMultilParam(dto);
        ResultInSideDto result;
        if (listEntity != null && listEntity.size() > 0) {
          KedbRatingInsideDTO temp = listEntity.get(0).toDTO();
          temp.setPoint(kedbRatingInsideDTO.getPoint());
          temp.setNote(kedbRatingInsideDTO.getNote());
          result = updateKedbRating(temp);
        } else {
          result = insertKedbRating(kedbRatingInsideDTO);
        }
        if (RESULT.SUCCESS.equals(result.getKey())) {
//          return getKedbRating(kedbRatingInsideDTO);
          dto.setResult(RESULT.SUCCESS);
        } else {
          dto.setResult(RESULT.FAIL);
        }
      } else {
        dto.setResult(RESULT.FAIL);
      }
    } catch (Exception e) {
      dto.setResult(RESULT.FAIL);
      dto.setDescription(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return dto;
  }

  @Override
  public ResultInSideDto insertOrUpdateListKedbRating(
      List<KedbRatingInsideDTO> listKedbRatingInsideDTO) {
    log.debug("Request to insertOrUpdateListKedbRating: {}", listKedbRatingInsideDTO);
    return kedbRatingRepository.insertOrUpdateListKedbRating(listKedbRatingInsideDTO);
  }

  @Override
  public ResultInSideDto deleteKedbRating(Long id) {
    log.debug("Request to deleteKedbRating: {}", id);
    return kedbRatingRepository.deleteKedbRating(id);
  }

  @Override
  public ResultInSideDto deleteListKedbRating(List<Long> listId) {
    log.debug("Request to deleteListKedbRating: {}", listId);
    ResultInSideDto result = new ResultInSideDto();
    for (Long id : listId) {
      result = deleteKedbRating(id);
    }
    return result;
  }

  @Override
  public List<String> getSequenseKedbRating(int... size) {
    log.debug("Request to getSequenseKedbRating: {}");
    return kedbRatingRepository.getSequenseKedbRating(KEDB_RATING_SEQ, 1);
  }

  @Override
  public List<KedbRatingInsideDTO> getListKedbRatingByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    log.debug("Request to getListKedbRatingByCondition: {}", lstCondition);
    return kedbRatingRepository.getListKedbRatingByCondition(lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }
}
