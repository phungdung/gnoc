package com.viettel.gnoc.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NetworkConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastCachingConfig {
  @Value("${application.hazelcast.mancenter}")
  private String mancenterUrl;
  @Value("${application.hazelcast.serverName:DEV}")
  private String name;
  @Value("${application.hazelcast.instanceName:instanceName}")
  private String instanceName;
  @Value("${application.hazelcast.timeToLiveSeconds:2000}")
  private int timeToLiveSeconds;
  @Value("${application.hazelcast.updateInterval:3}")
  private int updateInterval;
  @Value("${application.hazelcast.mapName}")
  private String mapName;
  @Value("${application.hazelcast.portNumber:9000}")
  private int portNumber;
  @Value("${application.hazelcast.portCount:20}")
  private int portCount;
  @Value("${application.hazelcast.cacheSize:1000}")
  private int cacheSize;
  @Value("${application.hazelcast.members}")
  private String hazelcastMembers;
  @Bean
  public Config hazelcastConfig() {
    Config config = new Config();
    config.getGroupConfig().setName(name);
    config.setInstanceName(instanceName)
        .addMapConfig(new MapConfig().setName(mapName)
        .setMaxSizeConfig(new MaxSizeConfig(cacheSize, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
        .setEvictionPolicy(EvictionPolicy.LRU)
        .setTimeToLiveSeconds(timeToLiveSeconds));
    config.getManagementCenterConfig()
        .setEnabled(true).setUrl(mancenterUrl)
        .setUpdateInterval(updateInterval);

    NetworkConfig networkConfig = config.getNetworkConfig();
    networkConfig.setPort(portNumber).setPortCount(portCount);
    networkConfig.setPortAutoIncrement(true);
    networkConfig.getJoin().getMulticastConfig().setEnabled(false);
    JoinConfig join = networkConfig.getJoin();
    join.getMulticastConfig().setMulticastPort(54327);
    String[] members = hazelcastMembers.split(",");
    if(members != null) {
      for(int i = 0; i < members.length; i++){
        String member = String.format("%s:%d", members[i], portNumber);
        join.getTcpIpConfig().addMember(member);
      }
    }
    join.getTcpIpConfig().setEnabled(true);
    return config;
  }
}
