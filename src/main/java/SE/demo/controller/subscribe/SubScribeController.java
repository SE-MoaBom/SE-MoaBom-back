package SE.demo.controller.subscribe;

import SE.demo.dto.subscribe.MydetailSubscribeDto;
import SE.demo.dto.subscribe.SubScribeRequestDto;
import SE.demo.dto.subscribe.UpdateRequestDto;
import SE.demo.entity.User;
import SE.demo.repository.subscribe.SubScribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubScribeController {
    private final SubScribeRepository subscribeRepository;

    @PostMapping("/subscriptions") //구독 정보 등록
    public ResponseEntity<?> saveOttInfo(@AuthenticationPrincipal User user, @RequestBody SubScribeRequestDto dto) {
        subscribeRepository.saveSubScribeInfo(user, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/subscriptions") //구독 정보 조회
    public ResponseEntity<?> getOttInfo(@AuthenticationPrincipal User user) {
        List<MydetailSubscribeDto> mySubScribeInfo = subscribeRepository.findMySubScribeInfo(user);
        return new ResponseEntity<>(mySubScribeInfo, HttpStatus.OK);
    }

    @PatchMapping("/subscriptions/{subscribeId}")
    public ResponseEntity<?> updateOttInfo(@AuthenticationPrincipal User user,
                                           @PathVariable int subscribeId,
                                           @RequestBody UpdateRequestDto dto) {
        subscribeRepository.updateMySubScribeInfo(user, dto, subscribeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/subscriptions/{subscribeId}")
    public ResponseEntity<?> deleteOttInfo(@AuthenticationPrincipal User user,
                                           @PathVariable int subscribeId) {
        subscribeRepository.deleteMySubScribeInfo(user, subscribeId);
        return ResponseEntity.ok().build();
    }
}
