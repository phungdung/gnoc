package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrFileObject;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportDataRepository {

  List<CrFileObject> getListTemplateFileByProcess(String processTypeId, String type, String locale);
}
