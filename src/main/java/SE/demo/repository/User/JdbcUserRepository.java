package SE.demo.repository.User;

import SE.demo.dto.user.GetMeDto;
import SE.demo.entity.User;
import SE.demo.exception.user.PasswordNotEqualException;
import SE.demo.exception.user.UserDataAccessException;
import SE.demo.exception.user.UserNotFoundException;
import java.sql.PreparedStatement;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        String sql = "Insert into User(email,password) values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPassword());
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() == null) {
                throw new RuntimeException("생성된 user_id를 가져올 수 없습니다.");
            }

            user.setUserId(keyHolder.getKey().intValue());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("이미 사용중인 이메일입니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 실제 예외 확인
            throw new UserDataAccessException("사용자 정보를 저장하는 도중 오류가 발생했습니다.(서버문제)");
        }
    }

    //로그인
    @Override
    public User getUserInfo(String email, String passwordInput) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try {
            // 1. 이메일로 사용자 조회
            User user = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> User.builder()
                            .userId(rs.getInt("user_id"))
                            .email(rs.getString("email"))
                            .password(rs.getString("password"))
                            .build(),
                    email
            );
            if (!user.getPassword().equals(passwordInput)) {
                throw new PasswordNotEqualException("비밀번호가 일치하지 않습니다.");
            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("존재하지 않는 이메일입니다.");
        } catch (PasswordNotEqualException e) { //exception에 잡히니까 다시 던져줌
            throw e;
        } catch (Exception e) {
            throw new UserDataAccessException("사용자 정보를 가져오는 도중 오류가 발생했습니다.(서버문제)");
        }
    }

    @Override
    public GetMeDto getMeDto() {
        return null;
    }
}
