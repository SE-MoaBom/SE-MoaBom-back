package SE.demo.controller.ott;

import SE.demo.repository.ott.OttRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OttController {
    private final OttRepository ottRepository; // Repository 주입

    @GetMapping("/otts")
    public ResponseEntity<?> getOtts() {
        return ResponseEntity.ok(ottRepository.getOttList());
    }
}
