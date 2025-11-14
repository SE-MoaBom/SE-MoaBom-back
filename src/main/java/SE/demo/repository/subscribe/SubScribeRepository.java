package SE.demo.repository.subscribe;

import SE.demo.dto.subscribe.MydetailSubscribeDto;
import SE.demo.dto.subscribe.SubScribeRequestDto;
import SE.demo.dto.subscribe.UpdateRequestDto;
import SE.demo.entity.User;
import java.util.List;

public interface SubScribeRepository {
    void saveSubScribeInfo(User user, SubScribeRequestDto dto);

    List<MydetailSubscribeDto> findMySubScribeInfo(User user);

    void updateMySubScribeInfo(User user, UpdateRequestDto dto, int subscribeId);

    void deleteMySubScribeInfo(User user, int subscribeId);
}