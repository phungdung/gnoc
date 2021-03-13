package com.viettel.gnoc.cr.repository;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.GnocFileEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassProtector;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import com.viettel.gnoc.cr.model.CrFilesAttachEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrFilesAttachRepositoryImpl extends BaseRepository implements
    CrFilesAttachRepository {

  @Override
  public Datatable getListFilesSearchDataTable(CrFilesAttachInsiteDTO crFilesAttachDTO) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "get-list-CrFile-Search2");
    Map<String, Object> parameters = new HashMap<>();
//    parameters.put("processIn", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
//    parameters.put("processOut", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
    if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO)) {
      sql += " and a.cr_id = :crId";
      parameters.put("crId", crFilesAttachDTO.getCrId());
      if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO.getFileType())) {
        sql += " and a.file_type = :fileType";
        parameters.put("fileType", crFilesAttachDTO.getFileType());
      }
      if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO.getUserId())) {
        sql += " and a.user_id = :userId";
        parameters.put("userId", crFilesAttachDTO.getUserId());
      }
    }
    return getListDataTableBySqlQuery(sql.toString(), parameters, crFilesAttachDTO.getPage(),
        crFilesAttachDTO.getPageSize(),
        CrFilesAttachInsiteDTO.class, crFilesAttachDTO.getSortName(),
        crFilesAttachDTO.getSortType());
  }


  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesSearch(
      CrFilesAttachInsiteDTO crFilesAttachDTO) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_PROCESS_WO, "get-list-CrFile-Search");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("processIn", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
    parameters.put("processOut", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
    if (crFilesAttachDTO != null) {
      sql += " and a.cr_id = :crId";
      parameters.put("crId", crFilesAttachDTO.getCrId());
      if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO.getFileType())) {
        sql += " and a.file_type = :fileType";
        parameters.put("fileType", crFilesAttachDTO.getFileType());
      }
      if (!StringUtils.isStringNullOrEmpty(crFilesAttachDTO.getUserId())) {
        sql += " and a.user_id = :userId";
        parameters.put("userId", crFilesAttachDTO.getUserId());
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrFilesAttachInsiteDTO.class));

  }

  @Override
  public List<CrFileObjectInsite> getListTemplateFileByProcess(String crProcessId,
      String fileType) {
    String language = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_FILE_ATTACH, "get-list-template-file-byProcess");
    Map<String, Object> parameters = new HashMap<>();
//    parameters.put("crProcessId", crProcessId);
    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      String[] arrCrProcessId = crProcessId.split(",");
      if (arrCrProcessId.length > 1) {
        List<String> lstCrProcess = Arrays.asList(arrCrProcessId);
        sql += " and pro.cr_process_id in(:lst_cr) ";
        parameters.put("lst_cr", lstCrProcess);
      } else {
        sql += " and pro.cr_process_id = :crProcessId";
        parameters.put("crProcessId", crProcessId);
      }
    }
    if (!StringUtils.isStringNullOrEmpty(fileType)) {
      parameters.put("file_type", fileType);
    } else {
      parameters.put("file_type", fileType);
    }
    parameters.put("p_leeLocale", language);
    List<CrFileObjectInsite> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrFileObjectInsite.class));
    if ((lst != null && !lst.isEmpty()) || ("vi_VN".equalsIgnoreCase(language))) {
      return lst;
    }
    parameters.put("p_leeLocale", "vi_VN");
    lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrFileObjectInsite.class));
    return lst;
  }

  @Override
  public String deleteCrFilesAttachByCrIdAndType(String crId, String fileType) {
    try {
      if (StringUtils.isStringNullOrEmpty(crId) || StringUtils.isStringNullOrEmpty(fileType)) {
        return RESULT.FAIL;
      }
      deleteByMultilParam(CrFilesAttachEntity.class,
          "crId", crId,
          "fileType", fileType);
    } catch (Exception he) {
      log.error(he.getMessage(), he);
      return RESULT.FAIL;
    }
    return Constants.RESULT.SUCCESS;
  }

  @Override
  public String deleteListTempImportDataDTO(List<TempImportDataDTO> tempImportDataDTOs) {
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> parameters = new HashMap<>();
      sql.append(" delete temp_import_data a where a.tida_id in(-1 ");
      for (TempImportDataDTO dto : tempImportDataDTOs) {
        sql.append(",:tidaId");
        parameters.put("tidaId", dto.getTidaId());
      }
      sql.append(")");
      return "SUCCESS";
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "FAIL";
    }
  }

  @Override
  public String insertTempImportDataDTO(List<TempImportDataDTO> tempImportDataDTOs) {
    try {
      StringBuilder sql = new StringBuilder();

      sql.append(
          " insert into temp_import_data a(a.tida_id, a.temp_import_id, a.temp_import_col_id,");
      sql.append(
          " a.temp_import_value, a.cr_id, a.row_order) values (TEMP_IMPORT_DATA_SEQ.nextval,:tempImportId,:tempImportColId,:tempImportValue,:crId,:rowOrder)");
      Map<String, Object> parameters = new HashMap<>();
      for (TempImportDataDTO tempImportDataDTO : tempImportDataDTOs) {
        parameters.put("tempImportId", tempImportDataDTO.getTempImportId());
        parameters.put("tempImportColId",
            tempImportDataDTO.getTempImportColId() != null ? tempImportDataDTO.getTempImportColId()
                : "");
        parameters.put("tempImportValue",
            tempImportDataDTO.getTempImportValue() != null ? tempImportDataDTO.getTempImportValue()
                .trim() : "");
        parameters.put("crId", tempImportDataDTO.getCrId());
        parameters.put("rowOrder", tempImportDataDTO.getRowOrder());
        getNamedParameterJdbcTemplateNormal().update(sql.toString(), parameters);
        getEntityManager().flush();
      }
      return "SUCCESS";
    } catch (Exception he) {
      log.error(he.getMessage(), he);
      return "FAIL";
    }
  }

  @Override
  public List<CrFilesAttachDTO> getListFileImportByProcess(CrFilesAttachInsiteDTO dto) {
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> parameters = new HashMap<>();
      sql.append(" SELECT a.file_id fileId, "
          + "       a.file_name fileName, "
          + " a.time_attack timeAttack, "
//          + "       TO_CHAR(a.time_attack, 'dd/MM/yyyy HH24:mi:ss') timeAttack, "
          + "       a.user_id userId, "
          + "       a.file_type fileType, "
          + "       a.cr_id crId, "
          + "       a.file_path filePath, "
          + "       a.temp_import_id tempImportId, "
          + "       u.username userName "
          + "  FROM cr_files_attach a "
          + "  LEFT JOIN common_gnoc.users u on a.user_id = u.user_id"
          + " where a.file_type in (:fileTypeIn, :fileTypeOut) ");
      parameters.put("fileTypeIn", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
      parameters.put("fileTypeOut", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
      if (dto != null) {
        if (!StringUtils.isStringNullOrEmpty(dto.getCrId())) {
          sql.append(" and a.cr_id = :crId");
          parameters.put("crId", dto.getCrId());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getUserId())) {
          sql.append(" and a.user_id = :userId");
          parameters.put("userId", dto.getUserId());
        }
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), parameters,
              BeanPropertyRowMapper.newInstance(CrFilesAttachDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> actionGetUserByUserName(String userName) {
    if (StringUtils.isStringNullOrEmpty(userName)) {
      return null;
    }
    StringBuilder sql = new StringBuilder("");
    sql.append(
        " select user_id userId from common_gnoc.users us where lower(us.username) = :userName  ");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", userName);
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  @Override
  public void deleteListAttachByListId(List<String> fileIds, Long crId) {
    if (fileIds.isEmpty()) {
      return;
    }
    for (String fileId : fileIds) {
      CrFilesAttachEntity crFilesAttachEntity = getEntityManager()
          .find(CrFilesAttachEntity.class, Long.valueOf(fileId));
      if (crFilesAttachEntity != null) {
        getEntityManager().remove(crFilesAttachEntity);
      }
      deleteByMultilParam(GnocFileEntity.class,
          "businessCode", GNOC_FILE_BUSSINESS.CR,
          "businessId", crId,
          "mappingId", Long.valueOf(fileId));
    }
  }

  @Override
  public String deleteCrFilesAttach(Long id) {
    if (StringUtils.isStringNullOrEmpty(id)) {
      return RESULT.FAIL;
    }
    CrFilesAttachEntity crFilesAttachEntity = getEntityManager()
        .find(CrFilesAttachEntity.class, id);
    if (crFilesAttachEntity != null) {
      getEntityManager().remove(crFilesAttachEntity);
    }
    return RESULT.SUCCESS;
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesByCrId(String crId, boolean isImportByProcess) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_FILE_ATTACH, "get-listCr-fileBy-crId");
    Map<String, Object> parameters = new HashMap<>();
    if (!isImportByProcess) {
      sql += " where a.file_type <> :fileType and a.file_type <> :fileType2 ";
      parameters.put("fileType", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
      parameters.put("fileType2", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
    } else {
      sql += " where a.file_type in (:fileType, :fileType2)";
      parameters.put("fileType", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
      parameters.put("fileType2", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
    }
    if (!StringUtils.isStringNullOrEmpty(crId)) {
      sql += " and a.cr_id = :crId";
      parameters.put("crId", crId);
    }
    sql += " order by a.file_type ";
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CrFilesAttachInsiteDTO.class));
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesAttachDTO(
      CrFilesAttachInsiteDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(CrFilesAttachEntity.class, crFilesAttachDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public ResultInSideDto add(CrFilesAttachInsiteDTO crFilesAttachDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CrFilesAttachEntity crFilesAttachEntity = getEntityManager()
        .merge(crFilesAttachDTO.toEntity());
    resultInSideDTO.setId(crFilesAttachEntity.getFileId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertByModel(CrFilesAttachInsiteDTO crFilesAttachDTO, String colId) {
    ResultInSideDto result = new ResultInSideDto();
    insertByModel(crFilesAttachDTO.toEntity(), colId);
    return result;
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesAttachByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new CrFilesAttachEntity(), lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public String onUpdateIsRunMop(String crIdParent, List<String> lstDtCode, String crIdChild) {
    StringBuilder sql = new StringBuilder();
    try {
      Map<String, Object> params = new HashMap<>();
      sql.append(" update cr_files_attach set is_run = :crIdChild ");
      sql.append(" where cr_id = :cdId and DT_CODE in (:lstDtCode) ");
      params.put("crIdChild", crIdChild);
      params.put("cdId", crIdParent);
      params.put("lstDtCode", lstDtCode);
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      getEntityManager().flush();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return Constants.RESULT.SUCCESS;
  }

  @Override
  public List<TempImportDataDTO> getListTempImportDataDTO(TempImportDataDTO tempImportDataDTO) {
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-temp-import-data"));

      Map<String, Object> params = new HashMap<>();
      if (tempImportDataDTO.getTidaId() != null) {
        sql.append(" and a.tida_id= :tida_id ");
        params.put("tida_id", tempImportDataDTO.getTidaId());
      }
      if (StringUtils.isNotNullOrEmpty(tempImportDataDTO.getTempImportId())) {
        sql.append(" and a.temp_import_id= :tempImportId ");
        params.put("tempImportId", tempImportDataDTO.getTempImportId());
      }
      if (StringUtils.isNotNullOrEmpty(tempImportDataDTO.getTempImportColId())) {
        sql.append(" and a.temp_import_col_id= :tempImportColId ");
        params.put("tempImportColId", tempImportDataDTO.getTempImportColId());
      }
      if (StringUtils.isNotNullOrEmpty(tempImportDataDTO.getCrId())) {
        sql.append(" and a.cr_id= :cr_id ");
        params.put("cr_id", tempImportDataDTO.getCrId());
      }
      sql.append(" order by a.row_order");

      return getNamedParameterJdbcTemplate().query(sql.toString(), params,
          BeanPropertyRowMapper.newInstance(TempImportDataDTO.class));
    } catch (HibernateException he) {
      log.error(he.getMessage(), he);
      return null;
    } catch (Exception he) {
      log.error(he.getMessage(), he);
      return null;
    }
  }

  @Override
  public CrFilesAttachInsiteDTO findFileAttachById(Long id) {
    List<CrFilesAttachEntity> crFilesAttachEntities = findByMultilParam(CrFilesAttachEntity.class,
        "fileId", id);
    CrFilesAttachInsiteDTO crFilesAttachDTO = null;
    if (!crFilesAttachEntities.isEmpty()) {
      crFilesAttachDTO = crFilesAttachEntities.get(0).toDTO();
    }
    return crFilesAttachDTO;
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesToUpdateOrInsert( //luu y khi su dung ham nay
      List<CrFilesAttachInsiteDTO> lstUpload,
      boolean isImportByProcess) {
    List<String> lstOldFileId = new ArrayList<>();
    List<CrFilesAttachInsiteDTO> lstInsert = new ArrayList<>();
    if (lstUpload == null || lstUpload.isEmpty()) {
      return null;
    }
    //Lay CR ID
    String crId = lstUpload.get(0).getCrId().toString();
    //Lay danh sach file da dinh kem tu DB
    List<CrFilesAttachInsiteDTO> lstDB =
        getListCrFilesByCrId(crId, isImportByProcess);
    //Check file upload ton tai trong DB
    if (lstDB != null && lstDB.size() > 0) {
      //lay danh sach file lstUpload upload khong ton tai trong lstDB de upload
      for (CrFilesAttachInsiteDTO item : lstUpload) {
        if (!Iterables.any(lstDB, containNameAndType(item))) {
          lstInsert.add(item);
        }
      }
      //Xoa file attach cu.
      if (isImportByProcess) {//File import theo quy trinh -> xoa theo type
        //lay danh sach file da import tu truoc, cung type de xoa, import file moi
        for (CrFilesAttachInsiteDTO item : lstDB) {
          if (Iterables.any(lstUpload, containType(item))) {
            lstOldFileId.add(item.getFileId().toString());
          }
        }
      } else {//file attach -> xoa theo name+type
        //lay danh sach file trong lstDB nhung khong ton tai trong lstUpload de xoa
        for (CrFilesAttachInsiteDTO item : lstDB) {
          if (!Iterables.any(lstUpload,
              containNameAndType(item))) { //Tiennv comment bo trong code, cho giong con cu
            lstOldFileId.add(item.getFileId().toString());
          }
        }
      }
      deleteListAttachByListId(lstOldFileId, Long.valueOf(crId));
      return lstInsert;
    }
    return lstUpload;
  }

  @Override
  public List<CrFilesAttachInsiteDTO> getListCrFilesToUpdateOrInsertForList(
      List<CrFilesAttachInsiteDTO> lstUpload,
      boolean isImportByProcess, boolean isRemove) {
    List<String> lstOldFileId = new ArrayList<>();
    List<CrFilesAttachInsiteDTO> lstInsert = new ArrayList<>();
    if (lstUpload == null || lstUpload.isEmpty()) {
      return null;
    }
    //Lay CR ID
    String crId = lstUpload.get(0).getCrId().toString();
    //Lay danh sach file da dinh kem tu DB
    List<CrFilesAttachInsiteDTO> lstDB =
        getListCrFilesByCrId(crId, isImportByProcess);
    //Check file upload ton tai trong DB
    if (lstDB != null && lstDB.size() > 0) {
      //lay danh sach file lstUpload upload khong ton tai trong lstDB de upload
      for (CrFilesAttachInsiteDTO item : lstUpload) {
//        if (!Iterables.any(lstDB, containNameAndType(item))) {
        lstInsert.add(item);
//        }
      }
      //Xoa file attach cu.
      if (isImportByProcess) {//File import theo quy trinh -> xoa theo type
        //lay danh sach file da import tu truoc, cung type de xoa, import file moi
        for (CrFilesAttachInsiteDTO item : lstDB) {
          if (Iterables.any(lstUpload, containType(item))) {
            lstOldFileId.add(item.getFileId().toString());
          }
        }
      } else {//file attach -> xoa theo name+type
        //lay danh sach file trong lstDB nhung khong ton tai trong lstUpload de xoa
        for (CrFilesAttachInsiteDTO item : lstDB) {
          if (!Iterables.any(lstUpload, containNameAndType(item))) {
            lstOldFileId.add(item.getFileId().toString());
          }
        }
      }
      if (isRemove) {
        deleteListAttachByListId(lstOldFileId, Long.valueOf(crId));
      }
      return lstInsert;
    }
    return lstUpload;
  }

  Predicate<CrFilesAttachInsiteDTO> containNameAndType(CrFilesAttachInsiteDTO input) {
    return new Predicate<CrFilesAttachInsiteDTO>() {
      public boolean apply(CrFilesAttachInsiteDTO orgi) {
        if (StringUtils.isStringNullOrEmpty(orgi.getFileSize()) || "0".equals(orgi.getFileSize())
            || StringUtils.isStringNullOrEmpty(input.getFileSize()) || "0"
            .equals(input.getFileSize())) {
          return orgi.getFileName() != null && orgi.getFileType() != null
              && input.getFileName() != null && input.getFileType() != null
              && orgi.getFileName().trim().equals(input.getFileName().trim())
              && orgi.getFileType().trim().equals(input.getFileType().trim());
        } else {
          return orgi.getFileName() != null && orgi.getFileType() != null
              && input.getFileName() != null && input.getFileType() != null
              && orgi.getFileName().trim().equals(input.getFileName().trim())
              && orgi.getFileType().trim().equals(input.getFileType().trim())
              && orgi.getFileSize().equals(input.getFileSize());
        }
      }
    };
  }

  Predicate<CrFilesAttachInsiteDTO> containType(CrFilesAttachInsiteDTO input) {
    return new Predicate<CrFilesAttachInsiteDTO>() {
      public boolean apply(CrFilesAttachInsiteDTO orgi) {
        return orgi.getFileType() != null && input.getFileType() != null
            && orgi.getFileType().trim().equals(input.getFileType().trim());
      }
    };
  }

  @Override
  public List<CrFilesAttachDTO> search(CrFilesAttachDTO tDTO, int start, int maxResult,
      String sortType, String sortField) {
    return onSearchOutSideDTO(CrFilesAttachEntity.class, tDTO, start, maxResult, sortType,
        sortField);
  }

  @Override
  public List<CrFilesAttachResultDTO> getListFileImportByProcessOutSide(CrFilesAttachDTO dto) {
    try {
      StringBuffer sql = new StringBuffer();
      Map<String, Object> params = new HashMap<>();
      sql.append(SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_FILE_ATTACH, "get-list-file-import-byProcess"));
      params.put("fileTypeIn", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
      params.put("fileTypeOut", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_OUT);
      if (dto != null) {
        if (!StringUtils.isStringNullOrEmpty(dto.getCrId())) {
          sql.append(" and a.cr_id = :crId");
          params.put("crId", dto.getCrId());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getUserId())) {
          sql.append(" and a.user_id = :userId");
          params.put("userId", dto.getUserId());
        }
      }
      List<CrFilesAttachResultDTO> list = getNamedParameterJdbcTemplate().query(sql.toString(),
          params, BeanPropertyRowMapper.newInstance(CrFilesAttachResultDTO.class));
      if (list != null) {
        List<CrFilesAttachResultDTO> temp = new ArrayList<>();
        for (CrFilesAttachResultDTO cr : list) {
          cr.setFilePath(
              cr.getFilePath() != null ? PassProtector.encrypt(cr.getFilePath(), cr.getCrId())
                  : cr.getFilePath());
          temp.add(cr);
        }
        list = temp;
      }
      return list;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CrFilesAttachInsiteDTO> checkExistMop(String crId) {
    try {
      String sql = "select file_id fileId, dt_code dtCode, cr_id crId from CR_FILES_ATTACH where CR_ID = :crId ";
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      List<CrFilesAttachInsiteDTO> lstData = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrFilesAttachInsiteDTO.class));

      return lstData;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<CrFilesAttachDTO> getCrFileDT(String crId) {
    try {
      String sql = "select file_id fileId, dt_code dtCode, cr_id crId from CR_FILES_ATTACH where CR_ID = :crId AND dt_code is not null ";
      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      List<CrFilesAttachDTO> lstData = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrFilesAttachDTO.class));
      return lstData;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public void deleteFileDT(List<String> fileIds, Long crId) {
    if (fileIds.isEmpty()) {
      return;
    }
    for (String fileId : fileIds) {
      CrFilesAttachEntity crFilesAttachEntity = getEntityManager()
          .find(CrFilesAttachEntity.class, Long.valueOf(fileId));
      if (crFilesAttachEntity != null) {
        crFilesAttachEntity.setCrId(crFilesAttachEntity.getCrId() * -1);
        getEntityManager().merge(crFilesAttachEntity);
      }
      List<GnocFileEntity> gnocFileEntities = findByMultilParam(GnocFileEntity.class,
          "businessCode", GNOC_FILE_BUSSINESS.CR,
          "businessId", crId,
          "mappingId", Long.valueOf(fileId));
      for (GnocFileEntity entity : gnocFileEntities) {
        entity.setBusinessCode(GNOC_FILE_BUSSINESS.CR + "_DEL");
        getEntityManager().merge(entity);
      }
    }
  }

  @Override
  public List<CrFilesAttachDTO> getCrFileAttachForOutSide(CrFilesAttachDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    try {
      StringBuilder sqlBuilder = new StringBuilder(SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_FILE_ATTACH, "get-list-cr-dto"));
      Map<String, Object> params = new HashMap<>();
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getFileId())) {
        sqlBuilder.append(" AND a.FILE_ID = :FILE_ID");
        params.put("FILE_ID", crFilesAttachDTO.getFileId());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getFileName())) {
        sqlBuilder.append(" AND lower(a.FILE_NAME) LIKE :FILE_NAME ESCAPE '\\'");
        params.put("FILE_NAME",
            StringUtils.convertLowerParamContains(crFilesAttachDTO.getFileName()));
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getTimeAttack())) {
        sqlBuilder.append(" AND TO_CHAR(a.TIME_ATTACK,'dd/MM/yyyy') = :TIME_ATTACK");
        params.put("TIME_ATTACK", DateTimeUtils.convertDateToString(
            DateTimeUtils.convertStringToDate(crFilesAttachDTO.getTimeAttack())));
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getUserId())) {
        sqlBuilder.append(" AND a.USER_ID = :USER_ID");
        params.put("USER_ID", crFilesAttachDTO.getUserId());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getFileType())) {
        sqlBuilder.append(" AND a.FILE_TYPE = :FILE_TYPE");
        params.put("FILE_TYPE", crFilesAttachDTO.getFileType());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getCrId())) {
        sqlBuilder.append(" AND a.CR_ID = :CR_ID");
        params.put("CR_ID", crFilesAttachDTO.getCrId());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getFilePath())) {
        sqlBuilder.append(" AND a.FILE_PATH = :FILE_PATH");
        params.put("FILE_PATH", crFilesAttachDTO.getFilePath());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getTempImportId())) {
        sqlBuilder.append(" AND a.TEMP_IMPORT_ID = :TEMP_IMPORT_ID");
        params.put("TEMP_IMPORT_ID", crFilesAttachDTO.getTempImportId());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getFileSize())) {
        sqlBuilder.append(" AND a.FILE_SIZE = :FILE_SIZE");
        params.put("FILE_SIZE", crFilesAttachDTO.getFileSize());
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getDtCode())) {
        sqlBuilder.append(" AND lower(a.DT_CODE) LIKE :DT_CODE ESCAPE '\\'");
        params.put("DT_CODE", StringUtils.convertLowerParamContains(crFilesAttachDTO.getDtCode()));
      }
      if (StringUtils.isNotNullOrEmpty(crFilesAttachDTO.getDtFileHistory())) {
        sqlBuilder.append(" AND lower(a.DT_FILE_HISTORY) LIKE :DT_FILE_HISTORY ESCAPE '\\'");
        params.put("DT_FILE_HISTORY", StringUtils.convertLowerParamContains(crFilesAttachDTO.getDtFileHistory()));
      }
      if (StringUtils.isNotNullOrEmpty(sortFieldList)) {
        sqlBuilder.append(String.format(" ORDER BY %s ", sortFieldList));
        if (StringUtils.isNotNullOrEmpty(sortType)) {
          sqlBuilder.append(String.format(" %s", sortType));
        }
      }
      Query query = getEntityManager().createNativeQuery(sqlBuilder.toString());
      query.unwrap(NativeQuery.class).
          addScalar("fileId", new StringType()).
          addScalar("tempImportId", new StringType()).
          addScalar("fileName", new StringType()).
          addScalar("timeAttack", new StringType()).
          addScalar("userId", new StringType()).
          addScalar("fileType", new StringType()).
          addScalar("crId", new StringType()).
          addScalar("filePath", new StringType()).
          addScalar("fileSize", new StringType()).
          addScalar("dtCode", new StringType()).
          addScalar("dtFileHistory", new StringType())
          .setResultTransformer(Transformers.aliasToBean(CrFilesAttachDTO.class));
      for (Map.Entry<String, Object> item : params.entrySet()) {
        query.setParameter(item.getKey(), item.getValue());
      }
      query.setFirstResult(rowStart);
      query.setMaxResults(maxRow);
      return query.getResultList();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }
}
