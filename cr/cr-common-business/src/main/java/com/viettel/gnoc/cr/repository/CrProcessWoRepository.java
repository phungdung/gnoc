package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrProcessWoRepository {

  List<CrProcessWoDTO> getLstWoFromProcessId(String crProcessId);
}
