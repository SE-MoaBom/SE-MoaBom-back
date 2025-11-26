package SE.demo.repository.programs;

import SE.demo.dto.programs.AvailabilityDto;
import SE.demo.dto.programs.ProgramDetailResponseDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProgramRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<ProgramDetailResponseDto> searchPrograms(
            String keyword,
            String status,
            String sort,
            int page,
            int size
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.program_id, p.title, p.description, p.thumbnail_url, p.backdrop_url, p.ranking, " +
                        "p.genre, p.running_time, " +
                        "CASE MAX(" +
                        "  CASE " +
                        "    WHEN pa.release_date IS NOT NULL THEN 3 " +
                        "    WHEN pa.expire_date IS NOT NULL THEN 2 " +
                        "    ELSE 1 " +
                        "  END" +
                        ") " +
                        "  WHEN 3 THEN 'UPCOMING' " +
                        "  WHEN 2 THEN 'EXPIRING' " +
                        "  ELSE NULL " +
                        "END AS status " +
                        "FROM Program p " +
                        "LEFT JOIN Program_Availability pa ON p.program_id = pa.program_id " +
                        "WHERE 1=1 "
        );
        List<Object> paramList = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND p.title LIKE ?");
            paramList.add("%" + keyword + "%");
        }
        if (status != null && !status.equals("ALL")) {
            sql.append(" AND (CASE " +
                    "WHEN pa.release_date IS NOT NULL THEN 'UPCOMING' " +
                    "WHEN pa.expire_date IS NOT NULL THEN 'EXPIRING' " +
                    "ELSE 'AVAILABLE' END) = ?");
            paramList.add(status);
        }

        sql.append(" GROUP BY p.program_id");

        if ("RANKING".equals(sort)) {
            sql.append(" ORDER BY COALESCE(p.ranking,99999) ASC, p.program_id ASC");
        } else {
            sql.append(" ORDER BY p.program_id ASC");
        }
        int offset = (page - 1) * size;
        sql.append(" Limit ? Offset ?");
        paramList.add(size);
        paramList.add(offset);

        return jdbcTemplate.query(
                sql.toString(),
                paramList.toArray(),
                (rs, rowNum) -> {
                    ProgramDetailResponseDto dto = new ProgramDetailResponseDto();

                    long id = rs.getLong("program_id");
                    dto.setProgramId(rs.wasNull() ? null : id);

                    dto.setTitle(rs.getString("title"));
                    dto.setDescription(rs.getString("description"));
                    dto.setThumbnailUrl(rs.getString("thumbnail_url"));
                    dto.setBackdropUrl(rs.getString("backdrop_url"));
                    dto.setGenre(rs.getString("genre"));
                    dto.setRunningTime(rs.getInt("running_time"));

                    int ranking = rs.getInt("ranking");
                    dto.setRanking(rs.wasNull() ? null : ranking);

                    dto.setStatus(rs.getString("status"));
                    dto.setWishlistId(null);

                    return dto;
                }
        );
    }

    public Long findWishList(int userId, Long programId) {
        String sql = "SELECT wishlist_id FROM Wishlist WHERE user_id = ? AND program_id = ?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{userId, programId},
                    Long.class
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int countPrograms(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT p.program_id) FROM Program p " +
                        "LEFT JOIN Program_Availability pa on p.program_id = pa.program_id " +
                        "WHERE 1=1"
        );
        List<Object> paramList = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND p.title LIKE ?");
            paramList.add("%" + keyword + "%");
        }
        if (status != null && !status.equals("ALL")) {
            sql.append(" AND (CASE " +
                    "WHEN pa.release_date IS NOT NULL THEN 'UPCOMING' " +
                    "WHEN pa.expire_date IS NOT NULL THEN 'EXPIRING' " +
                    "ELSE 'AVAILABLE' END) = ?");
            paramList.add(status);
        }
        return jdbcTemplate.queryForObject(sql.toString(), paramList.toArray(), Integer.class);
    }

    public ProgramDetailResponseDto getProgramDetail(long programId) {
        String sql =
                "SELECT p.program_id, p.title, p.description, p.thumbnail_url, p.backdrop_url, " +
                        "p.genre, p.running_time, p.ranking, " +
                        "CASE MAX(" +
                        "  CASE " +
                        "    WHEN pa.release_date IS NOT NULL THEN 3 " +
                        "    WHEN pa.expire_date IS NOT NULL THEN 2 " +
                        "    ELSE 1 " +
                        "  END" +
                        ") " +
                        "  WHEN 3 THEN 'UPCOMING' " +
                        "  WHEN 2 THEN 'EXPIRING' " +
                        "  ELSE NULL " +
                        "END AS status " +
                        "FROM Program p " +
                        "LEFT JOIN Program_Availability pa ON p.program_id = pa.program_id " +
                        "WHERE p.program_id = ? " +
                        "GROUP BY p.program_id";

        return jdbcTemplate.queryForObject(sql, new Object[]{programId}, (rs, rowNum) -> {
            ProgramDetailResponseDto dto = new ProgramDetailResponseDto();
            dto.setProgramId(rs.getLong("program_id"));
            dto.setTitle(rs.getString("title"));
            dto.setDescription(rs.getString("description"));
            dto.setThumbnailUrl(rs.getString("thumbnail_url"));
            dto.setBackdropUrl(rs.getString("backdrop_url"));
            dto.setGenre(rs.getString("genre"));
            dto.setRunningTime(rs.getInt("running_time"));

            int ranking = rs.getInt("ranking");
            dto.setRanking(rs.wasNull() ? null : ranking);

            dto.setStatus(rs.getString("status"));

            return dto;
        });
    }

    public List<AvailabilityDto> findAvailability(long programId) {
        String sql =
                "SELECT pa.ott_id, o.logo_url, pa.release_date, pa.expire_date "
                        + "from Program_Availability pa " +
                        "left join OTT o on pa.ott_id=o.ott_id " +
                        "WHERE pa.program_id=?";
        return jdbcTemplate.query(sql, new Object[]{programId}, (rs, rowNum) -> {
            AvailabilityDto dto = new AvailabilityDto();
            dto.setOttId(rs.getLong("ott_id"));
            dto.setLogoUrl(rs.getString("logo_url"));
            dto.setReleaseDate(rs.getObject("release_date", LocalDate.class));
            dto.setExpireDate(rs.getObject("expire_date", LocalDate.class));
            return dto;
        });
    }
}