package SE.demo.controller.recommendation;

import SE.demo.dto.recommendation.RecommendationResponseDto;
import SE.demo.entity.User;
import SE.demo.service.recommendation.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/schedule")
    @Operation(summary = "최적의 시청 스케줄 추천", description = "사용자의 위시리스트를 바탕으로 최적의 구독 스케줄을 추천합니다.")
    public ResponseEntity<RecommendationResponseDto> getSchedule(
            @AuthenticationPrincipal User user
    ) {
        RecommendationResponseDto response = recommendationService.getOptimalSchedule(user);
        return ResponseEntity.ok(response);
    }
}
