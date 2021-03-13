package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.DataHistoryChange;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.MASTER_DATA;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusRoleDTO;
import com.viettel.gnoc.od.dto.OdExportCfgBusinessDTO;
import com.viettel.gnoc.od.model.OdTypeEntity;
import com.viettel.gnoc.od.repository.OdCfgBusinessRepository;
import com.viettel.gnoc.od.repository.OdChangeStatusRepository;
import com.viettel.gnoc.od.repository.OdChangeStatusRoleRepository;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class OdChangeStatusBusinessImpl implements OdChangeStatusBusiness {

  @Autowired
  protected OdChangeStatusRepository odChangeStatusRepository;

  @Autowired
  protected OdCfgBusinessRepository odCfgBusinessRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  OdChangeStatusRoleRepository odChangeStatusRoleRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO) {
    log.debug("Request to getListOdChangeStatus : {}", odChangeStatusDTO);
    Datatable datatable = odChangeStatusRepository.getListOdCfgBusiness(odChangeStatusDTO);
    if (datatable != null) {
      List<OdChangeStatusDTO> odChangeStatusDTOS = (List<OdChangeStatusDTO>) datatable.getData();
      if (odChangeStatusDTOS != null) {
        for (int i = 0; i < odChangeStatusDTOS.size(); i++) {
          if (odChangeStatusDTOS.get(i).getIsDefault() != null
              && odChangeStatusDTOS.get(i).getIsDefault() == 1) {
            odChangeStatusDTOS.get(i).setIsDefaultName(I18n.getLanguage("odCfgBusiness.yes"));
          } else {
            odChangeStatusDTOS.get(i).setIsDefaultName(I18n.getLanguage("odCfgBusiness.no"));
          }
        }
        datatable.setData(odChangeStatusDTOS);
      }
    }
    return datatable;
  }

  @Override
  public String getSeqOdChangeStatus() {
    log.debug("Request to getSeqOdChangeStatus : {}");
    return odChangeStatusRepository.getSeqOdChangeStatus();
  }

  @Override
  public ResultInSideDto deleteCfg(Long odChangeStatusId) {
    log.debug("Request to delete : {}", odChangeStatusId);
    ResultInSideDto resultInSideDto;
    OdChangeStatusDTO oldHis = getDetailCfg(odChangeStatusId);
    odCfgBusinessRepository.deleteByOdChangeStatusId(odChangeStatusId);
    odChangeStatusRoleRepository.deleteByOdChangeStatusId(odChangeStatusId);
    resultInSideDto = odChangeStatusRepository.deleteCfg(odChangeStatusId);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(odChangeStatusId.toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new OdChangeStatusDTO());
        dataHistoryChange.setType("OD_CONFIG_BUSINESS");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCfg(List<Long> odChangeStatusIds) {
    log.debug("Request to deleteList : {}", odChangeStatusIds);
    return odChangeStatusRepository.deleteListCfg(odChangeStatusIds);
  }

  @Override
  public String delete(Long id) {
    return odChangeStatusRepository.delete(id);
  }

  @Override
  public String delete(List<OdChangeStatusDTO> odChangeStatusDTOs) {
    List<Long> listIds = new ArrayList<>();
    odChangeStatusDTOs.forEach(c -> {
      listIds.add(c.getId());
    });
    String checkConstraint = odChangeStatusRepository.checkConstraint(listIds);
    if (checkConstraint == null || !checkConstraint.equals(RESULT.SUCCESS)) {
      return checkConstraint;
    }
    // Xóa key ngôn ngữ
    String deleteLocale = odChangeStatusRepository.deleteLocaleList(listIds);
    if (deleteLocale == null || !deleteLocale.equals(RESULT.SUCCESS)) {
      return checkConstraint;
    }
    int countSuccess = odChangeStatusRepository.deleteList(listIds);
    return (countSuccess > 0) ? RESULT.SUCCESS : RESULT.ERROR;
  }

  @Override
  public int deleteListOdChangeStatus(List<Long> ids) {
    return odChangeStatusRepository.deleteList(ids);
  }

  @Override
  public OdChangeStatusDTO getDetailCfg(Long id) {
    log.debug("Request to getDetail : {}", id);
    OdChangeStatusDTO odChangeStatusDTO = null;
    if (id > 0) {
      odChangeStatusDTO = odChangeStatusRepository.findOdChangeStatusDTOById(id);
    } else {
      odChangeStatusDTO = new OdChangeStatusDTO(id, null, null, null,
          null, null, null, null,
          null, null, null, null, null, null, null);
    }
    if (odChangeStatusDTO != null) {
      List<OdCfgBusinessDTO> odCfgBusinessDTO = odCfgBusinessRepository
          .getListOdCfgBusiness(odChangeStatusDTO);
      odChangeStatusDTO.setOdCfgBusinessDTO(odCfgBusinessDTO);
      if (id > 0) {
        List<OdChangeStatusRoleDTO> odChangeStatusRoleDTOS = odChangeStatusRoleRepository
            .getListByOdChangeStatusId(id);
        odChangeStatusDTO.setOdChangeStatusRoleDTO(odChangeStatusRoleDTOS);
      }
      return odChangeStatusDTO;
    }

    return null;
  }

  @Override
  public ResultInSideDto addList(List<OdChangeStatusDTO> odChangeStatusDTOs) throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    for (OdChangeStatusDTO odChangeStatusDTO : odChangeStatusDTOs) {
      insertOrUpdateCfg(odChangeStatusDTO);
    }
    result.setKey(RESULT.SUCCESS);
    return result;
  }

  @Override
  public ResultInSideDto insertOrUpdateCfg(OdChangeStatusDTO odChangeStatusDTO) throws Exception {
    ResultInSideDto result = null;
    try {
      String msg = validateData(odChangeStatusDTO);
      if (msg.length() > 0) {
        throw new RuntimeException(msg);
      }
      OdChangeStatusDTO oldHis = new OdChangeStatusDTO();
      if (odChangeStatusDTO.getId() != null) {
        oldHis = getDetailCfg(odChangeStatusDTO.getId());
      }
      result = odChangeStatusRepository.insertOrUpdate(odChangeStatusDTO);
      if (result.getKey().equals(RESULT.SUCCESS)) {
        //Add history
        try {
          UserToken userToken = ticketProvider.getUserToken();
          List<String> keys = getAllKeysDTO();
          DataHistoryChange dataHistoryChange = new DataHistoryChange();
          dataHistoryChange.setRecordId(result.getId().toString());
          dataHistoryChange.setType("OD_CONFIG_BUSINESS");
          dataHistoryChange.setOldObject(oldHis);
          dataHistoryChange.setActionType(odChangeStatusDTO.getId() != null ? "update" : "add");
          //New Object History
          dataHistoryChange.setNewObject(odChangeStatusDTO);
          dataHistoryChange.setUserId(userToken.getUserID().toString());
          dataHistoryChange.setKeys(keys);
          commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
        } catch (Exception err) {
          log.error(err.getMessage());
        }
      }
      if (odChangeStatusDTO.getId() != null) {
        odCfgBusinessRepository.deleteByOdChangeStatusId(odChangeStatusDTO.getId());
        odChangeStatusRoleRepository.deleteByOdChangeStatusId(odChangeStatusDTO.getId());
      } else {
        odChangeStatusDTO.setId(result.getId());
      }
      odCfgBusinessRepository.add(odChangeStatusDTO);
      odChangeStatusRoleRepository.addList(odChangeStatusDTO);
      result.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    return result;
  }

  @Override
  public File exportData(OdChangeStatusDTO odChangeStatusDTO) throws Exception {
    List<OdExportCfgBusinessDTO> lstExport = odChangeStatusRepository
        .getOdCfgBusinessDataExport(odChangeStatusDTO);
    String txtYes = I18n.getLanguage("odCfgBusiness.yes");
    String txtNo = I18n.getLanguage("odCfgBusiness.no");
    if (lstExport != null) {
      for (int i = 0; i < lstExport.size(); i++) {
        if (lstExport.get(i).getIsVisible() != null && lstExport.get(i).getIsVisible() == 1) {
          lstExport.get(i).setIsVisibleName(txtYes);
        } else {
          lstExport.get(i).setIsVisibleName(txtNo);
        }
        if (lstExport.get(i).getIsDefault() != null && lstExport.get(i).getIsDefault() == 1) {
          lstExport.get(i).setIsDefaultName(txtYes);
        } else {
          lstExport.get(i).setIsDefaultName(txtNo);
        }
        if (lstExport.get(i).getIsRequired() != null && lstExport.get(i).getIsRequired() == 1) {
          lstExport.get(i).setIsRequiredName(txtYes);
        } else {
          lstExport.get(i).setIsRequiredName(txtNo);
        }
        if (lstExport.get(i).getEditable() != null && lstExport.get(i).getEditable() == 1) {
          lstExport.get(i).setIsEditableName(txtYes);
        } else {
          lstExport.get(i).setIsEditableName(txtNo);
        }

      }
    }

    return getFileExport(lstExport, I18n.getLanguage("odCfgBusiness.export_cfg_title"),
        "OD_CFG_BUSINESS", 4, 2, 9, "TEMPLATE_EXPORT.xlsx",
        "EXPORT_DATA_", "odTypeName", "oldStatusName", "newStatusName", "odPriorityName",
        "isDefaultName", "isRequiredName", "columnNameValue", "isEditableName", "isVisibleName",
        "message");
  }

  // API import Excel
  @Override
  public ResultInSideDto importData(MultipartFile uploadfile) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.ERROR);
    String txtYes = I18n.getLanguage("odCfgBusiness.yes");
    try {
      if (uploadfile.isEmpty()) {
        resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = upFile(uploadfile);
        File fileImport = new File(filePath);
        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport, 0, 5, 0, 11, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            6,//begin row
            0,//from column
            11,//to column
            1000
        );

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        Map<String, String> odStatus = getItemMaster(CATEGORY.OD_STATUS);
        Map<String, String> odPriority = getItemMaster(CATEGORY.OD_PRIORITY);
        Map<String, String> odType = getOdTypeMaster();
        Map<String, String> odColumn = getItemMaster(OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN);
        Map<String, OdChangeStatusDTO> mapOdChangeStatusDTO = new HashMap<>();
        Map<String, String> vioExportKey = new HashMap<>();
        List<OdExportCfgBusinessDTO> lstOdExportCfgBusinessDTOS = new ArrayList<>();
        List<OdCfgBusinessDTO> odCfgBusinessDTOS;

        for (Object[] c : lstData) {
          OdExportCfgBusinessDTO odExportCfgBusinessDTO = new OdExportCfgBusinessDTO();
          if (c[1] != null && c[1].toString().trim().length() > 0) {
            OdChangeStatusDTO odChangeStatusDTO;
            OdCfgBusinessDTO odCfgBusinessDTO;
            odExportCfgBusinessDTO.setGroupCfg(c[1].toString().trim());
            if (mapOdChangeStatusDTO.containsKey(c[1].toString().trim())) {
              odChangeStatusDTO = mapOdChangeStatusDTO.get(c[1].toString().trim());
              odCfgBusinessDTOS = odChangeStatusDTO.getOdCfgBusinessDTO();
              odCfgBusinessDTO = new OdCfgBusinessDTO();
              if (c[2] != null) {
                odExportCfgBusinessDTO.setOdTypeName(c[2].toString().trim());
              }
              if (c[3] != null) {
                odExportCfgBusinessDTO.setOldStatusName(c[3].toString().trim());
              }
              if (c[4] != null) {
                odExportCfgBusinessDTO.setNewStatusName(c[4].toString().trim());
              }
              if (c[5] != null) {
                odExportCfgBusinessDTO.setOdPriorityName(c[5].toString().trim());
              }
              if (c[6] != null) {
                odExportCfgBusinessDTO.setIsDefaultName(c[6].toString().trim());
              }
            } else {
              odChangeStatusDTO = new OdChangeStatusDTO();
              odCfgBusinessDTO = new OdCfgBusinessDTO();
              odCfgBusinessDTOS = new ArrayList<>();
              if (c[2] != null) {
                odExportCfgBusinessDTO.setOdTypeName(c[2].toString().trim());
                String odTypeName = odExportCfgBusinessDTO.getOdTypeName();
                for (Map.Entry<String, String> item : odType.entrySet()) {
                  if (odTypeName.equals(item.getValue())) {
                    odChangeStatusDTO.setOdTypeId(Long.valueOf(item.getKey()));
                    break;
                  }
                }
              }

              if (c[3] != null) {
                odExportCfgBusinessDTO.setOldStatusName(c[3].toString().trim());
                String oldStatusName = odExportCfgBusinessDTO.getOldStatusName();
                for (Map.Entry<String, String> item : odStatus.entrySet()) {
                  if (oldStatusName.equals(item.getValue())) {
                    odChangeStatusDTO.setOldStatus(Long.valueOf(item.getKey()));
                    break;
                  }
                }
              }

              if (c[4] != null) {
                odExportCfgBusinessDTO.setNewStatusName(c[4].toString().trim());
                String newStatusName = odExportCfgBusinessDTO.getNewStatusName();
                for (Map.Entry<String, String> item : odStatus.entrySet()) {
                  if (newStatusName.equals(item.getValue())) {
                    odChangeStatusDTO.setNewStatus(Long.valueOf(item.getKey()));
                    break;
                  }
                }
              }

              if (c[5] != null) {
                odExportCfgBusinessDTO.setOdPriorityName(c[5].toString().trim());
                String odPriorityName = odExportCfgBusinessDTO.getOdPriorityName();
                for (Map.Entry<String, String> item : odPriority.entrySet()) {
                  if (odPriorityName.equals(item.getValue())) {
                    odChangeStatusDTO.setOdPriority(Long.valueOf(item.getKey()));
                    break;
                  }
                }
              }

              if (c[6] != null) {
                odExportCfgBusinessDTO.setIsDefaultName(c[6].toString().trim());
                String isDefault = odExportCfgBusinessDTO.getIsDefaultName();
                if (txtYes.equals(isDefault)) {
                  odChangeStatusDTO.setIsDefault(1L);
                } else {
                  odChangeStatusDTO.setIsDefault(0L);
                }
              } else {
                odChangeStatusDTO.setIsDefault(0L);
              }

              if (odChangeStatusDTO.getOldStatus() == null) {
                odExportCfgBusinessDTO
                    .setResultImport(I18n.getValidation("odCfgBusiness.oldStatus.isRequired"));
                vioExportKey.put(c[1].toString(), c[1].toString());
              }
              if (odChangeStatusDTO.getNewStatus() == null) {
                odExportCfgBusinessDTO
                    .setResultImport(I18n.getValidation("odCfgBusiness.newStatus.isRequired"));
                vioExportKey.put(c[1].toString(), c[1].toString());
              }
              if (odChangeStatusDTO.getOdPriority() == null) {
                odExportCfgBusinessDTO
                    .setResultImport(I18n.getValidation("odCfgBusiness.odPriority.isRequired"));
              }
              if (odChangeStatusDTO.getOldStatus() != null
                  && odChangeStatusDTO.getNewStatus() != null
                  && odChangeStatusDTO.getOdPriority() != null) {
                if (odChangeStatusRepository.isExitOdChangeStatusDTO(odChangeStatusDTO)) {
                  odExportCfgBusinessDTO
                      .setResultImport(I18n.getValidation("odCfgBusiness.data.dup"));
                  vioExportKey.put(c[1].toString(), c[1].toString());
                }
              }

            }

            if (c[7] != null) {
              odExportCfgBusinessDTO.setIsRequiredName(c[7].toString().trim());
              String isRequired = odExportCfgBusinessDTO.getIsRequiredName();
              if (txtYes.equals(isRequired)) {
                odCfgBusinessDTO.setIsRequired(1L);
              } else {
                odCfgBusinessDTO.setIsRequired(0L);
              }
            } else {
              odCfgBusinessDTO.setIsRequired(0L);
            }

            if (c[8] != null) {
              odExportCfgBusinessDTO.setColumnNameValue(c[8].toString().trim());
              String columnName = odExportCfgBusinessDTO.getColumnNameValue();
              for (Map.Entry<String, String> item : odColumn.entrySet()) {
                if (columnName.equals(item.getValue())) {
                  odCfgBusinessDTO.setColumnName(item.getKey());
                  break;
                }
              }
            }

            if (c[9] != null) {
              odExportCfgBusinessDTO.setIsEditableName(c[9].toString().trim());
              String editable = odExportCfgBusinessDTO.getIsEditableName();
              if (txtYes.equals(editable)) {
                odCfgBusinessDTO.setEditable(1L);
              } else {
                odCfgBusinessDTO.setEditable(0L);
              }
            }

            if (c[10] != null) {
              odExportCfgBusinessDTO.setIsVisibleName(c[10].toString().trim());
              String isVisible = odExportCfgBusinessDTO.getIsVisibleName();
              if (txtYes.equals(isVisible)) {
                odCfgBusinessDTO.setIsVisible(1L);
              } else {
                odCfgBusinessDTO.setIsVisible(0L);
              }
            }

            if (c[11] != null) {
              odExportCfgBusinessDTO.setMessage(c[11].toString().trim());
              odCfgBusinessDTO.setMessage(odExportCfgBusinessDTO.getMessage());
            }

            odCfgBusinessDTOS.add(odCfgBusinessDTO);
            odChangeStatusDTO.setOdCfgBusinessDTO(odCfgBusinessDTOS);
            lstOdExportCfgBusinessDTOS.add(odExportCfgBusinessDTO);
            mapOdChangeStatusDTO.put(c[1].toString(), odChangeStatusDTO);
          } else {
            odExportCfgBusinessDTO.setResultImport(I18n.getLanguage("odCfgBusiness.STT.invalid"));
          }
        }

        //insert update
        for (Map.Entry<String, OdChangeStatusDTO> item : mapOdChangeStatusDTO.entrySet()) {
          if (!vioExportKey.containsKey(item.getKey())) {
            OdChangeStatusDTO odChangeStatusDTO = item.getValue();
            insertOrUpdateCfg(odChangeStatusDTO);
          }
        }
        int count = 0;
        while (count < lstOdExportCfgBusinessDTOS.size()) {
          if (!vioExportKey.containsKey(lstOdExportCfgBusinessDTOS.get(count).getGroupCfg())) {
            lstOdExportCfgBusinessDTOS.remove(count);
          } else {
            count++;
          }
        }

        if (lstOdExportCfgBusinessDTOS.size() > 0) {
          resultInSideDTO.setFile(getFileExport(lstOdExportCfgBusinessDTOS,
              I18n.getLanguage("odCfgBusiness.import_cfg_title"), "OD_CFG_BUSINESS", 5, 1, 11,
              "TEMPLATE_EXPORT.xlsx",
              "IMPORT_DATA_",
              "groupCfg", "odTypeName", "oldStatusName", "newStatusName", "odPriorityName",
              "isDefaultName", "isRequiredName", "columnNameValue", "isEditableName",
              "isVisibleName", "message", "resultImport"));
        } else {
          resultInSideDTO.setKey(RESULT.SUCCESS);
        }
      }
      return resultInSideDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDTO.setMessage(e.getMessage());
    }
    return resultInSideDTO;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count < 12) {
      return false;
    }

    if (obj0 == null) {
      return false;
    }
    if (obj0[1] == null) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.groupCfg")).equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.odTypeName"))
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.oldStatusName") + "(*)")
        .contains(obj0[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.newStatusName") + "(*)")
        .contains(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.odPriorityName") + "(*)")
        .contains(obj0[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.isDefaultName"))
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.isRequiredName"))
        .equalsIgnoreCase(obj0[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.columnNameValue"))
        .equalsIgnoreCase(obj0[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.isEditableName"))
        .equalsIgnoreCase(obj0[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.isVisibleName"))
        .equalsIgnoreCase(obj0[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odCfgBusiness.message")).equalsIgnoreCase(obj0[11].toString().trim())) {
      return false;
    }
    return true;
  }

  private String upFile(MultipartFile multipartFile) throws Exception {
    return FileUtils
        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), tempFolder);
  }

  public Map<String, String> getItemMaster(String categoryCode) {
    Map<String, String> mapResults = new HashMap<>();
    List<CatItemDTO> catItemDTOS;
    if (OD_MASTER_CODE.OD_CFG_BUSINESS_COLUMN.equals(categoryCode)) {
      Datatable datatable = catItemRepository
          .getItemMaster(categoryCode, MASTER_DATA.OD, MASTER_DATA.OD_CFG_BUSINESS_COLUMN,
              Constants.ITEM_VALUE, Constants.ITEM_NAME);
      catItemDTOS = (List<CatItemDTO>) datatable.getData();
      for (CatItemDTO catItemDTO : catItemDTOS) {
        mapResults.put(String.valueOf(catItemDTO.getItemValue()), catItemDTO.getItemName());
      }
    } else {
      catItemDTOS = catItemRepository.getDataItem(categoryCode);
      if (catItemDTOS != null && !catItemDTOS.isEmpty()) {
        if (CATEGORY.OD_STATUS.equals(categoryCode)) {
          for (CatItemDTO catItemDTO : catItemDTOS) {
            mapResults.put(String.valueOf(catItemDTO.getItemValue()), catItemDTO.getItemName());
          }
        } else {
          for (CatItemDTO catItemDTO : catItemDTOS) {
            mapResults.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
          }
        }
      }
    }
    return mapResults;
  }

  public Map<String, String> getOdTypeMaster() {
    Map<String, String> odTypeMap = new HashMap<>();
    List<OdTypeEntity> odType = odChangeStatusRepository.findAllOdType();
    if (odType != null) {
      odType.forEach(c -> {
        odTypeMap.put(c.getOdTypeId().toString(), c.getOdTypeName());
      });
    }
    return odTypeMap;
  }

  public File getFileExport(List object, String title, String sheetName, int startRow,
      int rowCellTitle, int mergeTitleEndIndex, String fileNameTemplate, String fileNameOutput,
      String... params) throws Exception {
    Locale locale = Locale.forLanguageTag("vi");
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    List<ConfigFileExport> fileExports = new ArrayList<>();
    if (params.length > 0) {
      for (int i = 0; i < params.length; i++) {
        columnSheet1 = new ConfigHeaderExport(params[i], "LEFT", false, 0, 0, new String[]{}, null,
            "STRING");
        lstHeaderSheet1.add(columnSheet1);
      }
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        object
        , sheetName
        , title
        , ""
        , startRow
        , rowCellTitle
        , mergeTitleEndIndex
        , true
        , "language.odCfgBusiness"
        , lstHeaderSheet1
        , fieldSplit
        , ""
    );

    configfileExport.setLangKey(locale.getLanguage());
    CellConfigExport cellSheet;
    List<CellConfigExport> cellSheets = new ArrayList<>();
    cellSheet = new CellConfigExport(startRow, 0, 0, 0, "STT",
        "HEAD", "STRING");
    cellSheets.add(cellSheet);
    configfileExport.setLstCreatCell(cellSheets);
    fileExports.add(configfileExport);

    String fileTemplate = "templates" + File.separator
        + fileNameTemplate;
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOutput
        , fileExports
        , rootPath
        , null
    );

    return fileExport;
  }

  public String validateData(OdChangeStatusDTO odChangeStatusDTO) {

    if (odChangeStatusDTO.getOldStatus() == null) {
      return I18n.getValidation("odCfgBusiness.oldStatus.isRequired");
    }
    if (odChangeStatusDTO.getNewStatus() == null) {
      return I18n.getValidation("odCfgBusiness.newStatus.isRequired");
    }
    if (odChangeStatusDTO.getOdPriority() == null) {
      return I18n.getValidation("odCfgBusiness.odPriority.isRequired");
    }

    if (odChangeStatusDTO.getOdChangeStatusRoleDTO() == null
        || odChangeStatusDTO.getOdChangeStatusRoleDTO().size() < 1) {
      return I18n.getValidation("odCfgBusiness.role.isRequired");
    }

    if (odChangeStatusRepository.isExitOdChangeStatusDTO(odChangeStatusDTO)) {
      return I18n.getValidation("odCfgBusiness.data.dup");
    }

    if (odChangeStatusDTO.getCreateContent() != null
        && odChangeStatusDTO.getCreateContent().length() > 500) {
      return I18n.getValidation("odCfgBusiness.createContent.tooLong");
    }

    if (odChangeStatusDTO.getReceiveUserContent() != null
        && odChangeStatusDTO.getReceiveUserContent().length() > 500) {
      return I18n.getValidation("odCfgBusiness.receiveUserContent.tooLong");
    }
    if (odChangeStatusDTO.getReceiveUnitContent() != null
        && odChangeStatusDTO.getReceiveUnitContent().length() > 750) {
      return I18n.getValidation("odCfgBusiness.receiveUnitContent.tooLong");
    }
    if (odChangeStatusDTO.getNextAction() != null
        && odChangeStatusDTO.getNextAction().length() > 2000) {
      return I18n.getValidation("odCfgBusiness.nextAction.tooLong");
    }
    if (odChangeStatusDTO.getApproverContent() != null
        && odChangeStatusDTO.getApproverContent().length() > 500) {
      return I18n.getValidation("odCfgBusiness.approverContent.tooLong");
    }
    return "";
  }

  @Override
  public List<OdChangeStatusDTO> search(OdChangeStatusDTO odChangeStatusDTO) {
    return odChangeStatusRepository.search(odChangeStatusDTO);
  }

  @Override
  public OdChangeStatusDTO getOdChangeStatusDTOByParams(String... params) {
    return odChangeStatusRepository.getOdChangeStatusDTOByParams(params);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = OdChangeStatusDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("isDefaultName", "odTypeName", "oldStatusName", "newStatusName",
            "odPriorityName", "odCfgBusinessDTO", "odChangeStatusRoleDTO");
        for (String rmKey : rmKeys) {
          keys.remove(rmKey);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
