package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.File;
import java.util.List;

/**
 * @author NamTN
 */
public interface CatItemBusiness {

  Datatable getItemMaster(String categoryCode, String system,
      String type,
      String idColName, String nameCol);

  List<CatItemDTO> getListCatItemDTOLE(String lan, String appliedSystem, String appliedBussiness,
      CatItemDTO catItemDTO, int rowStart, int maxRow, String sortType, String sortFieldList);

  CatItemDTO getCatItemById(Long id);

  List<CatItemDTO> getListItemByCategory(String categoryCode, String itemCode);

  List<CatItemDTO> getListCatItemDTO(CatItemDTO catItem);

  List<CatItemDTO> getListCatItemDTOByListCategory(List<String> lstCategory);

  List<CatItemDTO> getListCatItemDTOByListCategoryCode(List<String> lstCategory);

  List<CatItemDTO> getListItemByCategoryAndParent(String categoryCode, String parentId);

  List<CatItemDTO> getListCatItemDTOByListCategoryLE(String lan, String appliedSystem,
      String appliedBussiness, List<String> lstCategoryCode);

  List<CatItemDTO> getListCatItemByListParent(String lstParentItemId, String categoryId);

  List<CatItemDTO> getLstNetworkLevel(String typeId);

  void setLan(List<CatItemDTO> result);

  String countDayOff(String fromDate, String toDate, String nation);

  Datatable getListCellService(InfraCellServiceDetailDTO dto);

  Datatable getListCatItemSearch(CatItemDTO catItemDTO);

  ResultInSideDto deleteCatItem(Long itemId);

  ResultInSideDto insertCatItem(CatItemDTO catItemDTO);

  ResultInSideDto updateCatItem(CatItemDTO catItemDTO);

  File exportData(CatItemDTO catItemDTO) throws Exception;

  List<CatItemDTO> getListCatItemByListParentCBB(CatItemDTO catItemDTO);

  List<CatItemDTO> getListCatItemTransDTO(CatItemDTO catItemDTO);

  Datatable getItemMasterHasParent(String categoryCode, String system,
      String type,
      String idColName, String nameCol);

  Datatable getItemMasterOrderValueNumber(String categoryCode, String system,
      String type,
      String idColName, String nameCol);

  List<com.viettel.gnoc.ws.dto.CatItemDTO> getListCateItem(String category, String fromDate,
      String toDate);

  /**
   * @author tripm
   */
  List<com.viettel.gnoc.ws.dto.CatItemDTO> search(com.viettel.gnoc.ws.dto.CatItemDTO catItemDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<com.viettel.gnoc.ws.dto.CatItemDTO> translateListDTO(
      List<com.viettel.gnoc.ws.dto.CatItemDTO> lst, String locale);

  List<com.viettel.gnoc.ws.dto.CatItemDTO> getListActionByCategory(String category, Long serviceId,
      Long infraType);

  List<com.viettel.gnoc.ws.dto.WoFileTempDTO> searchWoFileTempDTO(
      com.viettel.gnoc.ws.dto.WoFileTempDTO woFileTempDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<InfraCellServiceDetailDTO> getListCellService(String cellCode, String cellType);

  List<InfraDeviceDTO> searchNonIp(InfraDeviceDTO infraDeviceDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
