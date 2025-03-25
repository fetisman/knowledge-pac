package com.fetisman.dao;

import com.fetisman.model.Kpac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KpacDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Kpac> getAllKpacs() {
        String sql = "SELECT * FROM kpac";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Kpac.class));
//    return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            Kpac kpac = new Kpac();
//            kpac.setId(rs.getInt("id"));
//            kpac.setTitle(rs.getString("title"));
//            kpac.setDescription(rs.getString("description"));
//            kpac.setCreationDate(rs.getDate("creation_date").toLocalDate());
//            return kpac;
//        });
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

