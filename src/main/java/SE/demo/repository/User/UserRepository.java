package SE.demo.repository.User;

import SE.demo.entity.User;
import java.util.Optional;

public interface UserRepository {
    public void saveUserInfo(User user);

    public Optional<User> getUserInfo(String username, String password);
}
