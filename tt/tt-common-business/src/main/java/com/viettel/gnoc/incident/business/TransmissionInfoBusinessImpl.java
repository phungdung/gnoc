package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.InfraSleevesDTO;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TransmissionInfoRepository;
import com.viettel.gnoc.incident.utils.WSNIMSInfraPort;
import com.viettel.nims.infra.webservice.InfraSleevesBO;
import com.viettel.nims.infra.webservice.VCntCableLinkBO;
import com.viettel.nims.infra.webservice.VInfraCableWsBO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class TransmissionInfoBusinessImpl implements TransmissionInfoBusiness {

  @Autowired
  TransmissionInfoRepository transmissionInfoRepository;

  @Autowired
  WSNIMSInfraPort wsnimsInfraPort;


  //loaiNN
  @Override
  public List<CatItemDTO> getListCatReason(String itemId) {
    String parentId = "";
    String excludeCode = "";
    if ("2046".equals(itemId)) {
      parentId = "23";
      excludeCode = "EM_CAUSE_MAT LUONG DOWNCELL_TRUYEN DAN_VIBA_LOI CAP LUONG VIBA";
    }
    if ("2047".equals(itemId)) {
      parentId = "22";
      excludeCode = "EM_CAUSE_MAT LUONG DOWNCELL_TRUYEN DAN_QUANG_LOI CAP LUONG";
    }
    List<CatItemDTO> lstReturn = new ArrayList();
    if (parentId != null && !"".equals(parentId)) {
      CatReasonInSideDTO dtoSearch = new CatReasonInSideDTO();
      dtoSearch.setParentId(Long.parseLong(parentId));

      List<CatReasonInSideDTO> lstReason = transmissionInfoRepository
          .getListReasonSearch(dtoSearch);
      for (CatReasonInSideDTO reason : lstReason) {
        CatItemDTO catDTO = new CatItemDTO();
        if (!excludeCode.equals(reason.getReasonCode())) {
          catDTO.setItemId(Long.valueOf(reason.getId()));
          catDTO.setItemCode(reason.getReasonCode());
          catDTO.setItemName(reason.getReasonName());
          catDTO.setDescription(reason.getDescription());
          lstReturn.add(catDTO);
        }
      }
    }

    return lstReturn;
  }

  //loai mang
  @Override
  public List<CatItemDTO> getListCableType(String lineCutCode, String codeSnippetOff) {
    List<CatItemDTO> lstCableType = new ArrayList<CatItemDTO>();
    //WSNIMSInfraPort nimsPort = new WSNIMSInfraPort();
    String[] tmpCodeSnippetOff = codeSnippetOff.split(",");
    List<String> lstDoanDut = Arrays.asList(tmpCodeSnippetOff);
    // tuyến đứt
    String[] tmpLineCut = lineCutCode.split(",");
    List<String> lstTuyenDut = Arrays.asList(tmpLineCut);
    // load cableType
    List<VInfraCableWsBO> infraCableBO = new ArrayList<VInfraCableWsBO>();
    try {
      for (String s : lstTuyenDut) {
        infraCableBO.addAll(wsnimsInfraPort.getCableInLanes(s));
      }
      if (infraCableBO != null && !infraCableBO.isEmpty()) {
        List<Long> lstId = new ArrayList<Long>();
        for (int i = 0; i < infraCableBO.size(); i++) {
          if (lstDoanDut.contains(infraCableBO.get(i).getCableCode())) {
            if (!lstId.contains(infraCableBO.get(i).getOpticalCableType())) {
              CatItemDTO dtoTmp = new CatItemDTO(
                  infraCableBO.get(i).getOpticalCableType().toString(),
                  infraCableBO.get(i).getOpticalCableType().equals(1L) ? I18n
                      .getString("incident.slings") : I18n.getString("incident.buried.cable"));
              lstCableType.add(dtoTmp);
              lstId.add(infraCableBO.get(i).getOpticalCableType());
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstCableType;
    // return null;
  }

  //ma doan dut
  @Override
  public List<CatItemDTO> getListSnippetOff(String lineCutCode) {
    List<CatItemDTO> lstSnippetOff = new ArrayList<CatItemDTO>();
    try {
      List<VInfraCableWsBO> INFRA_CABLE_B_OS = new ArrayList<VInfraCableWsBO>();
      if (lineCutCode != null && !"".equals(lineCutCode)) {
        String[] s = lineCutCode.split(",");
        for (String string : s) {
          INFRA_CABLE_B_OS.addAll(wsnimsInfraPort.getCableInLanes(string));
        }
      } else {
      }
      for (VInfraCableWsBO i : INFRA_CABLE_B_OS) {
        lstSnippetOff.add(new CatItemDTO(i.getCableId().toString(), i.getCableCode()));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstSnippetOff;
    //return null;
  }

  //View List
  @Override
  public List<LinkInfoDTO> getListLinkInfoDTO(String codeSnippetOff) {
    List<LinkInfoDTO> lstData = new ArrayList<LinkInfoDTO>();
    List<LinkInfoDTO> lstDataMerge = new ArrayList<>();
    if (codeSnippetOff != null && !"".equals(codeSnippetOff.trim())) {

      codeSnippetOff = codeSnippetOff.replaceAll(", ", ",");
      String[] tmpCodeSnippetOff = codeSnippetOff.split(",");
      List<String> lstDoanDut = Arrays.asList(tmpCodeSnippetOff);

      try {
        List<VCntCableLinkBO> lstTmp = wsnimsInfraPort.getLinkInCables(lstDoanDut);
        for (VCntCableLinkBO bo : lstTmp) {
          LinkInfoDTO dto = new LinkInfoDTO();
          dto.setCapacity(bo.getCapacity());
          if (bo.getLastUpdateTime() != null) {
            dto.setCreateTime(
                DateTimeUtils.convertDateTimeStampToString(toDate(bo.getCreateDate())));
          }
          dto.setDevice1(bo.getStartDeviceCode());
          dto.setDevice2(bo.getEndDeviceCode());
          dto.setFiber(bo.getPk().getLineNo() == null ? "" : bo.getPk().getLineNo().toString());
          dto.setLinkName(bo.getLinkName());
          if (bo.getLastUpdateTime() != null) {
            dto.setModifyTime(
                DateTimeUtils.convertDateTimeStampToString(toDate(bo.getLastUpdateTime())));
          }
          dto.setPort1(bo.getStartPort());
          dto.setPort2(bo.getEndPort());
          lstData.add(dto);

        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (lstData != null && lstData.size() > 0) {

        Map<String, LinkInfoDTO> mapMerge = new HashMap<String, LinkInfoDTO>();
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
    }
    return lstDataMerge;
    //return null;
  }

  @Override
  public Datatable onSearchInfraCableLaneDTO(InfraCableLaneDTO infraCableLaneDTO) {
    Datatable datatable = new Datatable();
    if (infraCableLaneDTO.getLaneCode() != null) {
      datatable = transmissionInfoRepository.onSearchInfraCableLaneDTO(infraCableLaneDTO);
    }
    return datatable;
  }


  //search mang xong thay the
  @Override
  public Datatable onSearchInfraSleevesDTO(String nameCode, String codeSnippetOff,
      TroublesInSideDTO troublesDTO) {
    List<InfraSleevesDTO> lstReturn = new ArrayList<InfraSleevesDTO>();
    Datatable datatable = new Datatable();
    int page = troublesDTO.getPage();
    int size = troublesDTO.getPageSize();
    if (nameCode != null) {
      try {
        List<InfraSleevesBO> lst = new ArrayList<InfraSleevesBO>();
        if (codeSnippetOff != null && !"".equals(codeSnippetOff)) {
          String[] s = codeSnippetOff.split(",");
          for (String string : s) {
            lst.addAll(wsnimsInfraPort.getSleevesInCable(nameCode, 1L,
                string)); //HaiNV20 sua, lay nhung mang xong phuc vu cho xu ly su co.
          }
        } else {
          lst.addAll(wsnimsInfraPort.getInfraSleevesBO(null, nameCode, 1L));
        }
        if (lst.size() > 0) {
          for (InfraSleevesBO bo : lst) {
            Date modifyDate;
            if (bo.getModifyDate() != null) {
              modifyDate = toDate(bo.getModifyDate());
              if (DateUtil.compareDate(modifyDate, troublesDTO.getCreatedTime()) >= 0) {
                lstReturn.add(new InfraSleevesDTO(modifyDate, bo.getSleeveCode(), bo.getSleeveId(),
                    bo.getSleeveName(), "0")); //Chi add nhung mang xong sau thoi gian tao ticket
              }
            }
          }
        }
        if (lstReturn != null && lstReturn.size() > 0) {
          int totalSize = lstReturn.size();
          int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
          List<InfraSleevesDTO> infraSublist = (List<InfraSleevesDTO>) DataUtil
              .subPageList(lstReturn, page, size);
          if (infraSublist != null && infraSublist.size() > 0) {
            infraSublist.get(0).setPage(page);
            infraSublist.get(0).setPageSize(pageSize);
            infraSublist.get(0).setTotalRow(totalSize);
          }

          datatable.setData(infraSublist);
          datatable.setTotal(totalSize);
          datatable.setPages(pageSize);
        }


      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return datatable;
  }

  public static Date toDate(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      return null;
    }
    return calendar.toGregorianCalendar().getTime();
  }
}
