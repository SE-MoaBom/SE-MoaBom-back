package SE.demo.controller.ott;

import SE.demo.dto.ott.OttDto;
import SE.demo.repository.ott.OttRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "OTT")
public class OttController {
    private final OttRepository ottRepository; // Repository 주입

    @GetMapping("/otts")
    @Operation(summary = "OTT 목록 조회", description = "서비스가 제공하는 전체 OTT 목록을 조회합니다.")
    public ResponseEntity<List<OttDto>> getOtts() {
        return ResponseEntity.ok(ottRepository.getOttList());
    }
}
