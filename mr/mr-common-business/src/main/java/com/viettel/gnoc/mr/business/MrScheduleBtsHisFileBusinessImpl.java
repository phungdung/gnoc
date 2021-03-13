package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisFileRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrScheduleBtsHisFileBusinessImpl implements MrScheduleBtsHisFileBusiness {

  @Autowired
  MrScheduleBtsHisFileRepository mrScheduleBtsHisFileRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;


  @Override
  public String delete(List<MrScheduleBtsHisFileDTO> mrScheduleBtsHisFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (mrScheduleBtsHisFileDTO != null && mrScheduleBtsHisFileDTO.size() > 0) {
      for (MrScheduleBtsHisFileDTO dto : mrScheduleBtsHisFileDTO) {
        if (!StringUtils.isStringNullOrEmpty(dto.getIdFile())) {
          MrScheduleBtsHisFileDTO checkDto = mrScheduleBtsHisFileRepository.findById(Long.valueOf(dto.getIdFile()));
          if (checkDto != null) {
            resultInSideDto = mrScheduleBtsHisFileRepository
                .deleteMrScheduleBtsHisFileByID(Long.parseLong(dto.getIdFile()));
            //xóa file trong bang GNOC FILE
            resultInSideDto = gnocFileRepository.deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.MR_SCHEDULE_BTS_HIS_FILE,
                Long.parseLong(dto.getIdFile()));
          } else {
            return RESULT.FAIL;
          }
        } else {
          return I18n.getLanguage("mrDeviceBtsService.check.IdFile");
        }
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
    }
    return resultInSideDto.getKey();
  }
}
