package SE.demo.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Subscribe {
    private int subscribeId;
    private int userId;
    private int ottId;
    private LocalDate startDate;
    private LocalDate endDate;
}
