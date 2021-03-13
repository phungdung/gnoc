package com.viettel.gnoc.wo.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface WoCdTempRepository {


  Long getFtByCdConfig(Long cdId);
}
