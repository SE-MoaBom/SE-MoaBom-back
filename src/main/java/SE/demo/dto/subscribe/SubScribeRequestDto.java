package SE.demo.dto.subscribe;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubScribeRequestDto {
    private int ottId;
    private LocalDate startDate;
    private LocalDate endDate;
}
