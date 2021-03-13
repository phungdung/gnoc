package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.model.WoCdGroupTypeEntity;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoCdGroupTypeRepositoryImpl extends BaseRepository implements WoCdGroupTypeRepository {

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Override
  public List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(WoCdGroupTypeDTO woCdGroupTypeDTO) {
    if (woCdGroupTypeDTO != null) {
      List<WoCdGroupTypeDTO> list = onSearchEntity(WoCdGroupTypeEntity.class, woCdGroupTypeDTO,
          0, Integer.MAX_VALUE, "ASC", "groupTypeName");
      try {
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
                Constants.APPLIED_BUSSINESS.GROUP_TYPE, I18n.getLocale());
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        list = DataUtil.setLanguage(list, lstLanguage, "groupTypeId", "groupTypeName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      return list;
    }
    return null;
  }
}
