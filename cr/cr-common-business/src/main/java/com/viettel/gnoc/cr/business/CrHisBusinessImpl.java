package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.repository.CrHisRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CrHisBusinessImpl implements CrHisBusiness {

  @Autowired
  CrHisRepository crHisRepository;


  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Override
  public Datatable searchSql(CrHisDTO crhisdto) {
    List<CrHisDTO> lst = new ArrayList<>();
    Datatable data = new Datatable();
    try {
      String myLanguage = I18n.getLocale();
      if (crhisdto != null && !StringUtils.isStringNullOrEmpty(crhisdto.getCrId())) {
        lst = crHisRepository.getListCrHis(crhisdto);
        Map<String, Object> map = DataUtil
            .getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
                Constants.APPLIED_BUSSINESS.RETURN_CODE_CATALOG, myLanguage);
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
        lst = DataUtil.setLanguage(lst, lstLanguage, "returnCodeId", "returnTitle");

        if (lst != null && !lst.isEmpty()) {
          for (CrHisDTO item : lst) {
            if (item.getActionCode() != null) {
              item.setActionName(StringUtils
                  .convertKeyToValueByMap(Constants.CR_ACTION_CODE.getGetActionCodeName(),
                      item.getActionCode()));
            }
            if (!StringUtils.isStringNullOrEmpty(item.getStatus())) {
              item.setStatusName(StringUtils
                  .convertKeyToValueByMap(Constants.CR_STATE.getGetStateName(), item.getStatus()));
            }
          }
        }
        if (lst != null && lst.size() > 0) {
          data.setTotal(lst.size());
          int pages = (int) Math.ceil(lst.size() * 1.0 / crhisdto.getPageSize());
          data.setPages(pages);
          data.setData(
              DataUtil.subPageList(lst, crhisdto.getPage(), crhisdto.getPageSize()));
        }
      }
      return data;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<CrHisDTO> search(CrHisDTO tDTO, int start, int maxResult, String sortType,
      String sortField) {
    return crHisRepository.search(tDTO, start, maxResult, sortType, sortField);
  }
}
