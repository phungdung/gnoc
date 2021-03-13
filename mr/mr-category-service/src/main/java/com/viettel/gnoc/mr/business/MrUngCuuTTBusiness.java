package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrUngCuuTTBusiness {

  Datatable getListMrUctt(MrUngCuuTTDTO mrUngCuuTTDTO);

  ResultInSideDto insertMrUctt(List<MultipartFile> mrFile, MrUngCuuTTDTO mrUngCuuTTDTO);

  File exportSearchData(MrUngCuuTTDTO mrUngCuuTTDTO) throws Exception;
}
