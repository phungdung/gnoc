package com.viettel.gnoc.commons.config;

import com.viettel.security.PassTranformer;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.hikari.connection-timeout:30000}")
  private int connectionTimeout;

  @Value("${spring.datasource.hikari.idle-timeout:60000}")
  private int idleTimeout;

  @Value("${spring.datasource.hikari.max-lifetime:180000}")
  private int maxLifeTime;

  @Value("${spring.datasource.hikari.maximum-pool-size:20}")
  private int maxPoolSize;

  @Value("${spring.datasource.hikari.minimum-idle:10}")
  private int minIdle;

  /*
  @Bean
  public DataSource dataSource1() {

    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    try {
      dataSourceBuilder.url(PassTranformer.decrypt(url));
      dataSourceBuilder.username(PassTranformer.decrypt(username));
      dataSourceBuilder.password(PassTranformer.decrypt(password));
    } catch (Exception e) {
      dataSourceBuilder.url(url);
      dataSourceBuilder.username(username);
      dataSourceBuilder.password(password);
    }
    return dataSourceBuilder.build();
  }
*/


  @Bean
  public DataSource getDataSource() {
    HikariDataSource hikariConfig = new HikariDataSource();
    hikariConfig.setDriverClassName("oracle.jdbc.OracleDriver");
    try {
      hikariConfig.setJdbcUrl(PassTranformer.decrypt(url));
      hikariConfig.setUsername(PassTranformer.decrypt(username));
      hikariConfig.setPassword(PassTranformer.decrypt(password));

      hikariConfig.setMaximumPoolSize(maxPoolSize);
      hikariConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
      hikariConfig.setMinimumIdle(minIdle);
      hikariConfig.setConnectionTimeout(connectionTimeout);
      hikariConfig.setIdleTimeout(idleTimeout);
      hikariConfig.setMaxLifetime(maxLifeTime);
//      hikariConfig.setLeakDetectionThreshold(60);

    } catch (Exception e) {
//      dataSourceBuilder.url(url);
//      dataSourceBuilder.username(username);
//      dataSourceBuilder.password(password);
      hikariConfig.setUsername(username);
      hikariConfig.setPassword(password);
      hikariConfig.setJdbcUrl(url);
      hikariConfig.setMaximumPoolSize(maxPoolSize);
      hikariConfig.setConnectionTestQuery("SELECT 1 FROM DUAL");
      hikariConfig.setMinimumIdle(minIdle);
      hikariConfig.setConnectionTimeout(connectionTimeout);
      hikariConfig.setIdleTimeout(idleTimeout);
      hikariConfig.setMaxLifetime(maxLifeTime);
    }
    return hikariConfig;
//    return dataSourceBuilder.build();
  }

}
