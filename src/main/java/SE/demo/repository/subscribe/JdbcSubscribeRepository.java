package SE.demo.repository.subscribe;

import SE.demo.dto.subscribe.MydetailSubscribeDto;
import SE.demo.dto.subscribe.SubScribeRequestDto;
import SE.demo.dto.subscribe.UpdateRequestDto;
import SE.demo.entity.Subscribe;
import SE.demo.entity.User;
import SE.demo.exception.subscribe.CannotFindSubscribeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcSubscribeRepository implements SubScribeRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Subscribe> subscribeRowMapper = new RowMapper<>() {
        @Override
        public Subscribe mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subscribe sub = new Subscribe();
            sub.setSubscribeId(rs.getInt("subscribe_id"));
            sub.setUserId(rs.getInt("user_id"));
            sub.setOttId(rs.getInt("ott_id"));
            sub.setStartDate(rs.getDate("start_date").toLocalDate());
            sub.setEndDate(rs.getDate("end_date").toLocalDate());
            return sub;
        }
    };

    //구독 정보 등록
    public void saveSubScribeInfo(User user, SubScribeRequestDto requestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO Subscribe (user_id, ott_id, start_date, end_date) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "start_date = VALUES(start_date), " +
                "end_date = VALUES(end_date)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user.getUserId());
            ps.setInt(2, requestDto.getOttId());
            ps.setDate(3, requestDto.getStartDate() != null ?
                    java.sql.Date.valueOf(requestDto.getStartDate()) : null);
            ps.setDate(4, requestDto.getEndDate() != null ?
                    java.sql.Date.valueOf(requestDto.getEndDate()) : null);
            return ps;
        }, keyHolder);

    }

    //구독 정보 조회
    @Override
    public List<MydetailSubscribeDto> findMySubScribeInfo(User user) {
        int userId = user.getUserId();
        String sql = "SELECT s.subscribe_id, s.ott_id, o.name, o.logo_url, s.start_date, s.end_date " +
                "FROM `Subscribe` s " +
                "JOIN OTT o ON s.ott_id = o.ott_id " +   // 끝에 공백 추가
                "WHERE s.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            MydetailSubscribeDto dto = new MydetailSubscribeDto();
            dto.setSubscribeId(rs.getInt("subscribe_id"));
            dto.setOttId(rs.getInt("ott_id"));
            dto.setOttName(rs.getString("name"));
            dto.setLogoUrl(rs.getString("logo_url"));
            dto.setStartDate(rs.getDate("start_date") != null ?
                    rs.getDate("start_date").toLocalDate() : null);
            dto.setEndDate(rs.getDate("end_date") != null ?
                    rs.getDate("end_date").toLocalDate() : null);
            return dto;
        });
    }

    //구독 정보 수정
    @Override
    public void updateMySubScribeInfo(User user, UpdateRequestDto dto, int subscribeId) {
        String sql = "UPDATE Subscribe SET start_date = ?, end_date = ? " +
                "WHERE subscribe_id = ? AND user_id = ?";

        int updated = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDate(1, dto.getStartDate() != null ? java.sql.Date.valueOf(dto.getStartDate()) : null);
            ps.setDate(2, dto.getEndDate() != null ? java.sql.Date.valueOf(dto.getEndDate()) : null);
            ps.setInt(3, subscribeId);
            ps.setInt(4, user.getUserId()); // 본인 구독만 수정 가능
            return ps;
        });
        if (updated == 0) {
            throw new CannotFindSubscribeInfo("수정할 구독 정보를 찾을 수 없습니다.");
        }
    }

    @Override
    public void deleteMySubScribeInfo(User user, int subscribeId) {
        String sql = "DELETE FROM Subscribe WHERE subscribe_id = ? AND user_id = ?";
        int deleted = jdbcTemplate.update(sql, subscribeId, user.getUserId());

        if (deleted == 0) {
            throw new CannotFindSubscribeInfo("삭제할 구독 정보를 찾을 수 없습니다.");
        }
    }
}
