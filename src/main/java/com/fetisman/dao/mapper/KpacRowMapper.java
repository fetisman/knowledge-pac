package com.fetisman.dao.mapper;

import com.fetisman.model.Kpac;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class KpacRowMapper implements RowMapper<Kpac> {
    @Override
    public Kpac mapRow(ResultSet rs, int rowNum) throws SQLException {
        Kpac kpac = new Kpac();
        kpac.setId(rs.getLong("id"));
        kpac.setTitle(rs.getString("title"));
        kpac.setDescription(rs.getString("description"));
        return kpac;
    }
}
