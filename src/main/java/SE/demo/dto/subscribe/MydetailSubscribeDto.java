package SE.demo.dto.subscribe;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MydetailSubscribeDto {
    private int subscribeId;
    private int ottId;
    private String ottName;
    private String logoUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}
