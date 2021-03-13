package com.viettel.gnoc.security.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Base repository.
 *
 * @author TungPV
 */
@Repository
@Slf4j
public abstract class BaseRepository<TDTO, T> {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }

  protected EntityManager getEntityManager() {
    return entityManager;
  }
}

