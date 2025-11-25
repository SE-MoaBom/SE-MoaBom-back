package SE.demo.dto.programs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProgramResponseDto {
    private Long programId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String backdropUrl;
    private String genre;
    private Integer runningTime;
    private Integer rank;
    private String status; //UPCOMING | EXPIRING | NULL
    private Long wishlistId;
}
