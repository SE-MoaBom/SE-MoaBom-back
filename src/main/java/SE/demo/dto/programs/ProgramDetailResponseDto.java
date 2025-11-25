package SE.demo.dto.programs;

import java.util.List;
import lombok.Data;

@Data
public class ProgramDetailResponseDto {
    private Long programId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String backdropUrl;
    private String genre;
    private Integer runningTime;
    private Integer ranking;
    private String status; // UPCOMING | EXPIRING | NULL
    private List<AvailabilityDto> availability;
    private Long wishlistId;
}
