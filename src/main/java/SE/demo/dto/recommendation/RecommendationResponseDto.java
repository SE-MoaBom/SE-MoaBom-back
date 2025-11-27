package SE.demo.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDto {
    private Integer totalCostSavings;
    private List<RecommendationActionDto> actions;
}
