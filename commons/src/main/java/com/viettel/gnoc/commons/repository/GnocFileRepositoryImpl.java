package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
@Transactional
public class GnocFileRepositoryImpl extends BaseRepository implements GnocFileRepository {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CommonRepository commonRepository;

  @Override
  public GnocFileEntity getGnocFileById(Long id) {
    return getEntityManager().find(GnocFileEntity.class, id);
  }

  @Override
  public GnocFileDto getGnocFileByDto(GnocFileDto gnocFileDto) {
    List<GnocFileEntity> gnocFileEntities = findByMultilParam(GnocFileEntity.class,
        "businessCode", gnocFileDto.getBusinessCode(),
        "businessId", gnocFileDto.getBusinessId());
    if (!gnocFileEntities.isEmpty()) {
      return gnocFileEntities.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<GnocFileDto> getListGnocFileByDto(GnocFileDto gnocFileDto) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-gnoc-file-dto");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(gnocFileDto.getFileType())) {
      sqlQuery = sqlQuery + " AND GF.FILE_TYPE = :p_fileType ";
      parameters.put("p_fileType", gnocFileDto.getFileType());
    }
    if (StringUtils.isNotNullOrEmpty(gnocFileDto.getTypeWs())) {
      sqlQuery = sqlQuery + " AND GF.TYPE_WS = :p_typeWs ";
      parameters.put("p_typeWs", gnocFileDto.getTypeWs());
    }
    if (gnocFileDto.getRequired() != null) {
      sqlQuery = sqlQuery + " AND GF.REQUIRED = :p_required ";
      parameters.put("p_required", gnocFileDto.getRequired());
    }
    if (gnocFileDto.getMappingId() != null) {
      sqlQuery = sqlQuery + " AND GF.MAPPING_ID = :p_mappingId ";
      parameters.put("p_mappingId", gnocFileDto.getMappingId());
    }
    if (gnocFileDto.getTemplateId() != null) {
      sqlQuery = sqlQuery + " AND GF.TEMPLATE_ID = :p_templateId ";
      parameters.put("p_templateId", gnocFileDto.getTemplateId());
    }
    parameters.put("p_businessCode", gnocFileDto.getBusinessCode());
    parameters.put("p_businessId", gnocFileDto.getBusinessId());
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }

  @Override
  public List<GnocFileDto> getListGnocFileForSR(GnocFileDto gnocFileDto) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-gnoc-file-dto-for-SR");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(gnocFileDto.getFileType())) {
      sqlQuery = sqlQuery + " AND sf.FILE_TYPE = :p_fileType ";
      parameters.put("p_fileType", gnocFileDto.getFileType());
    }
    if (StringUtils.isNotNullOrEmpty(gnocFileDto.getTypeWs())) {
      sqlQuery = sqlQuery + " AND sf.TYPE_WS = :p_typeWs ";
      parameters.put("p_typeWs", gnocFileDto.getTypeWs());
    }
    if (gnocFileDto.getRequired() != null) {
      sqlQuery = sqlQuery + " AND sf.REQUIRE_CREATE_SR = :p_required ";
      parameters.put("p_required", gnocFileDto.getRequired());
    }
    if (gnocFileDto.getMappingId() != null) {
      sqlQuery = sqlQuery + " AND GF.MAPPING_ID = :p_mappingId ";
      parameters.put("p_mappingId", gnocFileDto.getMappingId());
    }
    if (gnocFileDto.getTemplateId() != null) {
      sqlQuery = sqlQuery + " AND sf.TEMPLATE_ID = :p_templateId ";
      parameters.put("p_templateId", gnocFileDto.getTemplateId());
    }
    parameters.put("p_businessCode", gnocFileDto.getBusinessCode());
    parameters.put("p_businessId", gnocFileDto.getBusinessId());
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }

  @Override
  public ResultInSideDto saveListGnocFile(String businessCode, Long businessId,
      List<GnocFileDto> gnocFileDtos) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
      deleteByMultilParam(GnocFileEntity.class,
          "businessCode", businessCode,
          "businessId", businessId);
      for (GnocFileDto gnocFileDto : gnocFileDtos) {
        gnocFileDto.setBusinessCode(businessCode);
        gnocFileDto.setBusinessId(businessId);
        getEntityManager().merge(gnocFileDto.toEntity());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto saveListGnocFileNotDeleteAll(String businessCode, Long businessId,
      List<GnocFileDto> gnocFileDtos) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    for (GnocFileDto gnocFileDto : gnocFileDtos) {
      gnocFileDto.setBusinessCode(businessCode);
      gnocFileDto.setBusinessId(businessId);
      getEntityManager().merge(gnocFileDto.toEntity());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertGnocFile(GnocFileDto gnocFileDto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    GnocFileEntity gnocFileEntity = getEntityManager().merge(gnocFileDto.toEntity());
    resultInSideDTO.setId(gnocFileEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto updateGnocFile(GnocFileDto gnocFileDto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    resultInSideDTO.setId(gnocFileDto.getId());
    getEntityManager().merge(gnocFileDto.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteGnocFile(String businessCode, Long businessId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    deleteByMultilParam(GnocFileEntity.class,
        "businessCode", businessCode,
        "businessId", businessId);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListGnocFile(String businessCode, Long businessId,
      List<Long> mappingIds) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if (mappingIds != null && !mappingIds.isEmpty()) {
      for (Long mappingId : mappingIds) {
        deleteByMultilParam(GnocFileEntity.class,
            "businessCode", businessCode,
            "businessId", businessId, "mappingId", mappingId);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteGnocFileByMapping(String businessCode, Long mappingId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    deleteByMultilParam(GnocFileEntity.class,
        "businessCode", businessCode,
        "mappingId", mappingId);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteGnocFileByDto(GnocFileDto gnocFileDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    GnocFileEntity gnocFileEntity = getEntityManager()
        .find(GnocFileEntity.class, gnocFileDto.getId());
    if (gnocFileEntity != null) {
      getEntityManager().remove(gnocFileEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<GnocFileEntity> getLstGnocFileByDto(GnocFileDto gnocFileDto) {
    List<GnocFileEntity> gnocFileEntities = findByMultilParam(GnocFileEntity.class,
        "businessCode", gnocFileDto.getBusinessCode(),
        "businessId", gnocFileDto.getBusinessId());
    return gnocFileEntities;
  }

  @Override
  public List<GnocFileDto> findListGnocFile(GnocFileDto gnocFileDto) {
    Map<String, Object> parameters = new HashMap<>();
    try {
      String sql = "select * from common_gnoc.gnoc_file f where business_code IN (" +
          "SELECT cat.ITEM_CODE||:languageCode FROM COMMON_GNOC.CAT_ITEM cat WHERE CATEGORY_ID = " +
          "(SELECT ct.CATEGORY_ID FROM COMMON_GNOC.CATEGORY ct WHERE CATEGORY_CODE = 'COMMON_FILE') )";
      if (gnocFileDto.getLanguage().trim().equals("vi_VN")) {
        parameters.put("languageCode", "_VI");
      } else {
        parameters.put("languageCode", "_EN");
      }
      List<GnocFileDto> lstFile = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(GnocFileDto.class));
      String locale = gnocFileDto.getLanguage().trim().equals("vi_VN") ? "VI" : "EN";
      List<CatItemDTO> lstLink = commonRepository.getListCommonLink(locale);
      if (lstLink != null && !lstLink.isEmpty()) {
        for (CatItemDTO item : lstLink) {
          GnocFileDto gnocFile = new GnocFileDto();
          gnocFile.setFileName(item.getItemName());
          gnocFile.setPath(item.getItemValue());
          gnocFile.setFileTypeName("link");
          lstFile.add(gnocFile);
        }
      }
      if (lstFile != null && !lstFile.isEmpty()) {
        return lstFile;
      }
    } catch (Exception err) {
      log.error(err.getMessage(), err);
    }
    return null;
  }

  @Override
  public ResultInSideDto uploadFileCommon(List<MultipartFile> fileAttacks) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
      //  upload File Attachment
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (MultipartFile multipartFile : fileAttacks) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), new Date());
        String fileName = multipartFile.getOriginalFilename();
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDtos.add(gnocFileDto);
      }
      resultInSideDto = saveListGnocFileNotDeleteAll(Constants.GNOC_FILE_BUSSINESS.COMMON, null,
          gnocFileDtos);
      return resultInSideDto;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }
}
