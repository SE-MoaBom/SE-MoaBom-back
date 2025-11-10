package SE.demo.repository.subscribe;

import SE.demo.entity.Subscribe;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcSubscribeRepository implements SubScribeRepository {
    JdbcTemplate jdbcTemplate;

    private final RowMapper<Subscribe> subscribeRowMapper = new RowMapper<>() {
        @Override
        public Subscribe mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subscribe sub = new Subscribe();
            sub.setSubscribeId(rs.getInt("subscribe_id"));
            sub.setUserNumber(rs.getInt("user_number"));
            sub.setOttName(rs.getString("ott_name"));
            sub.setStartDate(rs.getDate("start_date").toLocalDate());
            sub.setEndDate(rs.getDate("end_date").toLocalDate());
            return sub;
        }
    };


    @Override
    public Subscribe saveOttInfo(Subscribe subscribe) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO subscribe (user_number, ott_name, start_date, end_date) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, subscribe.getUserNumber());
            ps.setString(2, subscribe.getOttName());
            ps.setDate(3, java.sql.Date.valueOf(subscribe.getStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(subscribe.getEndDate()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            subscribe.setSubscribeId(keyHolder.getKey().intValue());
        }
        return subscribe;
    }

    @Override
    public List<Subscribe> getOttInfo(String username) {
        String sql = "SELECT s.* FROM subscribe s JOIN user u ON s.user_number = u.user_number WHERE u.username = ?";
        return jdbcTemplate.query(sql, subscribeRowMapper, username);
    }

    @Override
    public void updateOttInfo(Subscribe subscribe) {
        String sql = "UPDATE subscribe SET ott_name = ?, start_date = ?, end_date = ? WHERE subscribe_id = ? AND user_number = ?";
        jdbcTemplate.update(sql,
                subscribe.getOttName(),
                java.sql.Date.valueOf(subscribe.getStartDate()),
                java.sql.Date.valueOf(subscribe.getEndDate()),
                subscribe.getSubscribeId(),
                subscribe.getUserNumber()
        );
    }

    @Override
    public void deleteOttInfo(Subscribe subscribe) {
        String sql = "delete from subscribe where subscribe_id = ?";
        jdbcTemplate.update(sql, subscribe.getSubscribeId());
    }
}
