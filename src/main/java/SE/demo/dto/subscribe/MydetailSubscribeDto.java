package SE.demo.dto.subscribe;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MydetailSubscribeDto {
    private int subscribeID;
    private int ottID;
    private String ottName;
    private String logoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}
