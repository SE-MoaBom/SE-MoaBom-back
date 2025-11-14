package SE.demo.dto.subscribe;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SubScribeResponseDto {
    private int ottId;
    private LocalDate startDate;
    private LocalDate endDate;
}
