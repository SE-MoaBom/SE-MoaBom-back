package SE.demo.repository.User;

import SE.demo.entity.User;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //회원가입
    @Override
    public void saveUserInfo(User user) {
        String sql = "Insert into user(username,password) values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_number"});
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps;
        }, keyHolder);

        user.setUserNumber(keyHolder.getKey().intValue());
    }

    //로그인
    @Override
    public Optional<User> getUserInfo(String username, String passwordInput) {
        String sql = "SELECT * FROM user WHERE username = ?";

        try {
            User user = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        String dbPassword = rs.getString("password");
                        if (dbPassword.equals(passwordInput)) { // 나중에 BCrypt 적용
                            return User.builder()
                                    .userNumber(rs.getInt("user_number"))
                                    .username(rs.getString("username"))
                                    .password(dbPassword)
                                    .build();
                        }
                        return null;
                    },
                    username
            );

            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
