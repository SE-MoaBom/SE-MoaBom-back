package SE.demo.dto.programs;

import java.time.LocalDate;
import lombok.Data;

@Data
public class AvailabilityDto {
    private Long ottId;
    private String logoUrl;
    private LocalDate releaseDate;
    private LocalDate expireDate;
}
