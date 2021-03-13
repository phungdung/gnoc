package com.viettel.gnoc.mr.repository;

public interface MrCdBatteryCreateWoRepository {

  boolean isValidCreateWo(String stationCode, String dcPower);
}
