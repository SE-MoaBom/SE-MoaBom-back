package SE.demo.controller.subscribe;

import SE.demo.dto.subscribe.MydetailSubscribeDto;
import SE.demo.dto.subscribe.SubScribeRequestDto;
import SE.demo.dto.subscribe.UpdateRequestDto;
import SE.demo.entity.User;
import SE.demo.exception.subscribe.CannotFindSubscribeInfo;
import SE.demo.repository.subscribe.SubScribeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Subscribe")
public class SubScribeController {
    private final SubScribeRepository subscribeRepository;

    @PostMapping("/subscriptions")
    @Operation(summary = "구독 정보 저장", description = "사용자의 OTT 구독 정보를 저장합니다.")
    @ApiResponse(responseCode = "201", content = @Content())
    public ResponseEntity<?> saveOttInfo(@AuthenticationPrincipal User user, @RequestBody SubScribeRequestDto dto) {
        subscribeRepository.saveSubScribeInfo(user, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "구독 정보 조회", description = "사용자의 OTT 구독 정보를 조회합니다.")
    public ResponseEntity<List<MydetailSubscribeDto>> getOttInfo(@AuthenticationPrincipal User user) {
        List<MydetailSubscribeDto> mySubScribeInfo = subscribeRepository.findMySubScribeInfo(user);
        return new ResponseEntity<>(mySubScribeInfo, HttpStatus.OK);
    }

    @PatchMapping("/subscriptions/{subscribeId}")
    @Operation(summary = "구독 정보 수정", description = "사용자의 OTT 구독 정보를 수정합니다.")
    @ApiResponse(responseCode = "204", content = @Content())
    @ApiResponse(responseCode = "404", content = @Content(), description = "Not Found")
    public ResponseEntity<?> updateOttInfo(@AuthenticationPrincipal User user,
                                           @PathVariable int subscribeId,
                                           @RequestBody UpdateRequestDto dto) {
        try {
            subscribeRepository.updateMySubScribeInfo(user, dto, subscribeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (CannotFindSubscribeInfo e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/subscriptions/{subscribeId}")
    @Operation(summary = "구독 정보 삭제", description = "사용자의 OTT 구독 정보를 삭제합니다.")
    @ApiResponse(responseCode = "204", content = @Content())
    @ApiResponse(responseCode = "404", content = @Content(), description = "Not Found")
    public ResponseEntity<?> deleteOttInfo(@AuthenticationPrincipal User user,
                                           @PathVariable int subscribeId) {
        try {
            subscribeRepository.deleteMySubScribeInfo(user, subscribeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (CannotFindSubscribeInfo e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
