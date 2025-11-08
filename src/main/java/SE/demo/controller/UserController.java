package SE.demo.controller;

import SE.demo.entity.User;
import SE.demo.repository.User.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        userRepository.saveUserInfo(user);
        return ResponseEntity.status(201).body(user);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> userInfo = userRepository.getUserInfo(user.getUsername(), user.getPassword());
        if (userInfo.isPresent()) {
            return ResponseEntity.status(201).body(userInfo.get());
        }
        return ResponseEntity.status(404).body(null);
    }
}
