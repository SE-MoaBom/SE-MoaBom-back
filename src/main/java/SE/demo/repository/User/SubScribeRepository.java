package SE.demo.repository.User;

import SE.demo.entity.Subscribe;
import java.util.List;

public interface SubScribeRepository {
    public Subscribe saveOttInfo(Subscribe subscribe);

    public List<Subscribe> getOttInfo(String username);

    public void updateOttInfo(Subscribe subscribe);

    public void deleteOttInfo(Subscribe subscribe);
}
