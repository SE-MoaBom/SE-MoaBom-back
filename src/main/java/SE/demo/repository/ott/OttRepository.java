package SE.demo.repository.ott;

import SE.demo.dto.ott.OttDto;
import java.util.List;

public interface OttRepository {
    List<OttDto> getOttList();
}
