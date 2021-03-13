package com.viettel.gnoc.auth.config;

import com.viettel.security.PassTranformer;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
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

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.url(PassTranformer.decrypt(url));
    dataSourceBuilder.username(PassTranformer.decrypt(username));
    dataSourceBuilder.password(PassTranformer.decrypt(password));
    return dataSourceBuilder.build();
  }
}
