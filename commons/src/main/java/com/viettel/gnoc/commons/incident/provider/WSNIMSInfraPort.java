package com.viettel.gnoc.commons.incident.provider;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.nims.infra.webservice.CatVendorBO;
import com.viettel.nims.infra.webservice.InfraWS;
import com.viettel.nims.infra.webservice.InputObjectForm;
import com.viettel.nims.infra.webservice.ResultCableForm;
import com.viettel.nims.infra.webservice.VCntCableLinkBO;
import com.viettel.nims.infra.webservice.VInfraCableWsBO;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("WSNIMSInfraPorts")
public class WSNIMSInfraPort {

  InfraWS port = null;

  @Autowired
  @Qualifier("WSNIMSInfraPortFactorys")
  WSNIMSInfraPortFactory wsnimsInfraPortFactory;
  @Value("${application.conf.user_name_nims_2:null}")
  private String username;
  @Value("${application.conf.password_nims_2:null}")
  private String password;


  @PostConstruct
  public void init() {
    try {
      if (StringUtils.isNotNullOrEmpty(username) && StringUtils.isNotNullOrEmpty(password)) {
        username = PassTranformer.decrypt(username);
        password = PassTranformer.decrypt(password);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }


  private void createConnect() throws Exception {
    port = (InfraWS) wsnimsInfraPortFactory.getWsFactory().borrowObject();
  }

  public List<CatVendorBO> getVendorList(String vendorCode, String vendorName) throws Exception {
    List<CatVendorBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {
        res = port.getVendor(null, null);
      }

      if (res == null) {
        return new ArrayList<>();
      }

      for (int i = res.size() - 1; i >= 0; i--) {
        if (res.get(i).getVendorCode() == null) {
          res.remove(i);
          continue;
        }
        if (vendorCode != null && !vendorCode.trim().isEmpty()) {
          if (!String.valueOf(res.get(i).getVendorCode()).toUpperCase()
              .contains(vendorCode.trim().toUpperCase())) {
            res.remove(i);
            continue;
          }
        }
        if (vendorName != null && !vendorName.trim().isEmpty()) {
          if (!String.valueOf(res.get(i).getVendorName()).toUpperCase()
              .contains(vendorName.trim().toUpperCase())) {
            res.remove(i);
          }
        }

      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public List<LinkInfoDTO> getLinkInfo(List<String> s) throws Exception {

    List<LinkInfoDTO> lstDataMerge = new ArrayList<>();
    try {
      List<VCntCableLinkBO> lstTmp = getLinkInCables(s);
      List<LinkInfoDTO> lstData = new ArrayList<>();
      if (lstTmp != null && !lstTmp.isEmpty()) {
        for (VCntCableLinkBO bo : lstTmp) {
          LinkInfoDTO dto = new LinkInfoDTO();

          dto.setCapacity(bo.getCapacity());
          if (bo.getLastUpdateTime() != null) {
            dto.setCreateTime(
                DateTimeUtils.convertDateTimeStampToString(toDate(bo.getLastUpdateTime())));
          }
          dto.setDevice1(bo.getStartDeviceCode());
          dto.setDevice2(bo.getEndDeviceCode());
          dto.setFiber(bo.getPk().getLineNo() == null ? "" : bo.getPk().getLineNo().toString());
//                    dto.setLinkName(bo.getLinkName());
          dto.setLinkName(
              bo.getStartDeviceCode() + ":" + bo.getStartPort() + "-" + bo.getEndDeviceCode() + ":"
                  + bo.getEndPort()); //Thiết_bị_1:port 1-Thiết_bị_2:port2
          if (bo.getLastUpdateTime() != null) {
            dto.setModifyTime(
                DateTimeUtils.convertDateTimeStampToString(toDate(bo.getLastUpdateTime())));
          }
          dto.setPort1(bo.getStartPort());
          dto.setPort2(bo.getEndPort());
          lstData.add(dto);
        }
      }
      if (lstData != null && lstData.size() > 0) {

        Map<String, LinkInfoDTO> mapMerge = new HashMap<>();
        for (LinkInfoDTO k : lstData) {
          String key = k.getDevice1() + ":" + k.getPort1();
          if (mapMerge.containsKey(key)) {
            LinkInfoDTO tmp = mapMerge.get(key);
            String[] arrFiber = tmp.getFiber().split(",");
            List<String> l = Arrays.asList(arrFiber);
            if (l != null && !l.contains(k.getFiber())) {
              tmp.setFiber(tmp.getFiber() + "," + k.getFiber());
            }
          } else {
            mapMerge.put(key, k);
          }
        }
        for (Map.Entry<String, LinkInfoDTO> entry : mapMerge.entrySet()) {
          lstDataMerge.add(entry.getValue());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lstDataMerge;
  }

  public List<VCntCableLinkBO> getLinkInCables(List<String> s) throws Exception {
    List<VCntCableLinkBO> res = null;
    try {
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        res = port.getLinkInCables(s);
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  //doan cap
  public List<CrCableDTO> getAllCableInLane(String laneCode) throws Exception {
    List<CrCableDTO> res = new ArrayList<>();
    try {
      InputObjectForm objectForm = new InputObjectForm();
      objectForm.setLaneCode(laneCode);
      if (port == null) {
        createConnect();
      }
      if (port != null) {

        ResultCableForm cableForm = port.getAllCableInLane(objectForm, username, password);
        if (cableForm != null && cableForm.getCables() != null && !cableForm.getCables()
            .isEmpty()) {
          List<VInfraCableWsBO> lst = cableForm.getCables();
          if (lst != null && !lst.isEmpty()) {
            for (VInfraCableWsBO vicwbo : lst) {
              CrCableDTO cableDTO = new CrCableDTO();
              cableDTO.setCableCode(vicwbo.getCableCode());
              cableDTO.setEndPoint(vicwbo.getEndPoint());
              cableDTO.setStartPoint(vicwbo.getStartPoint());
              cableDTO.setType("0");//truongnt sua doan cap = 0, tuyen cap = 1
              if (vicwbo.getCreateDate() != null) {
                Calendar cal = vicwbo.getCreateDate().toGregorianCalendar();
                cableDTO.setCreatedDate(DateTimeUtils.date2ddMMyyyyHHMMss(cal.getTime()));
              }
              res.add(cableDTO);
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      try {
        if (null != port) {
          wsnimsInfraPortFactory.getWsFactory().returnObject(port);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return res;
  }

  public static Date toDate(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    }
    return calendar.toGregorianCalendar().getTime();
  }
}

