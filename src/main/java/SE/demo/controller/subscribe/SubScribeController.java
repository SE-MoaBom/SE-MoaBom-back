package SE.demo.controller.subscribe;

import SE.demo.entity.Subscribe;
import SE.demo.repository.subscribe.SubScribeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubScribeController {
    SubScribeRepository subscribeRepository;

    @PostMapping("/api/save/subscribeInfo")
    public ResponseEntity<?> saveOttInfo(@RequestBody Subscribe subscribe) {
        try {
            Subscribe savedSubscribe = subscribeRepository.saveOttInfo(subscribe);
            return new ResponseEntity<>(savedSubscribe, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/get/subscribeInfo")
    public ResponseEntity<?> getOttInfo(@RequestParam String username) {
        try {
            List<Subscribe> subscribes = subscribeRepository.getOttInfo(username);
            if (!subscribes.isEmpty()) {
                return new ResponseEntity<>(subscribes, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 사용자의 구독 정보가 없습니다.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/update/subscribeInfo")
    public ResponseEntity<?> updateOttInfo(@RequestBody Subscribe subscribe) {
        try {
            if (subscribe.getSubscribeId() == 0) {
                return new ResponseEntity<>("subscribeId가 필요합니다.", HttpStatus.BAD_REQUEST);
            }
            subscribeRepository.updateOttInfo(subscribe);
            return new ResponseEntity<>(subscribe, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/delete/subscribeInfo")
    public ResponseEntity<?> deleteOttInfo(@RequestBody Subscribe subscribe) {
        try {
            if (subscribe.getSubscribeId() == 0) {
                return new ResponseEntity<>("subscribeId가 필요합니다.", HttpStatus.BAD_REQUEST);
            }
            subscribeRepository.deleteOttInfo(subscribe);
            return new ResponseEntity<>("구독 정보가 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
