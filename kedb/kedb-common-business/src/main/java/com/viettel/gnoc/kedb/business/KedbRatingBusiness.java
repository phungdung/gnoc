package com.viettel.gnoc.kedb.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbRatingInsideDTO;
import java.util.List;

public interface KedbRatingBusiness {

  List<KedbRatingInsideDTO> getListKedbRatingDTO(KedbRatingInsideDTO kedbRatingInsideDTO);

  KedbRatingInsideDTO findKedbRatingById(Long id);

  KedbRatingInsideDTO getKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto insertKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto updateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  KedbRatingInsideDTO insertOrUpdateKedbRating(KedbRatingInsideDTO kedbRatingInsideDTO);

  ResultInSideDto insertOrUpdateListKedbRating(List<KedbRatingInsideDTO> listKedbRatingInsideDTO);

  ResultInSideDto deleteKedbRating(Long id);

  ResultInSideDto deleteListKedbRating(List<Long> listId);

  List<String> getSequenseKedbRating(int... size);

  List<KedbRatingInsideDTO> getListKedbRatingByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
