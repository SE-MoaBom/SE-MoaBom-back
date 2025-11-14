package SE.demo.dto.subscribe;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SubScribeRequestDto {
    private int ottId;
    private LocalDate startDate;
    private LocalDate endDate;
}
