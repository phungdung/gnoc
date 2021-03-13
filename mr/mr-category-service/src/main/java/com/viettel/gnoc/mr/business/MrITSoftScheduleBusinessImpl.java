package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class MrITSoftScheduleBusinessImpl implements MrITSoftScheduleBusiness {

  @Autowired
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Autowired
  MrITSoftProcedureBusiness mrITSoftProcedureBusiness;


  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Autowired
  MrITHisBusiness mrITHisBusiness;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrServiceProxy crServiceProxy;


  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  MaintenanceMngtBusiness maintenanceMngtBusiness;

  @Autowired
  MrServiceRepository mrServiceRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String EXPORT_MR_IT_SOFT_SCHEDULE = "EXPORT_MR_IT_SOFT_SCHEDULE";
  private final static String MR_IT_SOFT_SCHEDULE_RESULT_IMPORT = "MR_IT_SOFT_SCHEDULE_RESULT_IMPORT";
  private final static String MR_IT_HARD_SCHEDULE_RESULT_IMPORT = "MR_SCHEDULE_IT__HARD_RESULT_IMPORT";
  private final static String MR_SCHEDULE_IT_HARD_EXPORT = "MR_SCHEDULE_IT_HARD_EXPORT";
  HashMap<String, String> mapMrConfirm = new HashMap<>();
  Map<Long, MrITSoftScheduleDTO> mapDataImport = new HashMap<>();
  private static final String MR_TYPE_SOFT = "S";

  @Override
  public Datatable getListMrSchedule(MrITSoftScheduleDTO mrITSoftScheduleDTO) {
    return mrITSoftScheduleRepository.getListMrSchedule(mrITSoftScheduleDTO);
  }

  public ResultInSideDto onUpdateSoft(MrITSoftScheduleDTO mrITSoftScheduleDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
    dtoDevice.setObjectId(mrITSoftScheduleDTO.getObjectId());
    dtoDevice.setDeviceType(mrITSoftScheduleDTO.getDeviceType());
    dtoDevice = mrSynItSoftDevicesRepository.findMrDeviceByObjectId(dtoDevice);
    //Khi cập nhật từ Bảo dưỡng về Không bảo dưỡng
    if (dtoDevice != null) {
      if ("1".equals(dtoDevice.getMrSoft()) && !StringUtils
          .isStringNullOrEmpty(mrITSoftScheduleDTO.getMrConfirm())) {
        //lý do bảo dưỡng khác Nâng cấp
        List<MrScheduleITHisDTO> lstScheduleHis = new ArrayList<>();
        List<MrITSoftScheduleDTO> lstDeleteSchedule = new ArrayList<>();
        String lastUpdate = "";
        MrITSoftScheduleDTO mrScheduleITModel = getDetail(
            Long.valueOf(mrITSoftScheduleDTO.getScheduleId()));
        if (!"5".equals(mrITSoftScheduleDTO.getMrConfirm())) {
          ResultInSideDto validate = validate(mrITSoftScheduleDTO, mrScheduleITModel);
          if (!RESULT.SUCCESS.equals(validate.getKey())) {
            return validate;
          }
          List<MrScheduleITHisDTO> lst = mrITSoftScheduleRepository
              .getListScheduleMoveToHis(dtoDevice);
          if (lst != null && !lst.isEmpty()) {
            lastUpdate = lst.get(0).getMrDate();
            //move lich sang lich su
            for (MrScheduleITHisDTO mrScheduleITHisDTO : lst) {
              mrScheduleITHisDTO.setNote(I18n.getLanguage("mtItSoftSchedule.list.noteUpdate"));
              mrScheduleITHisDTO.setMrMode("S");
              mrScheduleITHisDTO.setTitle(mrITSoftScheduleDTO.getNote());
              mrScheduleITHisDTO.setRegion(mrITSoftScheduleDTO.getRegion());
              MrITSoftProcedureDTO procedureItDTO = mrITSoftProcedureBusiness
                  .getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId()));
              mrScheduleITHisDTO.setCrNumber(
                  genCrNumber(mrScheduleITHisDTO.getCrId(), procedureItDTO.getTypeCr().toString(),
                      procedureItDTO.getArrayActionName()));
              MrITSoftScheduleDTO schedule = new MrITSoftScheduleDTO();
              schedule.setScheduleId(String.valueOf(mrScheduleITHisDTO.getScheduleId()));
              lstDeleteSchedule.add(schedule);
              lstScheduleHis.add(mrScheduleITHisDTO);
            }
            resultInSideDto = mrITHisBusiness.insertList(lstScheduleHis);
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              return resultInSideDto;
            }
            //xoa lich hien tai
            if (!lstDeleteSchedule.isEmpty()) {
              resultInSideDto = deleteMrScheduleITSoftForBusiness(
                  Long.valueOf(lstDeleteSchedule.get(0).getScheduleId()));
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                return resultInSideDto;
              }
            }
          }
          //cap nhat thiet bi
          dtoDevice.setIsCompleteSoft("1");
          dtoDevice.setMrSoft("0");
          dtoDevice.setMrConfirmSoft(mrITSoftScheduleDTO.getMrConfirm());
          dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
          dtoDevice.setLastDateSoft(
              "".equals(lastUpdate) ? null : DateTimeUtils.convertStringToDate(lastUpdate));
          try {
            resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(dtoDevice);
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              return resultInSideDto;
            }
          } catch (Exception e) {
            throw new Exception(e.getCause().getMessage());
          }
          // end lý do bảo dưỡng khác Nâng cấp
        } else {
          // Khi li do bao duong = 5
          List<CrHisDTO> crHisDtos = mrScheduleTelRepository
              .checkExistCrId(mrITSoftScheduleDTO.getCrId(), mrScheduleITModel.getCrId());
          CrInsiteDTO crDTO =
              crServiceProxy.findCrByIdProxy(Long.valueOf(mrITSoftScheduleDTO.getCrId()));
          ResultInSideDto validateUpgrade = validateUpgrade(mrITSoftScheduleDTO, mrScheduleITModel,
              crDTO);
          if (!RESULT.SUCCESS.equals(validateUpgrade.getKey())) {
            return validateUpgrade;
          }
          //Nếu lý do bảo dưỡng = Nâng cấp, CR vừa điền có trạng thái Đóng
          if (!StringUtils.isStringNullOrEmpty(crDTO.getState())) {
            if ("9".equals(crDTO.getState())) {
              if (crHisDtos != null && crHisDtos.size() > 0) {
                //MR  khác null=>>set mã MR, CR của record tương ứng về null
                if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId())) {
                  mrScheduleITModel.setRegion(mrITSoftScheduleDTO.getRegion());
                  mrScheduleITModel.setNote(mrITSoftScheduleDTO.getNote());
                  mrScheduleITModel.setMrId(null);
                  mrScheduleITModel.setCrId(null);
                  mrScheduleITModel.setNextDateModify(mrITSoftScheduleDTO.getNextDateModify());
                  resultInSideDto = mrITSoftScheduleRepository
                      .updateMrSchedule(mrScheduleITModel);
                  if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    return resultInSideDto;
                  }
                  //update node tac dong, don vi xac nhan BO
                  dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
                  dtoDevice.setMrConfirmSoft(mrITSoftScheduleDTO.getMrConfirm());
                  resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(dtoDevice);
                  if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    return resultInSideDto;
                  }
                } else {
                  //Nếu lý do bảo dưỡng = Nâng cấp, CR vừa điền có trạng thái Đóng,MR = null
                  List<MrScheduleITHisDTO> lst = mrITSoftScheduleRepository
                      .getListScheduleMoveToHis(dtoDevice);
                  if (lst != null && !lst.isEmpty()) {
                    lastUpdate = lst.get(0).getMrDate();
                    //move lich sang lich su
                    for (MrScheduleITHisDTO mrScheduleITHisDTO : lst) {

                      mrScheduleITHisDTO
                          .setNote(I18n.getLanguage("mtItSoftSchedule.list.noteUpdate"));
                      mrScheduleITHisDTO.setMrMode("S");
                      mrScheduleITHisDTO.setMrId(null);
                      mrScheduleITHisDTO.setCrId(mrITSoftScheduleDTO.getCrId());
                      mrScheduleITHisDTO.setTitle(mrITSoftScheduleDTO.getNote());
                      mrScheduleITHisDTO.setRegion(mrITSoftScheduleDTO.getRegion());
                      MrITSoftProcedureDTO procedureItDTO = mrITSoftProcedureBusiness
                          .getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId()));
                      mrScheduleITHisDTO.setCrNumber(genCrNumber(mrScheduleITHisDTO.getCrId(),
                          procedureItDTO.getTypeCr().toString(),
                          procedureItDTO.getArrayActionName()));
                      MrITSoftScheduleDTO schedule = new MrITSoftScheduleDTO();
                      schedule.setScheduleId(String.valueOf(mrScheduleITHisDTO.getScheduleId()));
                      lstDeleteSchedule.add(schedule);

                      lstScheduleHis.add(mrScheduleITHisDTO);
                    }
                    resultInSideDto = mrITHisBusiness.insertList(lstScheduleHis);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                    //xoa lich hien tai
                    if (!lstDeleteSchedule.isEmpty()) {
                      resultInSideDto = deleteMrScheduleITSoftForBusiness(
                          Long.valueOf(lstDeleteSchedule.get(0).getScheduleId()));
                      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                        return resultInSideDto;
                      }
                    }
                  }
                  //cap nhat thiet bi
                  dtoDevice.setIsCompleteSoft("1");
                  dtoDevice.setMrSoft("1");
                  dtoDevice.setMrConfirmSoft(null);
                  dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
                  dtoDevice.setLastDateSoft(
                      "".equals(lastUpdate) ? null : DateTimeUtils.convertStringToDate(lastUpdate));
                  resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(dtoDevice);
                  if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    return resultInSideDto;
                  }
                  //Thời gian bắt đầu kết thúc MR = thời gian bắt đầu kết thúc CR lấy trong CR_HIS
                  if (!StringUtils.isStringNullOrEmpty(mrScheduleITModel.getMrId())) {
                    MrInsideDTO mrDTO = maintenanceMngtBusiness
                        .findById(Long.valueOf(mrScheduleITModel.getMrId()));
                    mrDTO.setEarliestTime(
                        DateTimeUtils.convertStringToDate(crHisDtos.get(0).getEarliestStartTime()));
                    mrDTO.setLastestTime(
                        DateTimeUtils.convertStringToDate(crHisDtos.get(0).getLatestEndTime()));
                    resultInSideDto = mrITSoftScheduleRepository.updateMrEarAndLastestTime(mrDTO);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                  }
                }
              }
              // end CR ở trạng thái đóng
            } else {
              // CR khác đóng
              MrITSoftScheduleDTO mrScheuleITUpdate = getDetail(
                  Long.valueOf(mrITSoftScheduleDTO.getScheduleId()));
              if (!StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId())) {
                mrScheuleITUpdate.setMrId(null);
                mrScheuleITUpdate.setCrId(null);
              } else {
                resultInSideDto.setKey(RESULT.ERROR);
                return resultInSideDto;
              }
              mrScheuleITUpdate.setRegion(mrITSoftScheduleDTO.getRegion());
              mrScheuleITUpdate.setNote(mrITSoftScheduleDTO.getNote());
              mrScheuleITUpdate
                  .setNextDateModify(mrITSoftScheduleDTO.getNextDateModify());
              resultInSideDto = mrITSoftScheduleRepository.updateMrSchedule(mrScheuleITUpdate);
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                return resultInSideDto;
              }
              //Cap nhat lai Ngay du kiến bảo dưỡng cho các lịch cùng mã nhóm
              if (StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId()) && !StringUtils
                  .isStringNullOrEmpty(mrScheduleITModel.getGroupCode())) {
                List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = mrITSoftScheduleRepository
                    .findListMrScheduleByGroupCode(mrScheduleITModel.getGroupCode());
                for (MrITSoftScheduleDTO mrScheduleITUpdate : lstMrScheduleITGroupCode) {
                  mrScheduleITUpdate
                      .setNextDateModify(mrITSoftScheduleDTO.getNextDateModify());
                  resultInSideDto = mrITSoftScheduleRepository
                      .updateMrSchedule(mrScheduleITUpdate);
                  if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    return resultInSideDto;
                  }
                }
              }
              //update node tac dong
              dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
              dtoDevice.setMrConfirmSoft(mrITSoftScheduleDTO.getMrConfirm());
              dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
              resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(dtoDevice);
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                return resultInSideDto;
              }
            }
          }
        }
        //end li do bao duong = 5
      } else {
        MrITSoftScheduleDTO mrScheduleITDTO = getDetail(
            Long.valueOf(mrITSoftScheduleDTO.getScheduleId()));
        if (StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId())) {
          mrScheduleITDTO.setMrId(null);
          mrScheduleITDTO.setCrId(null);
        }
        mrScheduleITDTO.setRegion(mrITSoftScheduleDTO.getRegion());
        mrScheduleITDTO.setNote(mrITSoftScheduleDTO.getNote());
        mrScheduleITDTO.setNextDateModify(mrITSoftScheduleDTO.getNextDateModify());
        mrITSoftScheduleRepository.updateMrSchedule(mrScheduleITDTO);
        //Cap nhat lai Ngay du kiến bảo dưỡng cho các lịch cùng mã nhóm
        if (StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId()) && !StringUtils
            .isStringNullOrEmpty(mrScheduleITDTO.getGroupCode())) {
          List<MrITSoftScheduleDTO> lstMrScheduleITGroupCode = mrITSoftScheduleRepository
              .findListMrScheduleByGroupCode(mrScheduleITDTO.getGroupCode());
          for (MrITSoftScheduleDTO mrScheduleITUpdate : lstMrScheduleITGroupCode) {
            mrScheduleITUpdate.setNextDateModify(mrITSoftScheduleDTO.getNextDateModify());
            mrITSoftScheduleRepository.updateMrSchedule(mrScheduleITUpdate);
          }
        }
        //update node tac dong
        dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
        dtoDevice.setMrConfirmSoft(mrITSoftScheduleDTO.getMrConfirm());
        dtoDevice.setNodeAffected(mrITSoftScheduleDTO.getNodeAffected());
        resultInSideDto = mrSynItSoftDevicesRepository.updateMrSynItDevice(dtoDevice);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  private ResultInSideDto validate(MrITSoftScheduleDTO dto, MrITSoftScheduleDTO dtoOld) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    try {
      // Neu khong nhap ngay
      if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
        resultInSideDto.setMessage(I18n.getValidation("mrSchedule.list.nextDateModify") + I18n
            .getValidation("common.required.input"));
        return resultInSideDto;
      } else {
        // Ngay truoc khi sua
        // Date oldDateModify = DateUtil.string2Date(nextDateModifyProperty);
        // Ngay sau khi sua
        Date newDateModify = DateUtil.string2Date(dto.getNextDateModify());

        // Neu nam sau khi sua < nam hien tai
        if (DateUtil.getYY(newDateModify) < DateUtil.getYY(new Date())) {
          resultInSideDto.setMessage(I18n.getValidation("mrSchedule.list.nextDateModifyCompare"));
          return resultInSideDto;
        } else if (DateUtil.getYY(newDateModify) == DateUtil.getYY(new Date())) {
          // Neu thang sau khi sua < thang hien tai
          if (DateUtil.getMonth(newDateModify) < DateUtil.getMonth(new Date())) {
            resultInSideDto.setMessage(I18n.getValidation("mrSchedule.list.nextDateModifyCompare"));
            return resultInSideDto;
          } else if (DateUtil.getMonth(newDateModify) == DateUtil.getMonth(new Date())) {
            // Neu ngay sau khi sua < ngay hien tai
            if (DateUtil.getDayInMonth(newDateModify) < DateUtil.getDayInMonth(new Date())) {
              resultInSideDto
                  .setMessage(I18n.getValidation("mrSchedule.list.nextDateModifyCompare"));
              return resultInSideDto;
            }
          }
        }
      }
      //validate MrId
      if (!StringUtils.isStringNullOrEmpty(dtoOld.getMrId()) && !StringUtils
          .isStringNullOrEmpty(dto.getMrId()) && !dto.getMrId().equals(dtoOld.getMrId())) {
        if (StringUtils.isStringNullOrEmpty(dto.getMrId())) {
          resultInSideDto.setMessage(I18n.getValidation("mrSchedule.list.mrIdInvalid"));
          return resultInSideDto;
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  private String genCrNumber(String crId, String sTypeCr, String sArrayActionName) {
    if (StringUtils.isNotNullOrEmpty(crId)) {
      String crTypeLong;
      if (sTypeCr == null) {
        crTypeLong = Constants.CR_TYPE.STANDARD.toString();
      } else {
        crTypeLong = sTypeCr;
      }
      String crType = Constants.CR_TYPE.NORMAL.toString().equalsIgnoreCase(crTypeLong) ? "NORMAL"
          : Constants.CR_TYPE.EMERGENCY.toString().equalsIgnoreCase(crTypeLong) ? "EMERGENCY"
              : Constants.CR_TYPE.STANDARD.toString().equalsIgnoreCase(crTypeLong) ? "STANDARD"
                  : "";

      String crNumber = "CR_"
          + crType + "_"
          + sArrayActionName + "_"
          + crId;
      return crNumber.toUpperCase(Locale.US);
    }
    return null;
  }

  @Override
  public MrITSoftScheduleDTO getDetail(Long scheduleId) {
    log.debug("Request to getDetail : {}", scheduleId);
    return mrITSoftScheduleRepository.getDetail(scheduleId);
  }

  @Override
  public List<MrITSoftScheduleDTO> getListRegionByMrSynItDevices(String country) {
    return mrITSoftScheduleRepository.getListRegionByMrSynItDevices(country);
  }

  @Override
  public ResultInSideDto deleteMrScheduleITSoft(Long mrScheduleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = getDetail(mrScheduleId);
    List<MrSynItDevicesDTO> lstDevice = new ArrayList<>();
    List<MrITSoftScheduleDTO> lstMrITSoftSchedule = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(mrITSoftScheduleDTO.getMrId()) && StringUtils
        .isStringNullOrEmpty(mrITSoftScheduleDTO.getCrId())) {
      lstMrITSoftSchedule.add(mrITSoftScheduleDTO);
      resultInSideDto = mrITSoftScheduleRepository.deleteListSchedule(lstMrITSoftSchedule);
      MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
      dtoDevice.setObjectId(mrITSoftScheduleDTO.getDeviceId());
      dtoDevice.setMarketCode(mrITSoftScheduleDTO.getMarketCode());
      dtoDevice.setDeviceType(mrITSoftScheduleDTO.getDeviceType());
      dtoDevice = mrSynItSoftDevicesRepository.findMrDeviceByObjectId(dtoDevice);
      dtoDevice.setIsCompleteSoft("1");
      lstDevice.add(dtoDevice);
      mrSynItSoftDevicesRepository.updateList(lstDevice);
      return resultInSideDto;
    } else {
      resultInSideDto.setMessage(I18n.getLanguage("mtItSoftSchedule.err.delete"));
    }
    return resultInSideDto;
  }

  private ResultInSideDto deleteMrScheduleITSoftForBusiness(Long mrScheduleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    MrITSoftScheduleDTO mrITSoftScheduleDTO = getDetail(mrScheduleId);
    List<MrSynItDevicesDTO> lstDevice = new ArrayList<>();
    List<MrITSoftScheduleDTO> lstMrITSoftSchedule = new ArrayList<>();
    lstMrITSoftSchedule.add(mrITSoftScheduleDTO);
    resultInSideDto = mrITSoftScheduleRepository.deleteListSchedule(lstMrITSoftSchedule);
    MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
    dtoDevice.setObjectId(mrITSoftScheduleDTO.getDeviceId());
    dtoDevice.setMarketCode(mrITSoftScheduleDTO.getMarketCode());
    dtoDevice.setDeviceType(mrITSoftScheduleDTO.getDeviceType());
    dtoDevice = mrSynItSoftDevicesRepository.findMrDeviceByObjectId(dtoDevice);
    dtoDevice.setIsCompleteSoft("1");
    lstDevice.add(dtoDevice);
    mrSynItSoftDevicesRepository.updateList(lstDevice);
    return resultInSideDto;
  }

  @Override
  public File exportData(MrITSoftScheduleDTO mrITSoftScheduleDTO) throws Exception {
    List<MrITSoftScheduleDTO> mrITSoftScheduleDTOList = mrITSoftScheduleRepository
        .getDataExport(mrITSoftScheduleDTO);
    if (mrITSoftScheduleDTOList != null && !mrITSoftScheduleDTOList.isEmpty()) {
      for (MrITSoftScheduleDTO dto : mrITSoftScheduleDTOList) {
        if (dto.getApproveStatus() != null) {
          dto.setApproveStatusName(I18n.getLanguage(
              "mtItSoftSchedule.approveStatus." + dto.getApproveStatus()));
        } else {
          dto.setApproveStatusName(I18n.getLanguage(
              "mtItSoftSchedule.approveStatus.null"));
        }
        if (StringUtils.isNotNullOrEmpty(dto.getCycleType())) {
          dto.setCycleType(I18n.getLanguage(
              "mtItSoftSchedule.cycleType." + dto.getCycleType().trim().toUpperCase()));
        }
        if (StringUtils.isNotNullOrEmpty(dto.getBdType())) {
          dto.setBdType(I18n.getLanguage(
              "mtItSoftSchedule.BdType." + dto.getBdType()));
        }
      }
    }
    return exportFileEx(mrITSoftScheduleDTOList, "export", "S");
  }

  private void setMrConfirm() {
    mapMrConfirm.clear();
    List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    if (lstMrConfigDTO != null && !lstMrConfigDTO.isEmpty()) {
      for (MrConfigDTO mrConfigDTO : lstMrConfigDTO) {
        mapMrConfirm
            .put(String.valueOf(mrConfigDTO.getConfigCode()), mrConfigDTO.getConfigName());
      }
    }
  }

  private String[] getHeaderSoft() {
    return new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.bdType"),
        I18n.getLanguage("mtItSoftSchedule.stationCode"),
        I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
        I18n.getLanguage("mtItSoftSchedule.groupCode"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.crId"),
        I18n.getLanguage("mtItSoftSchedule.wlgText"),
        I18n.getLanguage("mtItSoftSchedule.descriptionCr"),
        I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
        I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.ud"),
        I18n.getLanguage("mtItSoftSchedule.db"),
        I18n.getLanguage("mtItSoftSchedule.boUnit"),
        I18n.getLanguage("mtItSoftSchedule.approveStatus"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
        I18n.getLanguage("mtItSoftSchedule.logMop")
    };
  }

  @Override
  public File getTemplate(String type) throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header;
    //Tiêu đề đánh dấu *
    String[] headerStar;
    if ("H".equals(type)) {
      header = new String[]{
          I18n.getLanguage("common.STT"),
          I18n.getLanguage("mtItSoftSchedule.scheduleId"),
          I18n.getLanguage("mtItSoftSchedule.nationName"),
          I18n.getLanguage("mtItSoftSchedule.region"),
          I18n.getLanguage("mtItSoftSchedule.arrayCode"),
          I18n.getLanguage("mtItSoftSchedule.deviceType"),
          I18n.getLanguage("mtItSoftSchedule.deviceCode"),
          I18n.getLanguage("mtItSoftSchedule.deviceName"),
          I18n.getLanguage("mtItSoftSchedule.ipNode"),
          I18n.getLanguage("mtItSoftSchedule.vendor"),
          I18n.getLanguage("mtItSoftSchedule.bdType"),
          I18n.getLanguage("mtItSoftSchedule.stationCode"),
          I18n.getLanguage("mtItSoftSchedule.nodeAffected"),
          I18n.getLanguage("mtItSoftSchedule.groupCode"),
          I18n.getLanguage("mtItSoftSchedule.nextDate"),
          I18n.getLanguage("mtItSoftSchedule.lastDate"),
          I18n.getLanguage("mtItSoftSchedule.mrId"),
          I18n.getLanguage("mtItSoftSchedule.crId"),
          I18n.getLanguage("mtItSoftSchedule.wlgText"),
          I18n.getLanguage("mtItSoftSchedule.implementUnitName"),
          I18n.getLanguage("mtItSoftSchedule.checkingUnitName"),
          I18n.getLanguage("mtItSoftSchedule.cycle"),
          I18n.getLanguage("mtItSoftSchedule.cycleType"),
          I18n.getLanguage("mtItSoftSchedule.note"),
          I18n.getLanguage("mtItSoftSchedule.lastDateStr"),
          I18n.getLanguage("mtItSoftSchedule.nextDateStr"),
          I18n.getLanguage("mtItSoftSchedule.ud"),
          I18n.getLanguage("mtItSoftSchedule.boUnit"),
          I18n.getLanguage("mtItSoftSchedule.approveStatus"),
          I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
          I18n.getLanguage("mtItSoftSchedule.mrConfirmName"),
          I18n.getLanguage("mtItSoftSchedule.logMop")
      };

      headerStar = new String[]{
          I18n.getLanguage("mtItSoftSchedule.scheduleId"),
          I18n.getLanguage("mtItSoftSchedule.nextDateModify")
      };
    } else {
      header = getHeaderSoft();
      //Tiêu đề đánh dấu *
      headerStar = new String[]{
          I18n.getLanguage("mtItSoftSchedule.scheduleId"),
          I18n.getLanguage("mtItSoftSchedule.nextDateModify")
      };
    }

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketCodeStrColumn = listHeader
        .indexOf(I18n.getLanguage("mtItSoftSchedule.nationName"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mtItSoftSchedule.arrayCode"));
    int mrConfirmNameColumn = listHeader
        .indexOf(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tao tieu de
    Font xssFontTitle = workbook.createFont();
    xssFontTitle.setFontName(HSSFFont.FONT_ARIAL);
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    CellStyle styleTitle = workbook.createCellStyle();
    styleTitle.setAlignment(HorizontalAlignment.CENTER);
    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    styleTitle.setFont(xssFontTitle);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workbook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 3, 8));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(26.25f);
    Cell titleCell = titleRow.createCell(3);
    titleCell.setCellValue("H".equals(type) ? I18n.getLanguage("mtItSoftSchedule.title.hard")
        : I18n.getLanguage("mtItSoftSchedule.title.soft"));
    titleCell.setCellStyle(styleTitle);

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(7);
    Row headerRegion = sheetParam1.createRow(0);
    Row headerArray = sheetParam2.createRow(0);
    headerRow.setHeightInPoints(22.5f);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(cellStyleHeader);
      sheetOne.setColumnWidth(i, 7000);
    }
    Cell headerCellMarketCode = headerRegion.createCell(0);
    Cell headerCellRegion = headerRegion.createCell(1);
    XSSFRichTextString market = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.nationName"));
    XSSFRichTextString reg = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.region"));
    headerCellMarketCode.setCellValue(market);
    headerCellMarketCode.setCellStyle(style.get("header"));
    headerCellRegion.setCellValue(reg);
    headerCellRegion.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(0, 15000);
    sheetParam1.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellDevice = headerArray.createCell(1);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.arrayCode"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellDevice.setCellValue(device);
    headerCellDevice.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 8;
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : lstMarketCode) {
      excelWriterUtils
          .createCell(sheetParam, 1, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name nationName = workbook.createName();
    nationName.setNameName("nationName");
    nationName.setRefersToFormula("param!$B$2:$B$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "nationName");
    CellRangeAddressList nationNameCreate = new CellRangeAddressList(8, 65000,
        marketCodeStrColumn,
        marketCodeStrColumn);
    XSSFDataValidation dataValidationnAtionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint, nationNameCreate);
    dataValidationnAtionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationnAtionName);

    row = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      List<MrITSoftScheduleDTO> mrDeviceDTOList = mrITSoftScheduleRepository
          .getListRegionByMrSynItDevices(
              marketCode.getValueStr() != null ? String.valueOf(marketCode.getValueStr()) : null);
      excelWriterUtils
          .createCell(sheetParam1, 0, row, marketCode.getDisplayStr(), style.get("cell"));
      for (MrITSoftScheduleDTO mrDeviceDTO : mrDeviceDTOList) {
        excelWriterUtils
            .createCell(sheetParam1, 1, row++, mrDeviceDTO.getRegion(), style.get("cell"));
      }
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 8;
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lstArrayCode = (List<CatItemDTO>) dataArray.getData();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name arrayCode = workbook.createName();
    arrayCode.setNameName("arrayCode");
    arrayCode.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint arrayCodeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "arrayCode");
    CellRangeAddressList arrayCodeCreate = new CellRangeAddressList(8, 65000, arrayCodeStrColumn,
        arrayCodeStrColumn);
    XSSFDataValidation dataValidationArrayCode = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            arrayCodeConstraint, arrayCodeCreate);
    dataValidationArrayCode.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationArrayCode);

    row = 1;
    for (CatItemDTO catDto : lstArrayCode) {
      List<MrSynItDevicesDTO> lstDeviceType = mrSynItSoftDevicesRepository
          .getListDeviceTypeByArrayCode(catDto.getItemName());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, catDto.getItemName(), style.get("cell"));
      for (MrSynItDevicesDTO deviceType : lstDeviceType) {
        excelWriterUtils
            .createCell(sheetParam2, 1, row++, deviceType.getDeviceType(), style.get("cell"));
      }
    }
    excelWriterUtils
        .createCell(sheetParam2, 0, row++, null, style.get("cell"));
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 8;
    List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    for (MrConfigDTO dto : lstMrConfigDTO) {
      excelWriterUtils
          .createCell(sheetParam, 24, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name mrConfirmNameStr = workbook.createName();
    mrConfirmNameStr.setNameName("mrConfirmNameStr");
    mrConfirmNameStr.setRefersToFormula("param!$Y$2:$Y$" + row);
    XSSFDataValidationConstraint mrConfirmNameStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrConfirmNameStr");
    CellRangeAddressList mrConfirmNameStrCreate = new CellRangeAddressList(8, 65000,
        mrConfirmNameColumn,
        mrConfirmNameColumn);
    XSSFDataValidation dataValidationmrConfirmNameStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrConfirmNameStrConstraint, mrConfirmNameStrCreate);
    dataValidationmrConfirmNameStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationmrConfirmNameStr);

    //set tên trang excel
    if ("H".equals(type)) {
      workbook.setSheetName(0, I18n.getLanguage("mtItSoftSchedule.title.hard"));
    } else {
      workbook.setSheetName(0, I18n.getLanguage("mtItSoftSchedule.title.soft"));
    }
    workbook.setSheetName(2, I18n.getLanguage("mtItSoftSchedule.region"));
    workbook.setSheetName(3, I18n.getLanguage("mtItSoftSchedule.arrNet"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_SCHEDULE_IT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MrITSoftScheduleDTO> mrScheduleITDTOArrayList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("common.fileEmpty"));
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        File fileImport = new File(filePath);
        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            31,
            1000
        );

        //Kiem tra form header
        if (headerList == null || headerList.isEmpty() || !validateFileSoftFormat(headerList,
            getHeaderSoft())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("common.import.errorTemplate"));
          return resultInSideDto;
        }

        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFileNew(
            fileImport,
            0,
            8,
            0,
            31,
            1500
        );

        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(I18n.getLanguage("common.importing.maxrow"));
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
//          int row = 7;
          int index = 0;
          setMrConfirm();
          index = processDataImport(dataImportList, mrScheduleITDTOArrayList, MR_TYPE_SOFT);
          if (index == 0) {
//            UserToken userToken = ticketProvider.getUserToken();
            if (mrScheduleITDTOArrayList != null && !mrScheduleITDTOArrayList.isEmpty()) {
              for (MrITSoftScheduleDTO dto : mrScheduleITDTOArrayList) {
                MrITSoftScheduleDTO dtoDetail = getDetail(Long.valueOf(dto.getScheduleId()));
                dtoDetail.setRegion(dto.getRegion());
                dtoDetail.setNextDateModify(dto.getNextDateModify());
                dtoDetail.setNote(dto.getNote());
                dtoDetail.setMrConfirm(dto.getMrConfirm());
                dtoDetail.setMrConfirmName(dto.getMrConfirmName());
                dtoDetail.setNodeAffected(dto.getNodeAffected());
                dtoDetail.setProcedureName(dto.getProcedureName());
//                ResultInSideDto res = new ResultInSideDto();
                ResultInSideDto res = onUpdateSoft(dtoDetail);
                if (!RESULT.SUCCESS.equals(res.getKey())) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
                  return resultInSideDto;
                }
              }
            }

          } else {
            File fileExport = exportFileEx(mrScheduleITDTOArrayList,
                Constants.RESULT_IMPORT, MR_TYPE_SOFT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(I18n.getLanguage("common.searh.nodata"));
          File fileExport = exportFileEx(mrScheduleITDTOArrayList, Constants.RESULT_IMPORT,
              MR_TYPE_SOFT);
          resultInSideDto.setFilePath(fileExport.getPath());
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }


  Map<Long, MrITSoftScheduleDTO> getMapFromList(List<String> idSearch) {
    Map<Long, MrITSoftScheduleDTO> mapData = new HashMap<>();
    try {
      List<String> tempIds = new ArrayList<>();
      for (int i = 0; i < idSearch.size(); i++) {
        tempIds.add(idSearch.get(i));
        if ((i != 0 && i % 500 == 0) || i == idSearch.size() - 1) {
          List<MrITSoftScheduleDTO> results = mrITSoftScheduleRepository
              .getListMrSheduleITByIdForImport(tempIds);
          if (results != null && !results.isEmpty()) {
            results.forEach(item -> {
              if (StringUtils.isNotNullOrEmpty(item.getScheduleId())) {
                mapData.put(Long.valueOf(item.getScheduleId()), item);
              }
            });
          }
          tempIds.clear();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    return mapData;
  }

  private int processDataImport(List<Object[]> dataImportList,
      List<MrITSoftScheduleDTO> mrScheduleITDTOArrayList, String type) {
    mapDataImport.clear();
    List<String> idSearch = new ArrayList<>();
    int index = 0;
    for (Object[] obj : dataImportList) {
      if (!StringUtils.isStringNullOrEmpty(obj[1]) && DataUtil.isNumber(obj[1].toString().trim())) {
        idSearch.add(obj[1].toString().trim());
      }
    }
    //Lay danh sach lich theo id
    if (!idSearch.isEmpty() && MR_TYPE_SOFT.equals(type)) {
      mapDataImport = getMapFromList(idSearch);
    }
//    if (!idSearch.isEmpty() && MR_TYPE_HARD.equals(type)) {
//      mapDataImport = getMapFromListHard(idSearch);
//    }
    for (Object[] obj : dataImportList) {
      MrITSoftScheduleDTO mrScheduleITDTO = new MrITSoftScheduleDTO();
      int column = 0;
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.invalid"));
          mrScheduleITDTO.setScheduleId(obj[column].toString().trim());
        } else {
          mrScheduleITDTO.setScheduleId(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNationName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setRegion(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setArrayCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setIpNode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setVendor(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setBdType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setStationCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNodeAffected(obj[column].toString().trim());
      }

      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setGroupCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNextDate(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setLastDate(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        String checkDate = DataUtil.validateDateTimeDdMmYyyy(obj[column].toString().trim());
        if (StringUtils.isStringNullOrEmpty(checkDate)) {
          try {
            mrScheduleITDTO.setNextDateModify(obj[column].toString().trim());
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            mrScheduleITDTO.setNextDateModify(obj[column].toString().trim());
            mrScheduleITDTO
                .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.invalid"));
          }
        } else {
          mrScheduleITDTO.setNextDateModify(obj[column].toString().trim());
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.invalid"));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.mrId.invalid"));
          mrScheduleITDTO.setMrId(obj[column].toString().trim());
        } else {
          mrScheduleITDTO.setMrId(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.crId.invalid"));
          mrScheduleITDTO.setCrId(obj[column].toString().trim());
        } else {
          mrScheduleITDTO.setCrId(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setWlgText(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDescriptionCr(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setImplementUnitName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCheckingUnitName(obj[column].toString().trim());
      }

      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCycle(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCycleType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNote(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setUd(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDb(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setBoUnitName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setApproveStatus(obj[column].toString().trim());
      }

      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setMrConfirmName(obj[column].toString().trim());
        for (Map.Entry<String, String> item : mapMrConfirm.entrySet()) {
          if (mrScheduleITDTO.getMrConfirmName().equals(item.getValue())) {
            mrScheduleITDTO.setMrConfirm(item.getKey());
            break;
          }
        }
      }
      MrITSoftScheduleDTO mrScheduleITDTOTmp = validateImportInfo(
          mrScheduleITDTO, mrScheduleITDTOArrayList, type);

      if (mrScheduleITDTOTmp.getResultImport() == null) {
        mrScheduleITDTOTmp
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.result.import"));
        mrScheduleITDTOArrayList.add(mrScheduleITDTOTmp);
      } else {
        mrScheduleITDTOArrayList.add(mrScheduleITDTO);
        index++;
      }
    }
    return index;
  }

  private ResultInSideDto validateUpgrade(MrITSoftScheduleDTO dto, MrITSoftScheduleDTO dtoOld,
      CrInsiteDTO crDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    List<CrImpactedNodesDTO> lstImpact = new ArrayList<>();
    String crId = dto.getCrId();
    if (!StringUtils.isLong(crId)) {
      resultInSideDto.setMessage(
          I18n.getLanguage("mrMngt.list.crId") + " " + I18n
              .getLanguage("mrHardContent.validate.cycle"));
      return resultInSideDto;
    }
    List<CrHisDTO> crHisDtos = mrScheduleTelRepository
        .checkExistCrId(dto.getCrId(), dtoOld.getCrId());
    if (crHisDtos == null || crHisDtos.size() < 1) {
      resultInSideDto.setMessage(
          I18n.getLanguage("mrScheduleTel.list.crId") + " " + I18n.getLanguage("common.incorrect"));
      return resultInSideDto;
    }

    try {
      if (crDTO != null) {
        CrImpactedNodesDTO crImpactedNodesDTO = new CrImpactedNodesDTO();
        crImpactedNodesDTO.setCrId(crId);
        crImpactedNodesDTO.setCrCreatedDateStr(
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(crDTO.getCreatedDate()));
        crImpactedNodesDTO.setEarlierStartTimeStr(
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(crDTO.getEarliestStartTime()));
        crImpactedNodesDTO.setNodeType(Constants.CR_NODE_TYPE.EFFECT);
        crImpactedNodesDTO.setSaveType(crDTO.getNodeSavingMode());
        lstImpact = crServiceProxy.getLisNodeOfCRForProxy(crImpactedNodesDTO);
      } else {
        resultInSideDto.setMessage(
            I18n.getLanguage("mrScheduleTel.list.crId") + " " + I18n.getLanguage("common.notnull"));
        return resultInSideDto;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setMessage(
          I18n.getLanguage("mrScheduleTel.list.crId") + " " + I18n.getLanguage("common.incorrect"));
      return resultInSideDto;
    }
    //duongnt edit start
    if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.list.nextDateModify.null"));
      return resultInSideDto;
    } else {
      try {
        Date newDateModify = DateUtil.string2Date(dto.getNextDateModify());
        if (DateUtil.getYY(newDateModify) < DateUtil.getYY(new Date())) {
          resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
          return resultInSideDto;
        } else if (DateUtil.getYY(newDateModify) == DateUtil.getYY(new Date())) {
          // Neu thang sau khi sua < thang hien tai
          if (DateUtil.getMonth(newDateModify) < DateUtil.getMonth(new Date())) {
            resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
            return resultInSideDto;
          } else if (DateUtil.getMonth(newDateModify) == DateUtil.getMonth(new Date())) {
            // Neu ngay sau khi sua < ngay hien tai
            if (DateUtil.getDayInMonth(newDateModify) < DateUtil.getDayInMonth(new Date())) {
              resultInSideDto
                  .setMessage(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
              return resultInSideDto;
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
        return resultInSideDto;
      }

    }
    CrImpactedNodesDTO dtoImpact = new CrImpactedNodesDTO();
    if (lstImpact != null && !lstImpact.isEmpty()) {
      if (lstImpact != null && !lstImpact.isEmpty()) {
        dtoImpact = lstImpact.get(0);
      }
    }
    if (!StringUtils.isStringNullOrEmpty(dtoOld.getMrId()) && !StringUtils
        .isStringNullOrEmpty(dto.getMrId()) && !dto.getMrId().equals(dtoOld.getMrId())) {
      if (StringUtils.isStringNullOrEmpty(dto.getMrId())) {
        resultInSideDto.setMessage(I18n.getValidation("mrSchedule.list.mrIdInvalid"));
        return resultInSideDto;
      }
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getMrId())) {
      MrInsideDTO mrDTO = mrServiceRepository.findMrById(Long.valueOf(dto.getMrId()));
      if (mrDTO == null) {
        resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.mrId.notFound"));
        return resultInSideDto;
      }
    }

    try {
      String newDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
      Date nextDate = DateUtil.string2Date(dto.getNextDateModify());
      Date checkDate = DateUtil.string2Date(newDate);

      if (DateUtil.compareDateTime(nextDate, checkDate) == -1) {
        resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.nextDateModify.small"));
        return resultInSideDto;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    try {
      boolean checkDeviceCode = false;
      if (crDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(dtoImpact)) {
          if (crDTO.getLatestStartTime() != null) {
            if (dto.getDeviceCode() != null) {
              Date lastestStartTime = crDTO.getLatestStartTime();
              Date lastDate = DateUtil.string2Date(dto.getLastDate());
              Long checkDate = lastestStartTime.getTime() - lastDate
                  .getTime(); // Ngay ket thuc CR - Ngay BD gan nhat ( so ngay con lai)
              int days = (int) (checkDate / (1000 * 60 * 60 * 24));
              if (dto.getCycleType().equals("D")) {
                Long cycleDays = Long.parseLong(dto.getCycle());
                if (days > cycleDays) { // DK ngay phai nho hon hoac bang chu
                  resultInSideDto
                      .setMessage(I18n.getValidation("mrScheduleTel.lastestStartTime.small.cycle"));
                  return resultInSideDto;
                }
              } else if (dto.getCycleType().equals("M")) {
                Integer cycleMonths = Integer.valueOf(dto.getCycle());
                int daysCycle = cycleMonths * 30;
                if (days > daysCycle) {// DK ngay phai nho hon hoac bang chu
                  resultInSideDto
                      .setMessage(I18n.getValidation("mrScheduleTel.lastestStartTime.small.cycle"));
                  return resultInSideDto;
                }
              }
              if (lstImpact != null) {
                for (CrImpactedNodesDTO item : lstImpact) {
                  if (dto.getDeviceCode().equals(item.getDeviceCode())) {
                    checkDeviceCode = true;
                    break;
                  }
                }
              }
              if (!checkDeviceCode) {
                resultInSideDto
                    .setMessage(I18n.getValidation("mrScheduleTel.deviceCode.and.NetworkNodeId"));
                return resultInSideDto;
              }
            } else {
              resultInSideDto
                  .setMessage(I18n.getValidation("mrScheduleTel.deviceCode.and.NetworkNodeId"));
              return resultInSideDto;
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  boolean validateFileSoftFormat(List<Object[]> headerList, String[] headerConfig) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }

    if (objects.length != headerConfig.length) {
      return false;
    }

    try {
      for (int i = 1; i < headerConfig.length; i++) {
        if (!headerConfig[i].trim()
            .equalsIgnoreCase(objects[i].toString().trim().replace("*", ""))) {
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  public File exportFileEx(List<MrITSoftScheduleDTO> lstData, String key, String type)
      throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = I18n
        .getLanguage("mtItSoftSchedule.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if ("H".equals(type)) {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("mtItSoftSchedule.import.title");
        fileNameOut = MR_IT_HARD_SCHEDULE_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode",
            "vendor", "bdType", "stationCode", "nodeAffected", "groupCode", "nextDate",
            "lastDate",
            "mrId", "crId", "wlgText", "descriptionCr", "implementUnitName",
            "checkingUnitName",
            "cycle", "cycleType", "note", "ud", "boUnit", "approveStatus", "nextDateModify",
            "mrConfirmName", "resultImport");
      } else {
        title = I18n.getLanguage("mtItSoftSchedule.export.title");
        fileNameOut = MR_SCHEDULE_IT_HARD_EXPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode",
            "vendor", "bdType", "stationCode", "nodeAffected", "groupCode", "nextDate",
            "lastDate",
            "mrId", "crId", "wlgText", "descriptionCr", "implementUnitName",
            "checkingUnitName",
            "cycle", "cycleType", "note", "ud", "boUnit", "approveStatus", "nextDateModify",
            "mrConfirmName");
      }
    } else {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("mtItSoftSchedule.import.title.soft");
        fileNameOut = MR_IT_SOFT_SCHEDULE_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName", "region", "arrayCode",
            "deviceType", "deviceCode", "deviceName", "ipNode", "vendor", "bdType", "stationCode",
            "nodeAffected", "groupCode", "nextDate", "lastDate", "nextDateModify", "mrId", "crId",
            "wlgText", "descriptionCr", "implementUnitName", "checkingUnitName", "cycle",
            "cycleType", "note", "ud", "db", "boUnit", "approveStatus", "mrConfirmName", "logMop",
            "resultImport");
      } else {
        title = I18n.getLanguage("mtItSoftSchedule.export.title.soft");
        fileNameOut = EXPORT_MR_IT_SOFT_SCHEDULE;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName", "region", "arrayCode",
            "deviceType",
            "deviceCode", "deviceName", "ipNode", "vendor", "bdType", "stationCode", "nodeAffected",
            "groupCode", "nextDate", "lastDate", "nextDateModify", "mrCode", "crNumber", "wlgText",
            "descriptionCr",
            "implementUnitName", "checkingUnitName", "cycle", "cycleType", "note", "ud", "db",
            "boUnitName", "approveStatusName", "mrConfirmName", "logMop");
      }
    }
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        title,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.mtItSoftSchedule",
        lstHeaderSheet,
        filedSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private MrITSoftScheduleDTO validateImportInfo(MrITSoftScheduleDTO mrScheduleITDTO,
      List<MrITSoftScheduleDTO> list, String type) {

    if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getScheduleId())) {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notEmpty"));
      return mrScheduleITDTO;
    }
    if (MR_TYPE_SOFT.equals(type)) {
      if ("5".equals(mrScheduleITDTO.getMrConfirm())) {
        if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getCrId())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("import.mtItSoftSchedule.cr.isrequired"));
          return mrScheduleITDTO;
        }
        if (DataUtil.isNumber(mrScheduleITDTO.getCrId().trim())) {
          mrScheduleITDTO.setCrId(mrScheduleITDTO.getCrId().trim());
        } else {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.crId.invalid"));
          return mrScheduleITDTO;
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getNextDateModify())) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.notEmpty"));
      return mrScheduleITDTO;
    }
    if (DataUtil.isNumber(mrScheduleITDTO.getScheduleId())) {
      if (StringUtils
          .isStringNullOrEmpty(mapDataImport.get(Long.valueOf(mrScheduleITDTO.getScheduleId())))) {
        mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notFound"));
        return mrScheduleITDTO;
      }
    } else {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notFound"));
      return mrScheduleITDTO;
    }

    if (!StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getNextDateModify())) {
      if (DateUtil.getYY(DateTimeUtils.convertStringToDate(mrScheduleITDTO.getNextDateModify()))
          < DateUtil.getYY(new Date())) {
        mrScheduleITDTO
            .setResultImport(I18n.getValidation("mtItSoftSchedule.erro.nextDateModify.invalid"));
        return mrScheduleITDTO;
      } else if (
          DateUtil.getYY(DateTimeUtils.convertStringToDate(mrScheduleITDTO.getNextDateModify()))
              == DateUtil
              .getYY(new Date())) {
        if (DateUtil
            .getMonth(DateTimeUtils.convertStringToDate(mrScheduleITDTO.getNextDateModify()))
            < DateUtil
            .getMonth(new Date())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.erro.nextDateModify.invalid"));
          return mrScheduleITDTO;
        } else if (DateUtil
            .getMonth(DateTimeUtils.convertStringToDate(mrScheduleITDTO.getNextDateModify()))
            == DateUtil
            .getMonth(new Date())) {
          if (DateUtil
              .getDayInMonth(DateTimeUtils.convertStringToDate(mrScheduleITDTO.getNextDateModify()))
              < DateUtil
              .getDayInMonth(new Date())) {
            mrScheduleITDTO
                .setResultImport(
                    I18n.getValidation("mtItSoftSchedule.erro.nextDateModify.invalid"));
            return mrScheduleITDTO;
          }
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(mrScheduleITDTO.getMrConfirmName()) && StringUtils
        .isStringNullOrEmpty(mrScheduleITDTO.getMrConfirm())) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.mrConfirm.invalid"));
      return mrScheduleITDTO;
    }
    if (mrScheduleITDTO.getNote() != null
        && mrScheduleITDTO.getNote().length() > 1000) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.mrComment.tooLong"));
      return mrScheduleITDTO;
    }

    MrITSoftScheduleDTO objUpdate = mapDataImport
        .get(Long.valueOf(mrScheduleITDTO.getScheduleId()));
    //dung edit 09/03/2020 không cho phép cập nhật khi đã có MrId
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId())) {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.mrId.removeConfirm"));
      return mrScheduleITDTO;
    }
    if (StringUtils.isStringNullOrEmpty(objUpdate.getMrId()) || StringUtils
        .isStringNullOrEmpty(objUpdate.getCrId())) {
    } else {
      if (!objUpdate.getNextDateModify().equals(mrScheduleITDTO.getNextDateModify())) {
        mrScheduleITDTO
            .setResultImport(I18n.getValidation("mtItSoftSchedule.import.nextDateModify.WO"));
        return mrScheduleITDTO;
      }
    }
    mrScheduleITDTO = validateDuplicate(mrScheduleITDTO, list);
    return mrScheduleITDTO;
  }

  public MrITSoftScheduleDTO validateDuplicate(MrITSoftScheduleDTO dto,
      List<MrITSoftScheduleDTO> lstoExportDTOS) {
    if (lstoExportDTOS != null && !lstoExportDTOS.isEmpty()) {
      for (int i = 0; i < lstoExportDTOS.size(); i++) {
        MrITSoftScheduleDTO dtoCheck = lstoExportDTOS.get(i);
        if (dto.getScheduleId().equals(dtoCheck.getScheduleId())) {
          dto.setResultImport(I18n.getValidation("MrMaterialDTO.unique.file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
    }
    return dto;
  }

}
