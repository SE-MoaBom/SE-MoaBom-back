package SE.demo.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DateRangeDto {
    private LocalDate start;
    private LocalDate end;
}
