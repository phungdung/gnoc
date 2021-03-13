select STATION_CODE itemId, STATION_CODE itemName from common_gnoc.infra_stations where nvl(nation_code, 'VNM') = :nationCode
