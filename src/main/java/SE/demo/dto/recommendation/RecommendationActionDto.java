package SE.demo.dto.recommendation;

import SE.demo.dto.wishlist.WishlistProgramDto;
import lombok.Data;

import java.util.List;

@Data
public class RecommendationActionDto {
    private String ottName;
    private DateRangeDto dateRange;
    private List<WishlistProgramDto> programs;
}
