package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import com.viettel.gnoc.mr.repository.MrHisSearchRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrHisSearchBusinessImpl implements MrHisSearchBusiness {

  @Autowired
  MrHisSearchRepository mrHisSearchRepository;

  @Override
  public List<MrHisSearchDTO> getListMrHisSearch(MrHisSearchDTO his) {
    Map<String, String> mapAction = new HashMap<>();
    mapAction.put("1", I18n.getLanguage("mr.history.list.actionCode.1"));
    mapAction.put("3", I18n.getLanguage("mr.history.list.actionCode.3"));
    mapAction.put("2", I18n.getLanguage("mr.history.list.actionCode.2"));
    mapAction.put("4", I18n.getLanguage("mr.history.list.actionCode.4"));
    mapAction.put("5", I18n.getLanguage("mr.history.list.actionCode.5"));
    List<MrHisSearchDTO> lst = new ArrayList<>();
    try {
      List<MrHisSearchDTO> lstTemp = mrHisSearchRepository.getListMrHisSearch(his);
      if (lstTemp != null && !lstTemp.isEmpty()) {
        for (MrHisSearchDTO h : lstTemp) {
          h.setActionCode(mapAction.get(h.getActionCode()) == null
              ? h.getActionCode() : mapAction.get(h.getActionCode()));
          lst.add(h);
        }
      }
      return lst;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return null;
    }
  }
}
