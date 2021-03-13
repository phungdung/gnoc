package com.viettel.gnoc.cr.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedNodesDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.repository.CrImpactedNodesRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class CrImpactedNodesBusinessImpl implements CrImpactedNodesBusiness {

  private int maxRecord = 3000;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  CrImpactedNodesRepository crImpactedNodesRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto actionImportAndGetNetworkNodeV2(MultipartFile multipartFile,
      String nationCode, int type, List<CrImpactedNodesDTO> lstImpactedNodes,
      List<CrAffectedNodesDTO> lstAffectedNodes) {

    ResultInSideDto resultInSideDto = new ResultInSideDto();
    Map<Long, String> mapImportResult = new HashMap<>();//danh sach IP + ket qua check
    Map<String, Long> mapRemoveIpDuplicate = new HashMap<>();//danh sach IP import da remove duplicate
    Map<String, Long> mapRemoveDeviceDuplicate = new HashMap<>();
    Map<Long, String> mapIp = new HashMap<>();
    Map<Long, String> mapDevice = new HashMap<>();
    boolean flagBlankFile = false;
    boolean flagOK = true;
    List<InfraDeviceDTO> lstResultOK = new ArrayList<>();
    try {
      resultInSideDto.setKey(RESULT.SUCCESS);

      String[] header = new String[]{"deviceCode", "ip", "resultImport"};
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
        File fileImp = new File(filePath);

        List<Object[]> lstHeader;

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 15, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 4,
            0, 15, 1000);
        if (lstHeader.size() == 0 || !validateImportedFile(lstHeader)) {
          resultInSideDto.setKey(Constants.RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        if (lstData != null) {
          mapIp = getDataFromImportedFile2(lstData,
              mapDevice); //put vao 2 map -> check tồn tại trong file
          if (mapIp != null && mapIp.values() != null && !mapIp.values().isEmpty()) {
            for (Iterator<Long> ir = mapIp.keySet().iterator(); ir.hasNext(); ) {
              Long id = ir.next();
              String code = mapIp.get(id);
              if (StringUtils.isStringNullOrEmpty(code)) {

              } else {
                flagBlankFile = true;
                if (mapRemoveIpDuplicate.get(code.trim()) != null) {
                  mapImportResult.put(id, I18n.getLanguage("import.ip.alreadyInfile"));
                  flagOK = false;
                } else {
                  mapRemoveIpDuplicate.put(code.trim().toLowerCase(), id);
                }
              }
            }
          }
          if (mapDevice != null && mapDevice.values() != null && !mapDevice.values().isEmpty()) {
            for (Iterator<Long> ir = mapDevice.keySet().iterator(); ir.hasNext(); ) {
              Long id = ir.next();
              String code = mapDevice.get(id);
              if (StringUtils.isStringNullOrEmpty(code)) {

              } else {
                flagBlankFile = true;
                if (mapRemoveDeviceDuplicate.get(code.trim()) != null) {
                  mapImportResult.put(id, I18n.getLanguage("import.ipdevice.alreadyInfile"));
                  flagOK = false;
                } else {
                  mapRemoveDeviceDuplicate.put(code.trim().toLowerCase(), id);
                }
              }
            }
          }

          if (!flagBlankFile) {
            resultInSideDto.setKey(Constants.CR_RETURN_MESSAGE.ERRORFORMAT);
            resultInSideDto.setMessage(I18n.getLanguage("file.blank.import.node"));
            return resultInSideDto;
          }

          List<String> listIp = new ArrayList<>();
          List<String> listDevice = new ArrayList<>();
          for (Iterator<String> ir = mapRemoveIpDuplicate.keySet().iterator(); ir.hasNext(); ) {
            listIp.add(ir.next());
          }
          for (Iterator<String> ir = mapRemoveDeviceDuplicate.keySet().iterator(); ir.hasNext(); ) {
            listDevice.add(ir.next());
          }

          List<InfraDeviceDTO> listIpFromDataBase = new ArrayList<>();
          if (listIp != null && !listIp.isEmpty()) {
            listIpFromDataBase = getListInfraDeviceIpV2(listIp, nationCode);
            if (!listIp.isEmpty() && (listIpFromDataBase == null || listIpFromDataBase
                .isEmpty())) {//toan bo danh sach import ko ton tai trong DB
              for (Iterator<String> ir = mapRemoveIpDuplicate.keySet().iterator();
                  ir.hasNext(); ) {
                String key = ir.next();
                Long index = mapRemoveIpDuplicate.get(key);
                mapImportResult.put(index,
                    I18n.getLanguage("import.node.doesnot.exists"));
                flagOK = false;
              }
            } else if (!listIp.isEmpty() && !listIpFromDataBase.isEmpty()) {
              List<String> lstNode = new ArrayList<>();//List IP node mang tu DB
              boolean checkDup = false;
              //Node mang tac dong
              if (lstImpactedNodes != null && !lstImpactedNodes.isEmpty()) {
                for (CrImpactedNodesDTO item : lstImpactedNodes) {
                  for (InfraDeviceDTO deviceDTO : listIpFromDataBase) {
                    if (StringUtils.isNotNullOrEmpty(item.getIp()) && !item.getIp()
                        .equals(deviceDTO.getIp())) {
                      lstNode.add(deviceDTO.getIp().trim().toLowerCase());
                    } else {
                      checkDup = true;
                    }
                  }
                }
              } else if (lstAffectedNodes != null && !lstAffectedNodes
                  .isEmpty()) {  //Node mang anh huong
                for (CrAffectedNodesDTO item : lstAffectedNodes) {
                  for (InfraDeviceDTO deviceDTO : listIpFromDataBase) {
                    if (StringUtils.isNotNullOrEmpty(item.getIp()) && !item.getIp()
                        .equals(deviceDTO.getIp())) {
                      lstNode.add(deviceDTO.getIp().trim().toLowerCase());
                    } else {
                      checkDup = true;
                    }
                  }
                }
              } else {
                for (InfraDeviceDTO deviceDTO : listIpFromDataBase) {
                  lstNode.add(deviceDTO.getIp().trim().toLowerCase());
                }
              }

              //check tung IP import so voi IP trong DB
              for (Iterator<String> ir = mapRemoveIpDuplicate.keySet().iterator(); ir.hasNext(); ) {
                String key = ir.next();
                Long index = mapRemoveIpDuplicate.get(key);
                //tuanpv14_chan danh sach IP sai dinh dang start
                if (!DataUtil.validateIP(key)) {
                  mapImportResult.put(index,
                      I18n.getLanguage("import.node.ipInvalid"));
                  flagOK = false;
                } //tuanpv14_chan danh sach IP sai dinh dang end
                else if (lstNode.contains(key.trim().toLowerCase())) {
                  mapImportResult.put(index, Constants.CR_RETURN_MESSAGE.OK);
                } else if (checkDup) {
                  mapImportResult.put(index,
                      I18n.getLanguage("import.duplicate"));
                  flagOK = false;
                } else {
                  mapImportResult.put(index,
                      I18n.getLanguage("import.node.doesnot.exists"));
                  flagOK = false;
                }
              }
            }
          }

          List<InfraDeviceDTO> listDeviceFromDataBase = new ArrayList<>();
//          List<CrImpactedNodesDTO> lstImpactNode = new ArrayList<>();
          if (listDevice != null && !listDevice.isEmpty()) {
            listDeviceFromDataBase = getListDeviceByListDevice(listDevice, nationCode);
            if (!listDevice.isEmpty() && (listDeviceFromDataBase == null || listDeviceFromDataBase
                .isEmpty())) {//toan bo danh sach import ko ton tai trong DB
              for (Iterator<String> ir = mapRemoveDeviceDuplicate.keySet().iterator();
                  ir.hasNext(); ) {
                String key = ir.next();
                Long index = mapRemoveDeviceDuplicate.get(key);
                mapImportResult.put(index,
                    I18n.getLanguage("import.device.doesnot.exists"));
                flagOK = false;
              }
            } else if (!listDevice.isEmpty() && !listDeviceFromDataBase.isEmpty()) {
              boolean checkDup = false;
              List<String> lstTemp = new ArrayList<>();//List IP node mang tu DB
              //Node mang tac dong
              if (lstImpactedNodes != null && !lstImpactedNodes.isEmpty()) {
                for (CrImpactedNodesDTO item : lstImpactedNodes) {
                  for (InfraDeviceDTO deviceDTO : listDeviceFromDataBase) {
                    if (StringUtils.isNotNullOrEmpty(item.getIp()) && !item.getIp()
                        .equals(deviceDTO.getIp())) {
                      lstTemp.add(deviceDTO.getDeviceCode().trim().toLowerCase());
                    } else {
                      checkDup = true;
                    }
                  }
                }
              } else if (lstAffectedNodes != null && !lstAffectedNodes
                  .isEmpty()) { // Node mang anh huong
                for (CrAffectedNodesDTO item : lstAffectedNodes) {
                  for (InfraDeviceDTO deviceDTO : listDeviceFromDataBase) {
                    if (StringUtils.isNotNullOrEmpty(item.getIp()) && !item.getIp()
                        .equals(deviceDTO.getIp())) {
                      lstTemp.add(deviceDTO.getDeviceCode().trim().toLowerCase());
                    } else {
                      checkDup = true;
                    }
                  }
                }
              } else {
                for (InfraDeviceDTO deviceDTO : listDeviceFromDataBase) {
                  lstTemp.add(deviceDTO.getDeviceCode().trim().toLowerCase());
                }
              }
              //check tung device import so voi device trong DB
              for (Iterator<String> ir = mapRemoveDeviceDuplicate.keySet().iterator();
                  ir.hasNext(); ) {
                String key = ir.next();
                Long index = mapRemoveDeviceDuplicate.get(key);

                if (lstTemp.contains(key.trim().toLowerCase())) {
                  mapImportResult.put(index, Constants.CR_RETURN_MESSAGE.OK);
                } else if (checkDup) {
                  mapImportResult.put(index,
                      I18n.getLanguage("import.duplicate"));
                  flagOK = false;
                } else {
                  mapImportResult.put(index,
                      I18n.getLanguage("import.device.doesnot.exists"));
                  flagOK = false;
                }
              }
            }
          }

          if (!flagOK) {
            resultInSideDto.setKey(Constants.CR_RETURN_MESSAGE.NOK);
            if (mapIp != null && mapIp.values() != null && !mapIp.values().isEmpty()) {
              for (Iterator<Long> ir = mapIp.keySet().iterator(); ir.hasNext(); ) {
                Long key = ir.next();
                String ori = mapIp.get(key);
                if (StringUtils.isStringNullOrEmpty(ori)) {
                  continue;
                }
                String resultImp = mapImportResult.get(key);
                InfraDeviceDTO cindto = new InfraDeviceDTO();
                cindto.setIp(ori);
                cindto.setResultImport(resultImp);
                lstResultOK.add(cindto);
              }
            }

            if (mapDevice != null && mapDevice.values() != null && !mapDevice.values().isEmpty()) {
              for (Iterator<Long> ir = mapDevice.keySet().iterator(); ir.hasNext(); ) {
                Long key = ir.next();
                String ori = mapDevice.get(key);
                if (StringUtils.isStringNullOrEmpty(ori)) {
                  continue;
                }
                String resultImp = mapImportResult.get(key);
                InfraDeviceDTO cindto = new InfraDeviceDTO();
                cindto.setDeviceCode(ori);
                cindto.setResultImport(resultImp);
                lstResultOK.add(cindto);
              }
            }

            resultInSideDto
                .setObject(lstResultOK);//Danh sach IP import + ket qua import -> ghi file ket qua

          } else {
            resultInSideDto.setKey(Constants.CR_RETURN_MESSAGE.OK);
            List<InfraDeviceDTO> resultListInside = new ArrayList();
            if (listDeviceFromDataBase != null && !listDeviceFromDataBase.isEmpty()) {
              resultListInside.addAll(listDeviceFromDataBase);

            }
            if (listIpFromDataBase != null && !listIpFromDataBase.isEmpty()) {
              resultListInside.addAll(listIpFromDataBase);
            }
            resultInSideDto.setObject(resultListInside);//Tat ca IP import deu OK
          }

          if (type == 0) {
            if (lstData != null && lstData.size() > maxRecord) {
              resultInSideDto.setMessage(I18n.getLanguage("cr.msg.node.over") + maxRecord);
            } else {
              if (Constants.CR_RETURN_MESSAGE.OK.equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(I18n.getLanguage("cr.msg.import.success"));
                if (resultInSideDto != null && resultInSideDto.getObject() != null) {
                  ResultInSideDto resultTxt = createFileTxt(
                      (List<InfraDeviceDTO>) resultInSideDto.getObject());
                  if (resultTxt.getKey().equals(RESULT.SUCCESS)) {
                    resultInSideDto.setFile(resultTxt.getFile());
                  } else {
                    throw new Exception(resultTxt.getMessage());
                  }
                }
              } else if (Constants.CR_RETURN_MESSAGE.ERRORFORMAT
                  .equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(resultInSideDto.getDescription());

              } else if (Constants.CR_RETURN_MESSAGE.NOK.equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(I18n.getLanguage("cr.msg.import.unsuccess"));

                if (resultInSideDto.getObject() != null) {
                  File fileExport = handleFileExport(
                      (List<CrImpactedNodesDTO>) resultInSideDto.getObject(), header, null,
                      Constants.RESULT_IMPORT);
                  resultInSideDto.setKey(RESULT.ERROR);
//                  resultInSideDto.setFile(fileExport);
                  resultInSideDto.setFilePath(fileExport == null ? null : fileExport.getPath());
                }
              } else if (Constants.CR_RETURN_MESSAGE.ERROR
                  .equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage("cr.msg.import.unsuccess");
              }
            }

          } else {
            if (resultInSideDto.getObject() != null && lstData.size() > maxRecord) {
              resultInSideDto.setMessage(I18n.getLanguage("cr.msg.node.over" + maxRecord));
            } else {
              if (Constants.CR_RETURN_MESSAGE.OK.equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(I18n.getLanguage("cr.msg.import.success"));
                if (resultInSideDto != null && resultInSideDto.getObject() != null) {
                  ResultInSideDto resultTxt = createFileTxt(
                      (List<InfraDeviceDTO>) resultInSideDto.getObject());
                  if (resultTxt.getKey().equals(RESULT.SUCCESS)) {
                    resultInSideDto.setFile(resultTxt.getFile());
                  }
                }
              } else if (Constants.CR_RETURN_MESSAGE.ERRORFORMAT
                  .equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(resultInSideDto.getDescription());
              } else if (Constants.CR_RETURN_MESSAGE.NOK.equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(I18n.getLanguage("cr.msg.import.unsuccess"));
                if (resultInSideDto.getObject() != null) {
                  File fileExport = handleFileExport(
                      (List<CrImpactedNodesDTO>) resultInSideDto.getObject(), header, null,
                      Constants.RESULT_IMPORT);
                  resultInSideDto.setKey(RESULT.ERROR);
//                  resultInSideDto.setFile(fileExport);
                  resultInSideDto.setFilePath(fileExport == null ? null : fileExport.getPath());
                }
              } else if (Constants.CR_RETURN_MESSAGE.ERROR
                  .equals(resultInSideDto.getKey())) {
                resultInSideDto.setMessage(I18n.getLanguage("cr.msg.import.unsuccess"));
              }
            }

          }

        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private ResultInSideDto createFileTxt(List<InfraDeviceDTO> lst) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    FileWriter fileWriter = null;
    BufferedWriter writer = null;
    try {
      if (lst != null && !lst.isEmpty()) {
        String filePathTxt =
            tempFolder + File.separator + Constants.CR_FILE_ATTACH.CR_IMPACTED_NODE_TXT + ".txt";

        File file = new File(filePathTxt);
        ObjectMapper mapper = new ObjectMapper();
        String contents = mapper.writeValueAsString(lst);
        if (file.exists()) {
          file.delete();
          file.createNewFile();
        }
        try {
          fileWriter = new FileWriter(file);
          writer = new BufferedWriter(fileWriter);
          if (writer != null) {
            writer.write(contents);
            writer.flush();
            writer.close();
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setFile(file);
        return resultInSideDto;
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (fileWriter != null) {
          fileWriter.close();
        }
      } catch (IOException e) {
        log.error(e.getMessage());
      }
    }
    return resultInSideDto;
  }

  private File handleFileExport(List<CrImpactedNodesDTO> listExport,
      String[] columnExport, String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName;
    String title;
    String fileNameOut;
    int startRow = 7;
    if (Constants.RESULT_IMPORT.equals(code)) {
      sheetName = I18n.getLanguage("infraDevice.import.sheetname");
      title = I18n.getLanguage("infraDevice.import.title");
      fileNameOut = I18n.getLanguage("infraDevice.import.fileNameOut");
    } else {
      sheetName = I18n.getLanguage("infraDevice.export.sheetname");
      title = I18n.getLanguage("infraDevice.export.title");
      fileNameOut = I18n.getLanguage("infraDevice.export.fileNameOut");
    }
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n
          .getLanguage("infraDevice.export.eportDate", date);
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        startRow,
        3,
        5,
        true,
        "language.crImpactNodes",
        lstHeaderSheet1,
        fieldSplit,
        ""
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("common.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }


  @Override
  public boolean validateImportedFile(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("import.nodeNetWork"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("crImpactNodes.nodeIp"))
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }

    return true;
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpByListIP(List<InfraDeviceDTO> listIp) {
    Map<String, String> map = new HashMap<>();
    List<InfraDeviceDTO> listTemp = new ArrayList<>();

    if (listIp != null && !listIp.isEmpty()) {
      for (InfraDeviceDTO item : listIp) {
        if (item.getIp() != null && map.get(item.getIp().trim()) == null) {
          map.put(item.getIp(), item.getDeviceCode());
          listTemp.add(item);
        }
      }
      if (!listTemp.isEmpty()) {
        listIp = listTemp;
      }
      return listIp;

    }
    return null;
  }

  public Map<Long, String> getDataFromImportedFile2(List<Object[]> lstData,
      Map<Long, String> mapDevice) {
    CrImpactedNodesDTO dtoImport = new CrImpactedNodesDTO();
    Map<Long, String> mapIp = new HashMap<>();
    if (lstData != null) {
      Long count = 0L;
      for (Object[] objData : lstData) {
        String cellDevice = "";
        String cellIp = "";
        if (objData[0] != null) {
          dtoImport.setDeviceCode(objData[0].toString() == null ? "" : objData[0].toString());
          dtoImport.setIp(objData[0].toString() == null ? "" : objData[0].toString());
          cellDevice = objData[0].toString();
        } else if (objData[1] != null) {
          dtoImport.setDeviceCode(objData[1].toString() == null ? "" : objData[1].toString());
          dtoImport.setIp(objData[1].toString() == null ? "" : objData[1].toString());
          cellIp = objData[1].toString();
        }
        if (!StringUtils.isStringNullOrEmpty(cellIp)) {
          mapIp.put(count++, cellIp);
        } else if (!StringUtils.isStringNullOrEmpty(cellDevice)) {
          mapDevice.put(count++, cellDevice);
        } else {
          mapIp.put(count++, "");
        }

      }

    }
    return mapIp;
  }


  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2(List<String> listIp, String nationCode) {
    return crImpactedNodesRepository.getListInfraDeviceIpV2(listIp, nationCode);
  }

  @Override
  public File exportData(List<CrImpactedNodesDTO> lstExport) throws Exception {

    String[] header = new String[]{"ip", "deviceCode", "deviceName", "deviceCodeOld", "nationCode"};

    return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()), "");
  }


  @Override
  public List<CrImpactedNodesDTO> getLisNodeOfCR(CrImpactedNodesDTO dto) {
    List<CrImpactedNodesDTO> lst = new ArrayList<>();
    try {
      Date crCreatedDate = null;
      Date earlierStartTime = null;
      Double offSet = userRepository.getOffsetFromUser(ticketProvider.getUserToken().getUserID());
      offSet = offSet * -1;

      if (dto.getCrCreatedDateStr() != null && !"".equalsIgnoreCase(dto.getCrCreatedDateStr())) {
        crCreatedDate = DateTimeUtils
            .convertToDate(dto.getCrCreatedDateStr(), "dd/MM/yyyy HH:mm:ss", offSet, false);
      }

//      if (dto.getEarlierStartTimeStr() != null && !"".equals(dto.getEarlierStartTimeStr())) {
//        earlierStartTime = DateTimeUtils
//            .convertToDate(dto.getEarlierStartTimeStr(), "dd/MM/yyyy HH:mm:ss", offSet, false);
//
//      }
//      if (crCreatedDate != null) {
//        if (earlierStartTime == null || crCreatedDate.compareTo(earlierStartTime) >= 0) {
//          earlierStartTime = new Date(crCreatedDate.getTime() + 30 * 24 * 60 * 60 * 1000);
//        }
//      }

      if (dto.getCrCreatedDateStr() != null && !dto.getCrCreatedDateStr().trim().isEmpty()) {
        crCreatedDate = DataUtil.getDate(dto.getCrCreatedDateStr(), "dd/MM/yyyy HH:mm:ss");
      }

      if ("1".equalsIgnoreCase(dto.getSaveType())) {
        lst = crImpactedNodesRepository
            .getImpactedNodesByCrIdV2(Long.valueOf(dto.getCrId()), crCreatedDate, earlierStartTime,
                dto.getNodeType());
      } else {
        lst = crImpactedNodesRepository
            .getImpactedNodesByCrId(Long.valueOf(dto.getCrId()), crCreatedDate, earlierStartTime,
                dto.getNodeType(), null, null, null);
      }
    } catch (Exception e) {
      Logger.getLogger(e.getMessage());
      log.error(e.getMessage(), e);
    }

    return lst;
  }

  @Override
  public List<InfraDeviceDTO> getListDeviceByListDevice(List listDevice,
      String nationCode) {
    List<InfraDeviceDTO> lstResult = new ArrayList<>();
    List tempDevices = new ArrayList<>();
    for (int i = 0; i < listDevice.size(); i++) {
      tempDevices.add(listDevice.get(i));
      if ((i != 0 && i % 500 == 0) || i == listDevice.size() - 1) {
        if (tempDevices != null && !tempDevices.isEmpty()) {
          List<InfraDeviceDTO> lstDevice = crImpactedNodesRepository
              .getListDeviceByListDevice(tempDevices, nationCode);
          if (lstDevice != null && !lstDevice.isEmpty()) {
            lstResult.addAll(lstDevice);
          }
          tempDevices.clear();
        }
      }
    }
    return lstResult;
  }

  @Override
  public List search(CrImpactedNodesDTO tForm, int start, int maxResult, String sortType,
      String sortField) {
//        return super.search(tForm, start, maxResult, sortType, sortField); //To change body of generated methods, choose Tools | Templates.
    Long crId = Long.valueOf(tForm.getCrId());
    Date startDate = null;
    try {
      String startDateStr = tForm.getInsertTime();
      if (startDateStr != null && !startDateStr.trim().isEmpty()) {
        startDate = DataUtil.getDate(startDateStr, "dd/MM/yyyy HH:mm:ss");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return crImpactedNodesRepository
        .getImpactedNodesByCrId(crId, startDate, null, tForm.getType(), null, null, null);
  }

  @Override
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(CrImpactedNodesDTO crImpactedNodesDTO) {
    return crImpactedNodesRepository.getListCrImpactedNodesDTO(crImpactedNodesDTO);
  }

  @Override
  public List<CrImpactedNodesDTO> getLisNodeOfCRForProxy(CrImpactedNodesDTO dto) {
    return getLisNodeOfCRForOutSide(Long.valueOf(dto.getCrId()), dto.getCrCreatedDateStr(), null,
        dto.getNodeType(), dto.getSaveType());
  }

  @Override
  public List<CrImpactedNodesDTO> getLisNodeOfCRForOutSide(Long crId, String crCreatedDateStr,
      String earlierStartTimeStr, String nodeType, String saveType) {
    try {
      Date crCreatedDate = null;
      if (StringUtils.isNotNullOrEmpty(crCreatedDateStr)) {
        crCreatedDate = DataUtil.getDate(crCreatedDateStr, "dd/MM/yyyy HH:mm:ss");
      }
      Date earlierStartTime = null;
      if ("1".equalsIgnoreCase(saveType)) {
        return crImpactedNodesRepository
            .getImpactedNodesByCrIdV2(crId, crCreatedDate, earlierStartTime, nodeType);
      } else {
        return crImpactedNodesRepository
            .getImpactedNodesByCrId(crId, crCreatedDate, earlierStartTime, nodeType, null, null,
                null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrImpactedNodesDTO> onSearch(CrImpactedNodesDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return crImpactedNodesRepository.onSearch(tDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public List<CrImpactedNodesDTO> getImpactedNodes(Date startDate, Date earlierStartTime,
      String type, String deviceCode, String deviceName, String ip) {
    if (startDate == null || earlierStartTime == null) {
      return null;
    }
    return crImpactedNodesRepository
        .getImpactedNodesByCrId(null, startDate, earlierStartTime, type, deviceCode, deviceName,
            ip);
  }
}
