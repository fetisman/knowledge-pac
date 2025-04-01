package com.fetisman.dao;

import com.fetisman.dao.mapper.KpacRowMapper;
import com.fetisman.model.Kpac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class KpacDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KpacRowMapper kpacRowMapper;

    public List<Kpac> getAllKpacs() {
        String sql = "SELECT * FROM kpac";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Kpac.class));
    }

    public List<Kpac> getKpacs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT * FROM kpac WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        try {
            return namedParameterJdbcTemplate.query(sql, parameters, kpacRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void addKpac(Kpac kpac) {
        String sql = "INSERT INTO kpac (title, description, creation_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, kpac.getTitle(), kpac.getDescription(), kpac.getCreationDate());
    }

    public void deleteKpac(int id) {
        String sql = "DELETE FROM kpac WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

