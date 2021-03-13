package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CategoryDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface CatItemRepository {

  List<CatItemDTO> getListItemByCategory(String categoryCode, String itemCode);

  List<WoCdGroupInsideDTO> getListWoCdGroupByCatitem(String categoryCode, String itemCode);

  Datatable getItemMaster(String categoryCode, String system, String business,
      String idColName, String nameCol);

  List<CatItemDTO> getDataItem(String categoryCode);

  CatItemEntity getItemByItemCode(String unitCode);

  List<CatItemDTO> getListCatItemDTOLE(String lan, String appliedSystem, String appliedBussiness,
      CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType, String sortFieldList);

  CatItemDTO getCatItemById(Long id);

  List<CatItemDTO> getListCatItemDTO(CatItemDTO catItem);

  List<CatItemDTO> getListItemByCategoryAndParent(String categoryCode, String parentId);

  List<CatItemDTO> getListCatItemDTOByListCategory(List<String> lstCategoryCode);

  List<CatItemDTO> getListCatItemDTOByListCategoryCode(List<String> lstCategoryCode);

  List<CatItemDTO> getListCatItemDTO(List<String> lstCategoryCode, String status);

  List<CatItemDTO> getListCatItemByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(CfgMapNetLevelIncTypeDTO dto);

  void setLan(List<CatItemDTO> result);

  String countDayOff(String resultOld, String fromDate, String toDate, String nation);

  Datatable getListCellService(InfraCellServiceDetailDTO dto);

  List<CatItemDTO> getListCatItemByItemCode(String itemCode);

  Datatable getListCatItemSearch(CatItemDTO catItemDTO);

  List<CatItemDTO> getListCatItemExport(CatItemDTO catItemDTO);

  ResultInSideDto deleteCatItem(Long itemId);

  ResultInSideDto insertCatItem(CatItemDTO catItemDTO);

  List<CategoryDTO> getListCategoryByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  List<CatItemDTO> getListCatItemTransDTO(CatItemDTO catItemDTO);

  Datatable getItemMasterHasParent(String categoryCode, String system,
      String business,
      String idColName, String nameCol);

  Datatable getItemMasterOrderValueNumber(String categoryCode, String system,
      String business,
      String idColName, String nameCol);

  List<com.viettel.gnoc.ws.dto.CatItemDTO> getListCateItem(String category, String fromDate,
      String toDate);

  /**
   * @author tripm
   */
  List<CatItemDTO> getListCatItemDTOSearch(
      com.viettel.gnoc.ws.dto.CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<com.viettel.gnoc.ws.dto.CatItemDTO> getListActionByCategory(String categoryCode,
      Long serviceId, Long infraType);

  List<WoFileTempDTO> searchWoFileTempDTO(
      com.viettel.gnoc.ws.dto.WoFileTempDTO woFileTempDTO, int rowStart, int maxRow,
      String sortType,
      String sortFieldList);

  List<InfraCellServiceDetailDTO> getListCellService(String cellCode, String cellType);

  List<InfraDeviceDTO> searchNonIp(InfraDeviceDTO infraDeviceDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);
}
