package SE.demo.repository.User;

import SE.demo.dto.GetMeDto;
import SE.demo.entity.User;

public interface UserRepository {
    void saveUserInfo(User user);

    User getUserInfo(String username, String password);

    GetMeDto getMeDto();
}
