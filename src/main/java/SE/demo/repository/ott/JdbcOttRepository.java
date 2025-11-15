package SE.demo.repository.ott;

import SE.demo.dto.ott.OttDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcOttRepository implements OttRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<OttDto> getOttList() {
        String sql = "SELECT * FROM OTT";
        return jdbcTemplate.query(sql, new RowMapper<OttDto>() {
            @Override
            public OttDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                OttDto dto = new OttDto();
                dto.setOttId(rs.getInt("ott_id"));
                dto.setName(rs.getString("name"));
                dto.setPrice(rs.getInt("price"));
                dto.setLogoUrl(rs.getString("logo_url"));
                return dto;
            }
        });
    }

}
