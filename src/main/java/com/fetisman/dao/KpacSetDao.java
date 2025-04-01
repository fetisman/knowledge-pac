package com.fetisman.dao;

import com.fetisman.model.Kpac;
import com.fetisman.model.KpacSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class KpacSetDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private KpacDao kpacDao;

    public List<KpacSet> getAllSets() {
        String sql = "SELECT * FROM kpac_set";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(KpacSet.class));
    }

    public void createSet(String title, List<Long> kpacIds) {
    String sql = "INSERT INTO kpac_set (title) VALUES (?)";

    // Використовуємо KeyHolder для отримання згенерованого ID
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, title);
        return ps;
    }, keyHolder);

    long setId = Objects.requireNonNull(keyHolder.getKey()).longValue(); // Отримуємо ID нового запису

        // Якщо є вибрані K-PAC, додаємо зв’язки у проміжну таблицю
        if (!kpacIds.isEmpty()) {
            String linkSql = "INSERT INTO kpac_set_relation (set_id, kpac_id) VALUES (?, ?)";
            List<Object[]> batchArgs = kpacIds.stream()
                    .map(kpacId -> new Object[]{setId, kpacId})
                    .toList();
            jdbcTemplate.batchUpdate(linkSql, batchArgs);
        }
    }

    public void deleteSet(long id) {
        String sql = "DELETE FROM kpac_set WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<KpacSet> getSetById(Long id) {
        String sql = "SELECT * FROM kpac_set WHERE id = ?";
        try {
            KpacSet kpacSet = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                KpacSet set = new KpacSet();
                set.setId(rs.getLong("id"));
                set.setTitle(rs.getString("title"));
                return set;
            }, id);

            if (kpacSet != null) {
                List<Kpac> kpacs = kpacDao.getKpacs(getKpacIds(kpacSet.getId()));
                kpacSet.setKpacs(kpacs);
            }

            return Optional.ofNullable(kpacSet);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<Long> getKpacIds(Long setId){
        if (setId == null) {
            return Collections.emptyList();
        }

        String sql = "SELECT kpac_id FROM kpac_set_relation WHERE set_id = :setId";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("setId", setId);

        try {
            return namedParameterJdbcTemplate.query(
                    sql, parameters, (rs, rowNum) -> rs.getLong("kpac_id"));
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
