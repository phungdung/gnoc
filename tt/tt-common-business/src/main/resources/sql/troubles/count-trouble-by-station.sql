WITH tb AS
		(select * from (SELECT ts.CREATED_TIME,
													 ts.PRIORITY_ID,
													 ts.INSERT_SOURCE,
													 NVL(ide.station_code,ts.AFFECTED_NODE) AFFECTED_NODE,
													 ts.TROUBLE_CODE,
													 ts.reason_id,
													 ts.reason_name,  ts.trouble_name, to_char(ts.begin_trouble_time,'dd/MM/yyyy HH24:MI:ss') beginTroubleTime,
													 to_char(ts.end_trouble_time,'dd/MM/yyyy HH24:MI:ss') endTroubleTime, s.item_name , ts.receive_unit_name
										FROM one_tm.troubles ts
													 LEFT JOIN
														 (SELECT *
															FROM COMMON_GNOC.INFRA_DEVICE ie
																		 INNER JOIN COMMON_GNOC.INFRA_STATIONS iss
																			 ON ie.STATION_ID          = iss.STATION_ID
														 ) ide ON ts.AFFECTED_NODE = ide.DEVICE_CODE
													 LEFT JOIN COMMON_GNOC.cat_item s on   ts.state = s.item_id
										WHERE ts.CREATED_TIME       > to_date(:startTime,'dd/MM/yyyy HH24:mi:ss')
											AND ts.CREATED_TIME         < to_date(:endTime,'dd/MM/yyyy HH24:mi:ss')
									 )  temp where AFFECTED_NODE =:stationCode )

