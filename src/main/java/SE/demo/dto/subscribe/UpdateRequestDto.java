package SE.demo.dto.subscribe;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
}
