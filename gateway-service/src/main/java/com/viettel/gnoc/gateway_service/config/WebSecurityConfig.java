package com.viettel.gnoc.gateway_service.config;

import com.viettel.gnoc.gateway_service.security.AuthenticationManager;
import com.viettel.gnoc.gateway_service.security.SecurityContextRepository;
import com.viettel.gnoc.security.util.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  SecurityContextRepository securityContextRepository;

  @Bean
  SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .exceptionHandling()
        .authenticationEntryPoint((swe, e)-> {
          return Mono.fromRunnable(()->{
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
          });
        }).accessDeniedHandler((swe, e)->{
          return Mono.fromRunnable(()->{
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization");
            swe.getResponse().getHeaders().set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
          });
        }).and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers("/oauthVsa/**").permitAll()
        .pathMatchers("/mr-category-service/download/**").permitAll()
//        // Common Stream
//        .pathMatchers("/common-stream-service/**").permitAll()
//        .pathMatchers("/common-stream-service/DeviceTypeVersionService/**").hasAuthority(SecurityConstants.UTILITY_VERSION_CATALOG)
//        .pathMatchers("/common-stream-service/TransitionStateConfig/**").hasAuthority(SecurityConstants.UTILITY_STATUS_CONFIG)
//        .pathMatchers("/common-stream-service/GnocLanguageService/**").hasAuthority(SecurityConstants.UTILITY_LANGUAGE_CONFIG)
//        .pathMatchers("/common-stream-service/EmployeeImpactController/**").hasAuthority(SecurityConstants.UTILITY_CONFIG_EMPLOYEE_IMPACT)
//        .pathMatchers("/common-stream-service/CfgRequestScheduleController/**").hasAuthority(SecurityConstants.UTILITY_CONFIG_REQUEST_SCHEDULE)
//        .pathMatchers("/common-stream-service/EmployeeDayOffController/**").hasAuthority(SecurityConstants.UTILITY_CONFIG_EMPLOYEE_DAY_OFF)
//        .pathMatchers("/common-stream-service/ShiftHandoverController/**").hasAuthority(SecurityConstants.UTILITY_CONFIG_SHIFT_HANDOVER)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_WORK_LOGS)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_WO_CD_TEMP)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_USER_PERSONAL)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CAT_ITEM)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_BUSINESS_CALL_SMS)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_WO_TEST_SERVICE)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CATEGORY)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_UNIT)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_SMS_GATEWAY)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_MAPPING_VSA_UNIT)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_MAP_NETWORK_LEVEL)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_SEARCH_SERVICE_CR)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_WO_FILE_TEMP)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_CLOSED_TICKET)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_REQUIRE_HAVE_WO)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_LANGUAGE_EXCHANGE)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_CREATE_WO)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_USER_APPROVE)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CFG_TROUBLE_CALL_IPCC)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_IPCC_SERVICE)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CR_DT_TEMPLATE_FILE)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_COORDINATION_SETTING)
//        .pathMatchers("/common-stream-service/**").hasAuthority(SecurityConstants.UTILITY_CONFIG_WORK_TIME_FOLLOW)
//        .pathMatchers("/common-stream-service/import/**").permitAll()
//        .pathMatchers("/common-stream-service/export/**").permitAll()
//        .pathMatchers("/common-stream-service/download/**").permitAll()
        .pathMatchers("/common-stream-service/textEditor/**").permitAll()
//        .pathMatchers("/common-stream-service/TableConfigUserService/**").permitAll()
//        .pathMatchers("/common-stream-service/SearchConfigUserService/**").permitAll()
//        .pathMatchers("/common-stream-service/theSign/**").permitAll()
//        .pathMatchers("/common-stream-service/commonStreamAPI/**").permitAll()
//        // OD
//        .pathMatchers("/od-service/od/**").hasAuthority(SecurityConstants.OD_WORKFLOW_MANAGEMENT)
//        .pathMatchers("/od-category-service/odCfgBusiness/**").hasAuthority(SecurityConstants.OD_CONFIG_BUSINESS)
//        .pathMatchers("/od-category-service/OdCfgScheduleCreate/**").hasAuthority(SecurityConstants.OD_SCHEDULE_CREATE)
//        .pathMatchers("/od-category-service/odType/**").hasAuthority(SecurityConstants.OD_TYPE_MANAGEMENT)
//        .pathMatchers("/GnocODOutsideServices/**").permitAll()
//        // PT
//        .pathMatchers("/pt-service/**").hasAuthority(SecurityConstants.PT_PROBLEM)
//        .pathMatchers("/pt-category-service/**").hasAuthority(SecurityConstants.PT_CONFIG_TIME_HANDLE_PROBLEM)
//        .pathMatchers("/GnocPTOutsideServices/**").permitAll()
//        // KEDB
//        .pathMatchers("/kedb-service/**").hasAuthority(SecurityConstants.KEDB_MANAGEMENT)
//        .pathMatchers("/GnocKedbOutsideServices/**").permitAll()
//        // TT
//        .pathMatchers("/tt-service/**").hasAuthority(SecurityConstants.TT_TROUBLE)
//        .pathMatchers("/tt-category-service/**").hasAuthority(SecurityConstants.TT_CONFIG_MOP)
//        .pathMatchers("/tt-category-service/**").hasAuthority(SecurityConstants.TT_CONFIG_RECEIVE_UNIT)
//        .pathMatchers("/tt-category-service/**").hasAuthority(SecurityConstants.TT_CONFIG_TIME)
//        .pathMatchers("/tt-category-service/**").hasAuthority(SecurityConstants.TT_INFO_CONFIG)
//        .pathMatchers("/tt-category-service/**").hasAuthority(SecurityConstants.TT_MESSAGE_CONFIG)
//        .pathMatchers("/GnocTTOutsideServices/**").permitAll()
//        // CR
//        .pathMatchers("/cr-service/**").hasAuthority(SecurityConstants.CR_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_ALARM_SETTING)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_GROUP_DEPARTMENT_CONFIG)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_DEVICE_TYPE_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_SCOPES_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_ROLES_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_IMPACT_SEGMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_AFFECTED_LEVEL)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_DEPARTMENTS_ROLES_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_DEPARTMENTS_SCOPES_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_ROLES_SCOPES_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_PROCESS_MANAGEMENT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CAB_USERS)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_GROUP_UNIT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_CHILD_ARRAY)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_ACTION_CODE)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_IMPACT_FRAME)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_TEMP_IMPORT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_WEBSERVICE_IMPORT)
//        .pathMatchers("/cr-category-service/**").hasAuthority(SecurityConstants.CR_CONFIG_RETURN_ACTION)
//        .pathMatchers("/GnocCrOutsideServices/**").permitAll()
//        // WO
//        .pathMatchers("/wo-service/**").hasAuthority(SecurityConstants.WO_MANAGEMENT)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_ERROR_CASE_MANAGEMENT)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_MAP_PROVINCE_CD_GROUP)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_CD_GROUP_MANAGEMENT)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_TYPE_MANAGEMENT)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_MATERIALS_CONFIG)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_CONFIG_WO_HELP_VSMART)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_CONFIG_PROPERTY)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_UPDATE_SERVICE_INFRA)
//        .pathMatchers("/wo-category-service/**").hasAuthority(SecurityConstants.WO_CONFIG_MAP_UNIT_GNOC_NIMS)
//        .pathMatchers("/GnocWoOutsideServices/**").permitAll()
//        // SR
//        .pathMatchers("/sr-service/**").hasAuthority(SecurityConstants.SR_MANAGEMENT)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_CATALOG)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_ROLE)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_ROLE_USER)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_STATUS)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_ROLE_ACTION)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_SERVICE_ARRAY)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_SERVICE_GROUP)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_MAPPING_PROCESS_CR)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_REASON_REJECT)
//        .pathMatchers("/sr-category-service/**").hasAuthority(SecurityConstants.SR_CONFIGURATION)
//        .pathMatchers("/GnocSrOutsideServices/**").permitAll()
//        // MR
//        .pathMatchers("/mr-service/**").hasAuthority(SecurityConstants.SR_MANAGEMENT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_HISTORY)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_SCHEDULE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_GENERATE_CR)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_PROCEDURE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_UNIT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_CAT_DEVICE_TYPE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_CAT_CONTENT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_IT_CAT_MARKET)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_TELECOM)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_TELECOM_DEVICE_LIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_TELECOM_UNIT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_TELECOM_PROCEDURE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SOFT_TELECOM_SCHEDULE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_DEVICE_LIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_PROCEDURE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_UNIT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_CHECKLIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_CONTENT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_WO_TIME)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_WORK_ITEM)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_SCHEDULE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_IT_HISTORY)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_DEVICE_LIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_GROUP)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_PROCEDURE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_CYCLE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_WORK_ITEM)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_SCHEDULE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_HISTORY)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_CHECKLIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_MATERIAL_REPLACE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_DEVICE_LIST)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_CYCLE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_PROCEDURE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_SCHEDULE)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_HARD_ELECTRONIC_BTS_HISTORY)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_VIBAS)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_VIBA_LISTS)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_VIBA_UNITS)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_VIBA_HISTORY)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_UCTT)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_DEVICE_NIMS)
//        .pathMatchers("/mr-category-service/**").hasAuthority(SecurityConstants.MR_SCHEDULE_DISCHARGE)
//        .pathMatchers("/GnocMrOutsideServices/**").permitAll()
//        // RISK
//        .pathMatchers("/risk-service/**").hasAuthority(SecurityConstants.RISK_MANAGEMENT)
//        .pathMatchers("/risk-category-service/**").hasAuthority(SecurityConstants.RISK_SYSTEMS)
//        .pathMatchers("/risk-category-service/**").hasAuthority(SecurityConstants.RISK_TYPES)
//        .pathMatchers("/risk-category-service/**").hasAuthority(SecurityConstants.RISK_CFG_BUSINESS)
//        .pathMatchers("/GnocRiskOutsideServices/**").permitAll()
        .anyExchange().authenticated()
//        .anyExchange().permitAll()
        .and().build();
  }
}
