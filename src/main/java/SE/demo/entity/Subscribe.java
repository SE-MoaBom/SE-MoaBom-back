package SE.demo.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Subscribe {
    private int subscribeId;
    private int userNumber;
    private String ottName;
    private LocalDate startDate;
    private LocalDate endDate;
}
