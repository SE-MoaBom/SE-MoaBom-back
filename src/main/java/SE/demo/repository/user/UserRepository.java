package SE.demo.repository.user;

import SE.demo.dto.user.GetMeDto;
import SE.demo.entity.User;

public interface UserRepository {
    void saveUserInfo(User user);

    User getUserInfo(String username, String password);

    GetMeDto getMeDto();
}
